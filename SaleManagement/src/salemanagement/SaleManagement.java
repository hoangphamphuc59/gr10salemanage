package salemanagement;

import data.Customer;
import data.Inventory;
import data.Management;
import data.Product;
import data.RegularCustomer;
import data.Report;
import data.Transaction;
import data.VipCustomer;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class SaleManagement {

    private static Management management;
    private static Scanner sc = new Scanner(System.in);
    
    // Clone arraylists for future use
    private static ArrayList<Customer> clonedCustomerList;
    private static ArrayList<Product> clonedProductList;
    private static ArrayList<Transaction> clonedTransactionList;

    public static void main(String[] args) {
        start();
        mainMenu();
    }
    
    public static void start() {
        management = new Management();
        
        management.setCustomerList(new ArrayList<>());
        management.setProductList(new ArrayList<>());
        management.setTransactionList(new ArrayList<>());
        
        management.loadCustomer();
        management.loadProduct();
        management.loadTransaction();
        
        clonedCustomerList = new ArrayList<>(management.getCustomerList());
        clonedProductList = new ArrayList<>(management.getProductList());
        clonedTransactionList = new ArrayList<>(management.getTransactionList());
        
        System.out.println("System started and data loaded successfully.");
    }
    
    private static void saveData() {
        management.setCustomerList(clonedCustomerList);
        management.setProductList(clonedProductList);
        management.setTransactionList(clonedTransactionList);
        
        management.saveCustomer();
        management.saveProduct();
        management.saveTransaction();
    }

    // ==========================================
    // HELPER METHODS (Input Validation & Display)
    // ==========================================
    
    private static void pause() {
        System.out.println("--------------------------------------");
        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }
    
    private static int readInt(String prompt, int min, int max) {
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

    private static double readDouble(String prompt, double min, double max) {
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

    private static String readString(String prompt) {
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

    // ==========================================
    // MENUS
    // ==========================================
    private static void mainMenu() {
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
                saveData();
                System.out.println("Goodbye!");
                break;
            }
        }
    }

    // --- PRODUCT MANAGEMENT ---
    private static void productMenu() {
        while (true) {
            System.out.println("\n--- PRODUCT MANAGEMENT ---");
            System.out.println("1. Add new product");
            System.out.println("2. Update product information");
            System.out.println("3. Remove a product");
            System.out.println("4. View all products");
            System.out.println("5. Search products by name or category");
            System.out.println("0. Back to Main Menu");
            
            int choice = readInt("Please enter your choice (0-5): ", 0, 5);
            
            if (choice == 1) { // Add product
                String id = readString("Input product ID: ");
                if (findProduct(id) != null) {
                    System.out.println("Product ID already exists!");
                    continue;
                }
                String name = readString("Input product name: ");
                String cat = readString("Input product category: ");
                double price = readDouble("Input product price: ", 0.01, Double.MAX_VALUE);
                int stock = readInt("Input product stock quantity: ", 0, Integer.MAX_VALUE);
                
                clonedProductList.add(new Product(id, name, cat, price, stock));
                saveData();
                System.out.println("Product added successfully!");
                pause();

            } else if (choice == 2) { // Update product
                String id = readString("Input ID of product to update: ");
                Product p = findProduct(id);
                if (p == null) {
                    System.out.println("Product not found!");
                } else {
                    p.setProductName(readString("Input new product name: "));
                    p.setCategory(readString("Input new category: "));
                    p.setPrice(readDouble("Input new price: ", 0.01, Double.MAX_VALUE));
                    p.setStock(readInt("Input new stock quantity: ", 0, Integer.MAX_VALUE));
                    saveData();
                    System.out.println("Product updated successfully!");
                }
                pause();

            } else if (choice == 3) { // Remove product
                String id = readString("Input ID of product to remove: ");
                Product p = findProduct(id);
                if (p == null) {
                    System.out.println("Product not found!");
                } else {
                    clonedProductList.remove(p);
                    saveData();
                    System.out.println("Product removed successfully!");
                }
                pause();

            } else if (choice == 4) { // View all
                System.out.println("--------------------------------------");
                if (clonedProductList.isEmpty()) {
                    System.out.println("No products available.");
                } else {
                    for (Product p : clonedProductList) {
                        p.showProduct();
                    }
                }
                pause();

            } else if (choice == 5) { // Search
                String keyword = readString("Input name or category to search: ").toLowerCase();
                System.out.println("--------------------------------------");
                boolean found = false;
                for (Product p : clonedProductList) {
                    if (p.getProductName().toLowerCase().contains(keyword) || 
                        p.getCategory().toLowerCase().contains(keyword)) {
                        p.showProduct();
                        found = true;
                    }
                }
                if (!found) System.out.println("No products found matching '" + keyword + "'.");
                pause();

            } else if (choice == 0) {
                break;
            }
        }
    }

    private static Product findProduct(String id) {
        for (Product p : clonedProductList) {
            if (p.getProductId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    // --- CUSTOMER MANAGEMENT ---
    private static void customerMenu() {
        while (true) {
            System.out.println("\n--- CUSTOMER MANAGEMENT ---");
            System.out.println("1. Add new customer");
            System.out.println("2. Update customer information");
            System.out.println("3. Remove a customer");
            System.out.println("4. View all customers");
            System.out.println("0. Back to Main Menu");
            
            int choice = readInt("Please enter your choice (0-4): ", 0, 4);
            
            if (choice == 1) {
                String id = readString("Input customer ID: ");
                if (findCustomer(id) != null) {
                    System.out.println("Customer ID already exists!");
                    continue;
                }
                String name = readString("Input customer name: ");
                String phone = readString("Input phone: ");
                String email = readString("Input email: ");
                String address = readString("Input address: ");
                int age = readInt("Input age: ", 1, 150);
                String gender = readString("Input gender: ");
                int isVip = readInt("Is VIP? (1 for YES, 0 for NO): ", 0, 1);

                if (isVip == 1) {
                    clonedCustomerList.add(new VipCustomer(id, name, phone, email, address, age, gender));
                } else {
                    clonedCustomerList.add(new RegularCustomer(id, name, phone, email, address, age, gender));
                }
                saveData();
                System.out.println("Customer added successfully!");
                pause();

            } else if (choice == 2) {
                String id = readString("Input ID of customer to update: ");
                Customer c = findCustomer(id);
                if (c == null) {
                    System.out.println("Customer not found!");
                } else {
                    c.setName(readString("Input new name: "));
                    c.setPhone(readString("Input new phone: "));
                    c.setEmail(readString("Input new email: "));
                    c.setAddress(readString("Input new address: "));
                    c.setAge(readInt("Input new age: ", 1, 150));
                    c.setGender(readString("Input new gender: "));
                    saveData();
                    System.out.println("Customer updated successfully!");
                }
                pause();

            } else if (choice == 3) {
                String id = readString("Input ID of customer to remove: ");
                Customer c = findCustomer(id);
                if (c == null) {
                    System.out.println("Customer not found!");
                } else {
                    clonedCustomerList.remove(c);
                    saveData();
                    System.out.println("Customer removed successfully!");
                }
                pause();

            } else if (choice == 4) {
                System.out.println("--------------------------------------");
                if (clonedCustomerList.isEmpty()) {
                    System.out.println("No customers available.");
                } else {
                    for (Customer c : clonedCustomerList) {
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

    private static Customer findCustomer(String id) {
        for (Customer c : clonedCustomerList) {
            if (c.getId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    // --- SALES MANAGEMENT ---
    private static void salesMenu() {
        while (true) {
            System.out.println("\n--- SALES MANAGEMENT ---");
            System.out.println("1. Create a new sales transaction");
            System.out.println("2. Update or cancel a transaction");
            System.out.println("3. View transaction history");
            System.out.println("0. Back to Main Menu");
            
            int choice = readInt("Please enter your choice (0-3): ", 0, 3);
            
            if (choice == 1) { // Create Transaction
                String transId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                
                String cusId = readString("Input Customer ID for this transaction: ");
                Customer customer = findCustomer(cusId);
                if (customer == null) {
                    System.out.println("Customer not found. Transaction aborted.");
                    continue;
                }
                
                HashMap<Product, Integer> cart = new HashMap<>();
                
                while (true) {
                    String pId = readString("Input Product ID to add (or type 'DONE' to finish): ");
                    if (pId.equalsIgnoreCase("DONE")) break;
                    
                    Product product = findProduct(pId);
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
                
                Transaction transaction = new Transaction(transId, customer, LocalDate.now(), 0.0, "PENDING", cart);
                transaction.calculateTotal(); // Calculate total initially
                
                System.out.println("Initial Total Amount: $" + transaction.getTotalAmount());
                int confirm = readInt("Confirm this transaction? (1 for YES, 0 for NO): ", 0, 1);
                
                if (confirm == 1) {
                    // We must save any pending data before checking inventory so Management matches Main
                    saveData();
                    
                    Inventory inventory = new Inventory();
                    transaction.confirmTransaction(inventory, management);
                    
                    if ("CONFIRMED".equalsIgnoreCase(transaction.getStatus())) {
                        clonedTransactionList.add(transaction);
                        // Reload products because inventory updated them on disk
                        management.loadProduct(); 
                        clonedProductList = new ArrayList<>(management.getProductList());
                        saveData();
                        System.out.println("Transaction finalized and saved!");
                    } else {
                        System.out.println("Transaction failed (possibly out of stock).");
                    }
                } else {
                    transaction.cancelTransaction();
                    clonedTransactionList.add(transaction);
                    saveData();
                    System.out.println("Transaction cancelled and saved to history.");
                }
                pause();

            } else if (choice == 2) { // Update / Cancel
                String tId = readString("Input Transaction ID to update/cancel: ");
                Transaction t = null;
                for (Transaction tr : clonedTransactionList) {
                    if (tr.getTransactionId().equalsIgnoreCase(tId)) {
                        t = tr;
                        break;
                    }
                }
                
                if (t == null) {
                    System.out.println("Transaction not found!");
                } else if ("CONFIRMED".equalsIgnoreCase(t.getStatus())) {
                    System.out.println("Cannot update a confirmed transaction (requires refund logic not implemented yet).");
                } else if ("CANCELLED".equalsIgnoreCase(t.getStatus())) {
                    System.out.println("Transaction is already cancelled.");
                } else {
                    // It's PENDING or FAILED
                    System.out.println("1. Update product quantity");
                    System.out.println("2. Cancel transaction");
                    int act = readInt("Choose action (1 or 2): ", 1, 2);
                    
                    if (act == 1) {
                        String pId = readString("Input Product ID to update: ");
                        int newQty = readInt("Input new quantity (0 to remove): ", 0, Integer.MAX_VALUE);
                        t.updateProductQuantity(pId, newQty);
                        saveData();
                        System.out.println("Transaction updated!");
                    } else {
                        t.cancelTransaction();
                        saveData();
                        System.out.println("Transaction cancelled!");
                    }
                }
                pause();
                
            } else if (choice == 3) { // View History
                System.out.println("--------------------------------------");
                if (clonedTransactionList.isEmpty()) {
                    System.out.println("No transactions found.");
                } else {
                    for (Transaction t : clonedTransactionList) {
                        t.displayTransaction();
                    }
                }
                pause();
                
            } else if (choice == 0) {
                break;
            }
        }
    }

    // --- REPORTING ---
    private static void reportMenu() {
        Report report = new Report(clonedTransactionList);
        
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
                String val = (rType == 1) ? readString("Input date (YYYY-MM-DD): ") : readString("Input month (YYYY-MM): ");
                String typeStr = (rType == 1) ? "DAILY" : "MONTHLY";
                System.out.println("--------------------------------------");
                report.bestSellingProducts(val, typeStr);
                pause();
            } else if (choice == 4) {
                int rType = readInt("Report type (1: Daily, 2: Monthly): ", 1, 2);
                String val = (rType == 1) ? readString("Input date (YYYY-MM-DD): ") : readString("Input month (YYYY-MM): ");
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
