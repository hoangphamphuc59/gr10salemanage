package IO;

import models.Customer;
import models.Product;
import models.RegularCustomer;
import models.Transaction;
import models.VipCustomer;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

public class IOHelper {

    private static final String DATA_DIR = "data" + File.separator;
    private static final String fCus = DATA_DIR + "customer.txt";
    private static final String fPro = DATA_DIR + "product.txt";
    private static final String fTra = DATA_DIR + "transaction.txt";

    public IOHelper() {
        // Ensure data directory exists
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // ==========================================
    // FORMAT HELPERS
    // ==========================================
    private String formatCustomer(Customer customer) {
        return String.format("|%s|%s|%s|%s|%s|%d|%s|%.2f|", customer.getId(), customer.getName(), customer.getPhone(), customer.getEmail(), customer.getAddress(), customer.getAge(), customer.getGender(), customer.getDiscount());
    }

    private String formatProduct(Product product) {
        return String.format("|%s|%s|%s|%.2f|%d|", product.getProductId(), product.getProductName(), product.getCategory(), product.getPrice(), product.getStock());
    }

    private String formatTransaction(Transaction transaction) {
        String result = String.format("|%s", transaction.getTransactionId())
                + formatCustomer(transaction.getCustomer())
                + String.format("%s|%.2f|%s|", transaction.getDate(), transaction.getTotalAmount(), transaction.getStatus());

        HashMap<Product, Integer> items = transaction.getItems();

        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product key = entry.getKey();
            Integer value = entry.getValue();
            result = result + String.format("%d", value) + formatProduct(key);
        }

        result = result + "#|";
        return result;
    }

    // ==========================================
    // SAVE METHODS
    // ==========================================
    public int saveCustomer(ArrayList<Customer> cusList) {
        File fileName = new File(fCus);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Customer customer : cusList) {
                writer.write(formatCustomer(customer));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    public int saveProduct(ArrayList<Product> proList) {
        File fileName = new File(fPro);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Product product : proList) {
                writer.write(formatProduct(product));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    public int saveTransaction(ArrayList<Transaction> traList) {
        File fileName = new File(fTra);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Transaction transaction : traList) {
                writer.write(formatTransaction(transaction));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    // ==========================================
    // LOAD METHODS
    // ==========================================
    public ArrayList<Customer> loadCustomer() {
        ArrayList<Customer> cusList = new ArrayList<>();
        File fileName = new File(fCus);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create file: " + fCus);
            }
            return cusList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split("\\|");
                if (tokens.length >= 9) {
                    String id = tokens[1].trim();
                    String name = tokens[2].trim();
                    String phone = tokens[3].trim();
                    String email = tokens[4].trim();
                    String address = tokens[5].trim();
                    int age = Integer.parseInt(tokens[6].trim());
                    String gender = tokens[7].trim();
                    double discount = Double.parseDouble(tokens[8].trim());

                    if (discount > 0.0) {
                        cusList.add(new VipCustomer(id, name, email, phone, address, age, gender));
                    } else {
                        cusList.add(new RegularCustomer(id, name, email, phone, address, age, gender));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading customers.");
        }
        return cusList;
    }

    public ArrayList<Product> loadProduct() {
        ArrayList<Product> proList = new ArrayList<>();
        File fileName = new File(fPro);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create file: " + fPro);
            }
            return proList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split("\\|");
                if (tokens.length >= 6) {
                    String productId = tokens[1].trim();
                    String productName = tokens[2].trim();
                    String category = tokens[3].trim();
                    double price = Double.parseDouble(tokens[4].trim());
                    int stock = Integer.parseInt(tokens[5].trim());

                    proList.add(new Product(productId, productName, category, price, stock));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading products.");
        }
        return proList;
    }

    public ArrayList<Transaction> loadTransaction() {
        ArrayList<Transaction> traList = new ArrayList<>();
        File fileName = new File(fTra);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create file: " + fTra);
            }
            return traList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split("\\|");

                if (tokens.length >= 13) {
                    try {
                        String transId = tokens[1].trim();
                        String cusId = tokens[2].trim();
                        String cusName = tokens[3].trim();
                        String cusPhone = tokens[4].trim();
                        String cusEmail = tokens[5].trim();
                        String cusAddress = tokens[6].trim();
                        int cusAge = Integer.parseInt(tokens[7].trim());
                        String cusGender = tokens[8].trim();
                        double discount = Double.parseDouble(tokens[9].trim());

                        String dateStr = tokens[10].trim();
                        double totalAmount = Double.parseDouble(tokens[11].trim());
                        String status = tokens[12].trim();

                        Customer customer;
                        if (discount > 0.0) {
                            customer = new VipCustomer(cusId, cusName, cusEmail, cusPhone, cusAddress, cusAge, cusGender);
                        } else {
                            customer = new RegularCustomer(cusId, cusName, cusEmail, cusPhone, cusAddress, cusAge, cusGender);
                        }

                        HashMap<Product, Integer> items = new HashMap<>();
                        int i = 13;
                        while (i < tokens.length) {
                            String currentToken = tokens[i].trim();
                            if (currentToken.equals("#") || currentToken.isEmpty()) break;

                            int quantity = Integer.parseInt(currentToken);
                            String prodId = tokens[i + 1].trim();
                            String prodName = tokens[i + 2].trim();
                            String prodCat = tokens[i + 3].trim();
                            double prodPrice = Double.parseDouble(tokens[i + 4].trim());
                            int prodStock = Integer.parseInt(tokens[i + 5].trim());

                            Product product = new Product(prodId, prodName, prodCat, prodPrice, prodStock);
                            items.put(product, quantity);
                            i += 6;
                        }

                        LocalDate localDate = LocalDate.parse(dateStr);
                        Transaction transaction = new Transaction(transId, customer, localDate, totalAmount, status, items);
                        traList.add(transaction);

                    } catch (Exception e) {
                        System.out.println("Error processing transaction line, skipping.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions.");
        }
        return traList;
    }
}
