package ui;

import IO.IOHelper;
import models.*;
import services.*;
import services.Validation.*;

import java.util.*;

public class ConsoleUi {
    private final IOHelper ioHelper;
    private final ProductManager productManager;
    private final CustomerManager customerManager;
    private final TransactionManager transactionManager;
    private final CustomerValidation customerValidation;
    private final ProductValidation productValidation;

    private final Scanner sc;

    public ConsoleUi(IOHelper ioHelper,
            ProductManager productManager,
            CustomerManager customerManager,
            TransactionManager transactionManager,
            CustomerValidation customerValidation,
            ProductValidation productValidation) {
        this.ioHelper = ioHelper;
        this.productManager = productManager;
        this.customerManager = customerManager;
        this.transactionManager = transactionManager;
        this.customerValidation = customerValidation;
        this.productValidation = productValidation;
        this.sc = new Scanner(System.in);
    }

    public void start() {
        // ioHelper = new IOHelper();
        // productManager = new ProductManager();
        // customerManager = new CustomerManager();
        // transactionManager = new TransactionManager();

        // Load data from files into managers
        productManager.setProductList(ioHelper.loadProduct());
        customerManager.setCustomerList(ioHelper.loadCustomer());
        transactionManager.setTransactionList(ioHelper.loadTransaction());

        ConsoleColor.printSuccess("System started and data loaded successfully.");
    }

    private void saveAllData() {
        ioHelper.saveProduct(productManager.getProductList());
        ioHelper.saveCustomer(customerManager.getCustomerList());
        ioHelper.saveTransaction(transactionManager.getTransactionList());
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================
    private void pause() {
        System.out.println("--------------------------------------");
        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

    private int readInt(String prompt, int min, int max) {
        int value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Integer.parseInt(sc.nextLine());
                if (value < min || value > max) {
                    ConsoleColor.printError("input out of range (" + min + " to " + max + "), input again!");
                    continue;
                }
                break;
            } catch (Exception e) {
                ConsoleColor.printError("wrong input format, input again!");
            }
        }
        return value;
    }

    private double readDouble(String prompt, double min, double max) {
        double value;
        while (true) {
            try {
                System.out.print(prompt);
                value = Double.parseDouble(sc.nextLine());
                if (value < min || value > max) {
                    ConsoleColor.printError("input out of range, input again!");
                    continue;
                }
                break;
            } catch (Exception e) {
                ConsoleColor.printError("wrong input format, input again!");
            }
        }
        return value;
    }

    private String readString(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine().trim();
            if (value.isEmpty()) {
                ConsoleColor.printError("input cannot be empty, input again!");
                continue;
            }
            break;
        }
        return value;
    }

    private String readCustomerField(String prompt, String fieldName) {
        while (true) {
            String rawValue = readString(prompt);
            try {
                if (rawValue == null)
                    throw new IllegalArgumentException("Cannot empty");
                return customerValidation.validate(fieldName, rawValue);
            } catch (IllegalArgumentException e) {
                ConsoleColor.printError("Invalid input: " + e.getMessage());
            }
        }
    }

    private String readProductField(String prompt, String fieldName) {
        while (true) {
            String rawValue = readString(prompt);
            try {
                if (rawValue == null) {
                    throw new IllegalArgumentException("Cannot be empty");
                }
                return productValidation.validate(fieldName, rawValue);
            } catch (IllegalArgumentException e) {
                ConsoleColor.printError("Invalid input: " + e.getMessage());
            }
        }
    }

    // ==========================================
    // MENUS
    // ==========================================
    public void mainMenu() {
        while (true) {
            System.out.println("\n========== SALE MANAGEMENT SYSTEM ==========");
            System.out.println("1. Product Management");
            System.out.println("2. Customer Management");
            System.out.println("3. Sales Management");
            System.out.println("4. Reporting");
            System.out.println("0. Exit");

            int choice = readInt("Please enter your choice (0-4): ", 0, 4);

            if (choice == 1) {
                productMenu();
            } else if (choice == 2) {
                customerMenu();
            } else if (choice == 3) {
                salesMenu();
            } else if (choice == 4) {
                reportMenu();
            } else if (choice == 0) {
                System.out.println("Saving data and exiting...");
                saveAllData();
                System.out.println("Goodbye!");
                break;
            }
        }
    }

    // --- PRODUCT MANAGEMENT ---
    private void productMenu() {
        while (true) {
            System.out.println("\n--- PRODUCT MANAGEMENT ---");
            System.out.println("1. Add new product");
            System.out.println("2. Update product information");
            System.out.println("3. Remove a product");
            System.out.println("4. View all products");
            System.out.println("5. Search products by name or category");
            System.out.println("0. Back to Main Menu");

            int choice = readInt("Please enter your choice (0-5): ", 0, 5);

            if (choice == 1) {
                String id;
                while (true) {
                    id = readProductField("Input product ID: ", "id");
                    if (productManager.findById(id) != null) {
                        ConsoleColor.printError("Product ID already exists! Please input a different ID.");
                        continue;
                    }
                    break;
                }

                String name = readProductField("Input product name: ", "name");
                String cat = readProductField("Input product category: ", "category");
                double price = readDouble("Input product price: ", 0.01, Double.MAX_VALUE);
                int stock = readInt("Input product stock quantity: ", 0, Integer.MAX_VALUE);

                boolean result = productManager.addProduct(id, name, cat, price, stock);
                if (result) {
                    saveAllData();
                    ConsoleColor.printSuccess("Product added successfully!");
                } else {
                    ConsoleColor.printError("Failed to add product! (ID may already exist or invalid input)");
                }
                pause();

            } else if (choice == 2) {
                String id = readProductField("Input product ID: ", "id");
                if (productManager.findById(id) == null) {
                    ConsoleColor.printError("Product not found!");
                } else {
                    String name = readProductField("Input new product name: ", "name");

                    String cat = readProductField("Input new category: ", "category");

                    double price = readDouble("Input new price: ", 0.01, Double.MAX_VALUE);
                    int stock = readInt("Input new stock quantity: ", 0, Integer.MAX_VALUE);

                    boolean result = productManager.updateProduct(id, name, cat, price, stock);
                    if (result) {
                        saveAllData();
                        ConsoleColor.printSuccess("Product updated successfully!");
                    } else {
                        ConsoleColor.printError("Failed to update product!");
                    }
                }
                pause();

            } else if (choice == 3) {
                String id = readString("Input ID of product to remove: ");
                boolean result = productManager.removeProduct(id);
                if (result) {
                    saveAllData();
                    ConsoleColor.printSuccess("Product removed successfully!");
                } else {
                    ConsoleColor.printError("Product not found!");
                }
                pause();

            } else if (choice == 4) {
                System.out.println("--------------------------------------");
                ArrayList<Product> products = productManager.getProductList();
                if (products.isEmpty()) {
                    ConsoleColor.printError("No products available.");
                } else {
                    for (Product p : products) {
                        p.showProduct();
                    }
                }
                pause();

            } else if (choice == 5) {
                String keyword = readString("Input name or category to search: ");
                System.out.println("--------------------------------------");
                ArrayList<Product> results = productManager.searchByNameOrCategory(keyword);
                if (results.isEmpty()) {
                    ConsoleColor.printError("No products found matching '" + keyword + "'.");
                } else {
                    for (Product p : results) {
                        p.showProduct();
                    }
                }
                pause();

            } else if (choice == 0) {
                break;
            }
        }
    }

    // --- CUSTOMER MANAGEMENT ---
    private void customerMenu() {
        while (true) {
            System.out.println("\n--- CUSTOMER MANAGEMENT ---");
            System.out.println("1. Add new customer");
            System.out.println("2. Update customer information");
            System.out.println("3. Remove a customer");
            System.out.println("4. View all customers");
            System.out.println("0. Back to Main Menu");

            int choice = readInt("Please enter your choice (0-4): ", 0, 4);

            if (choice == 1) {
                String id;
                while (true) {
                    id = readCustomerField("Input customer ID: ", "id");
                    if (customerManager.findById(id) != null) {
                        ConsoleColor.printError("Customer ID already exists! Please input a different ID.");
                        continue;
                    }
                    break;
                }
                String name = readCustomerField("Input customer name: ", "name");
                String phone = readCustomerField("Input phone: ", "phone");
                String email = readCustomerField("Input email: ", "email");
                String address = readString("Input address: ");
                int age = readInt("Input age: ", 1, 150);
                String gender = readString("Input gender: ");
                int isVip = readInt("Is VIP? (1 for YES, 0 for NO): ", 0, 1);
                Customer cus;
                if (isVip == 1) {
                    cus = new VipCustomer(id, name, email, phone, address, age, gender);
                } else {
                    cus = new RegularCustomer(id, name, email, phone, address, age, gender);
                }
                boolean result = customerManager.addCustomer(cus);
                if (result) {
                    saveAllData();
                    ConsoleColor.printSuccess("Customer added successfully!");
                } else {
                    ConsoleColor.printError("Failed to add customer! (ID may already exist or invalid input)");
                }
                pause();

            } else if (choice == 2) {
                String id = readCustomerField("Input ID of customer to update: ", "id");
                if (customerManager.findById(id) == null) {
                    ConsoleColor.printError("Customer not found!");
                } else {
                    String name = readCustomerField("Input new name: ", "name");
                    String phone = readCustomerField("Input new phone: ", "phone");
                    String email = readString("Input new email: ");
                    String address = readString("Input new address: ");
                    int age = readInt("Input new age: ", 1, 150);
                    String gender = readString("Input new gender: ");

                    boolean result = customerManager.updateCustomer(id, name, phone, email, address, age, gender);
                    if (result) {
                        saveAllData();
                        ConsoleColor.printSuccess("Customer updated successfully!");
                    } else {
                        ConsoleColor.printError("Failed to update customer!");
                    }
                }
                pause();

            } else if (choice == 3) {
                String id = readString("Input ID of customer to remove: ");
                boolean result = customerManager.removeCustomer(id);
                if (result) {
                    saveAllData();
                    ConsoleColor.printSuccess("Customer removed successfully!");
                } else {
                    ConsoleColor.printError("Customer not found!");
                }
                pause();

            } else if (choice == 4) {
                System.out.println("--------------------------------------");
                ArrayList<Customer> customers = customerManager.getCustomerList();
                if (customers.isEmpty()) {
                    ConsoleColor.printError("No customers available.");
                } else {
                    for (Customer c : customers) {
                        System.out.printf("%-10s | %-25s | %-15s | %-15.2f\n",
                                c.getId(), c.getName(), c.getPhone(), c.getDiscount());
                    }
                }
                pause();

            } else if (choice == 0) {
                break;
            }
        }
    }

    // --- SALES MANAGEMENT ---
    private void salesMenu() {
        while (true) {
            System.out.println("\n--- SALES MANAGEMENT ---");
            System.out.println("1. Create a new sales transaction");
            System.out.println("2. Update or cancel a transaction");
            System.out.println("3. View transaction history");
            System.out.println("0. Back to Main Menu");

            int choice = readInt("Please enter your choice (0-3): ", 0, 3);

            if (choice == 1) {
                String transId = transactionManager.generateTransactionId();

                String cusId = readString("Input Customer ID for this transaction: ");
                Customer customer = customerManager.findById(cusId);
                if (customer == null) {
                    ConsoleColor.printError("Customer not found. Transaction aborted.");
                    continue;
                }

                HashMap<Product, Integer> cart = new HashMap<>();

                while (true) {
                    String pId = readString("Input Product ID to add (or type 'DONE' to finish): ");
                    if (pId.equalsIgnoreCase("DONE"))
                        break;

                    Product product = productManager.findById(pId);
                    if (product == null) {
                        ConsoleColor.printError("Product not found!");
                        continue;
                    }

                    int qty = readInt("Input quantity to buy: ", 1, Integer.MAX_VALUE);
                    cart.put(product, cart.getOrDefault(product, 0) + qty);
                    ConsoleColor.printSuccess("Added " + qty + " of " + product.getProductName() + " to cart.");
                }

                if (cart.isEmpty()) {
                    ConsoleColor.printError("Transaction must have at least one product. Transaction aborted.");
                    continue;
                }

                Transaction transaction = transactionManager.createTransaction(transId, customer, cart);
                if (transaction == null) {
                    ConsoleColor.printError("Failed to create transaction.");
                    continue;
                }

                System.out.println("Total Amount: $" + transaction.getTotalAmount());
                int confirm = readInt("Confirm this transaction? (1 for YES, 0 for NO): ", 0, 1);

                if (confirm == 1) {
                    // Confirm delegates to InventoryManager for stock check
                    boolean confirmed = transactionManager.confirmTransaction(transaction,
                            productManager.getProductList());
                    if (confirmed) {
                        saveAllData();
                        ConsoleColor.printSuccess("Transaction finalized and saved!");
                    } else {
                        ConsoleColor.printError("Transaction failed (possibly out of stock).");
                    }
                } else {
                    transactionManager.cancelTransaction(transaction);
                    transactionManager.getTransactionList().put(transaction.getTransactionId(), transaction);
                    saveAllData();
                    ConsoleColor.printSuccess("Transaction cancelled and saved to history.");
                }
                pause();

            } else if (choice == 2) {
                String tId = readString("Input Transaction ID to update/cancel: ");
                Transaction t = transactionManager.findById(tId);

                if (t == null) {
                    ConsoleColor.printError("Transaction not found!");
                } else if ("CONFIRMED".equalsIgnoreCase(t.getStatus())) {
                    ConsoleColor.printError("Cannot update a confirmed transaction.");
                } else if ("CANCELLED".equalsIgnoreCase(t.getStatus())) {
                    ConsoleColor.printError("Transaction is already cancelled.");
                } else {
                    System.out.println("1. Update product quantity");
                    System.out.println("2. Cancel transaction");
                    System.out.println("3. Confirm transaction");
                    int act = readInt("Choose action (1 to 3): ", 1, 3);

                    if (act == 1) {
                        String pId = readString("Input Product ID to update: ");
                        int newQty = readInt("Input new quantity (0 to remove): ", 0, Integer.MAX_VALUE);
                        boolean updated = transactionManager.updateProductQuantity(t, pId, newQty);
                        if (updated) {
                            saveAllData();
                            ConsoleColor.printSuccess("Transaction updated!");
                        } else {
                            ConsoleColor.printError("Product not found in this transaction.");
                        }
                    } else if (act == 2) {
                        transactionManager.cancelTransaction(t);
                        saveAllData();
                        ConsoleColor.printSuccess("Transaction cancelled!");
                    } else if (act == 3) {
                        boolean confirmed = transactionManager.confirmTransaction(t, productManager.getProductList());
                        if (confirmed) {
                            saveAllData();
                            ConsoleColor.printSuccess("Transaction finalized and saved!");
                        } else {
                            saveAllData();
                            ConsoleColor.printError("Transaction failed (possibly out of stock), SAVE AS FAILED.");
                        }
                    }
                }
                pause();

            } else if (choice == 3) {
                System.out.println("--------------------------------------");
                Map<String, Transaction> transactions = transactionManager.getTransactionList();
                if (transactions.isEmpty()) {
                    ConsoleColor.printError("No transactions found.");
                } else {
                    for (Map.Entry<String, Transaction> t : transactions.entrySet()) {
                        t.getValue().displayTransaction();
                    }
                }
                pause();

            } else if (choice == 0) {
                break;
            }
        }
    }

    // --- REPORTING ---
    private void reportMenu() {
        ReportManager report = new ReportManager(transactionManager.getTransactionList());

        while (true) {
            System.out.println("\n--- REPORTING ---");
            System.out.println("1. Generate Daily Sales Report");
            System.out.println("2. Generate Monthly Sales Report");
            System.out.println("3. List Best-Selling Products");
            System.out.println("4. List Customers with Highest Purchase Value");
            System.out.println("0. Back to Main Menu");

            int choice = readInt("Please enter your choice (0-4): ", 0, 4);

            if (choice == 1) {
                String date = readString("Input date (YYYY-MM-DD): ");
                System.out.println("--------------------------------------");
                report.dailySaleReport(date);
                pause();
            } else if (choice == 2) {
                String month = readString("Input month (YYYY-MM): ");
                System.out.println("--------------------------------------");
                report.monthlySaleReport(month);
                pause();
            } else if (choice == 3) {
                int rType = readInt("Report type (1: Daily, 2: Monthly): ", 1, 2);
                String val = (rType == 1) ? readString("Input date (YYYY-MM-DD): ")
                        : readString("Input month (YYYY-MM): ");
                String typeStr = (rType == 1) ? "DAILY" : "MONTHLY";
                System.out.println("--------------------------------------");
                report.bestSellingProducts(val, typeStr);
                pause();
            } else if (choice == 4) {
                int rType = readInt("Report type (1: Daily, 2: Monthly): ", 1, 2);
                String val = (rType == 1) ? readString("Input date (YYYY-MM-DD): ")
                        : readString("Input month (YYYY-MM): ");
                String typeStr = (rType == 1) ? "DAILY" : "MONTHLY";
                System.out.println("--------------------------------------");
                report.highestPurchaseCustomer(val, typeStr);
                pause();
            } else if (choice == 0) {
                break;
            }
        }
    }
}
