package ui;

import IO.IOHelper;
import models.*;
import services.*;
import services.Validation.CustomerValidation;

import java.util.*;

public class ConsoleUi {
    private final IOHelper ioHelper;
    private final ProductManager productManager;
    private final CustomerManager customerManager;
    private final TransactionManager transactionManager;
    private final CustomerValidation customerValidation;
    private final Scanner sc;

    public ConsoleUi(IOHelper ioHelper,
            ProductManager productManager,
            CustomerManager customerManager,
            TransactionManager transactionManager,
            CustomerValidation customerValidation) {
        this.ioHelper = ioHelper;
        this.productManager = productManager;
        this.customerManager = customerManager;
        this.transactionManager = transactionManager;
        this.customerValidation = customerValidation;
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

        System.out.println("System started and data loaded successfully.");
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
                    System.out.println("input out of range (" + min + " to " + max + "), input again!");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("wrong input format, input again!");
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
                    System.out.println("input out of range, input again!");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("wrong input format, input again!");
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
                System.out.println("input cannot be empty, input again!");
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
                if(rawValue == null) throw new IllegalArgumentException("Cannot empty");
                return customerValidation.validate(fieldName, rawValue);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
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
                String id = readString("Input product ID: ");
                
                String name = readString("Input product name: ");
                String cat = readString("Input product category: ");
                double price = readDouble("Input product price: ", 0.01, Double.MAX_VALUE);
                int stock = readInt("Input product stock quantity: ", 0, Integer.MAX_VALUE);

                boolean result = productManager.addProduct(id, name, cat, price, stock);
                if (result) {
                    saveAllData();
                    System.out.println("Product added successfully!");
                } else {
                    System.out.println("Failed to add product! (ID may already exist or invalid input)");
                }
                pause();

            } else if (choice == 2) {
                String id = readString("Input ID of product to update: ");
                if (productManager.findById(id) == null) {
                    System.out.println("Product not found!");
                } else {
                    String name = readString("Input new product name: ");
                    String cat = readString("Input new category: ");
                    double price = readDouble("Input new price: ", 0.01, Double.MAX_VALUE);
                    int stock = readInt("Input new stock quantity: ", 0, Integer.MAX_VALUE);

                    boolean result = productManager.updateProduct(id, name, cat, price, stock);
                    if (result) {
                        saveAllData();
                        System.out.println("Product updated successfully!");
                    } else {
                        System.out.println("Failed to update product!");
                    }
                }
                pause();

            } else if (choice == 3) {
                String id = readString("Input ID of product to remove: ");
                boolean result = productManager.removeProduct(id);
                if (result) {
                    saveAllData();
                    System.out.println("Product removed successfully!");
                } else {
                    System.out.println("Product not found!");
                }
                pause();

            } else if (choice == 4) {
                System.out.println("--------------------------------------");
                ArrayList<Product> products = productManager.getProductList();
                if (products.isEmpty()) {
                    System.out.println("No products available.");
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
                    System.out.println("No products found matching '" + keyword + "'.");
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
                String id = readCustomerField("Input customer ID: ", "id");
                String name = readCustomerField("Input customer name: ", "name");
                String phone = readCustomerField("Input phone: ", "phone");
                String email = readString("Input email: ");
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
                    System.out.println("Customer added successfully!");
                } else {
                    System.out.println("Failed to add customer! (ID may already exist or invalid input)");
                }
                pause();

            } else if (choice == 2) {
                String id = readCustomerField("Input ID of customer to update: ", "id");
                if (customerManager.findById(id) == null) {
                    System.out.println("Customer not found!");
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
                        System.out.println("Customer updated successfully!");
                    } else {
                        System.out.println("Failed to update customer!");
                    }
                }
                pause();

            } else if (choice == 3) {
                String id = readString("Input ID of customer to remove: ");
                boolean result = customerManager.removeCustomer(id);
                if (result) {
                    saveAllData();
                    System.out.println("Customer removed successfully!");
                } else {
                    System.out.println("Customer not found!");
                }
                pause();

            } else if (choice == 4) {
                System.out.println("--------------------------------------");
                ArrayList<Customer> customers = customerManager.getCustomerList();
                if (customers.isEmpty()) {
                    System.out.println("No customers available.");
                } else {
                    for (Customer c : customers) {
                        System.out.printf("ID: %s | Name: %s | Phone: %s | VIP Discount: %.2f\n",
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
                    System.out.println("Customer not found. Transaction aborted.");
                    continue;
                }

                HashMap<Product, Integer> cart = new HashMap<>();

                while (true) {
                    String pId = readString("Input Product ID to add (or type 'DONE' to finish): ");
                    if (pId.equalsIgnoreCase("DONE"))
                        break;

                    Product product = productManager.findById(pId);
                    if (product == null) {
                        System.out.println("Product not found!");
                        continue;
                    }

                    int qty = readInt("Input quantity to buy: ", 1, Integer.MAX_VALUE);
                    cart.put(product, cart.getOrDefault(product, 0) + qty);
                    System.out.println("Added " + qty + " of " + product.getProductName() + " to cart.");
                }

                if (cart.isEmpty()) {
                    System.out.println("Transaction must have at least one product. Transaction aborted.");
                    continue;
                }

                Transaction transaction = transactionManager.createTransaction(transId, customer, cart);
                if (transaction == null) {
                    System.out.println("Failed to create transaction.");
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
                        System.out.println("Transaction finalized and saved!");
                    } else {
                        System.out.println("Transaction failed (possibly out of stock).");
                    }
                } else {
                    transactionManager.cancelTransaction(transaction);
                    transactionManager.getTransactionList().put(transaction.getTransactionId(), transaction);
                    saveAllData();
                    System.out.println("Transaction cancelled and saved to history.");
                }
                pause();

            } else if (choice == 2) {
                String tId = readString("Input Transaction ID to update/cancel: ");
                Transaction t = transactionManager.findById(tId);

                if (t == null) {
                    System.out.println("Transaction not found!");
                } else if ("CONFIRMED".equalsIgnoreCase(t.getStatus())) {
                    System.out.println("Cannot update a confirmed transaction.");
                } else if ("CANCELLED".equalsIgnoreCase(t.getStatus())) {
                    System.out.println("Transaction is already cancelled.");
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
                            System.out.println("Transaction updated!");
                        } else {
                            System.out.println("Product not found in this transaction.");
                        }
                    } else if (act == 2) {
                        transactionManager.cancelTransaction(t);
                        saveAllData();
                        System.out.println("Transaction cancelled!");
                    } else if (act == 3) {
                        boolean confirmed = transactionManager.confirmTransaction(t, productManager.getProductList());
                        if (confirmed) {
                            saveAllData();
                            System.out.println("Transaction finalized and saved!");
                        } else {
                            saveAllData();
                            System.out.println("Transaction failed (possibly out of stock), SAVE AS FAILED.");
                        }
                    }
                }
                pause();

            } else if (choice == 3) {
                System.out.println("--------------------------------------");
                Map<String, Transaction> transactions = transactionManager.getTransactionList();
                if (transactions.isEmpty()) {
                    System.out.println("No transactions found.");
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
