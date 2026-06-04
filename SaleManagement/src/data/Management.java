package data;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.Map;

public class Management {

    private ArrayList<Customer> cusList;
    private ArrayList<Product> proList;
    private ArrayList<Transaction> traList;

    private static final String fCus = "customer.txt";
    private static final String fPro = "product.txt";
    private static final String fTra = "transaction.txt";

    public Management() {
    }

    public ArrayList<Customer> getCustomerList() {
        return cusList;
    }

    public void setCustomerList(ArrayList<Customer> cusList) {
        this.cusList = cusList;
    }

    public ArrayList<Product> getProductList() {
        return proList;
    }

    public void setProductList(ArrayList<Product> proList) {
        this.proList = proList;
    }

    public ArrayList<Transaction> getTransactionList() {
        return traList;
    }

    public void setTransactionList(ArrayList<Transaction> traList) {
        this.traList = traList;
    }

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

    public int saveCustomer() {
        File fileName = new File(this.fCus);
        String newLine;

        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Customer customer : this.cusList) {
                newLine = formatCustomer(customer);
                writer.write(newLine);
                writer.newLine();
                writer.flush();
            }

            writer.close();
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    public int saveProduct() {
        File fileName = new File(this.fPro);
        String newLine;

        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Product product : this.proList) {
                newLine = formatProduct(product);
                writer.write(newLine);
                writer.newLine();
                writer.flush();
            }

            writer.close();
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    public int saveTransaction() {
        File fileName = new File(this.fTra);
        String newLine;

        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Transaction transaction : this.traList) {
                newLine = formatTransaction(transaction);
                writer.write(newLine);
                writer.newLine();
                writer.flush();
            }

            writer.close();
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    public int loadCustomer() {
        File fileName = new File(this.fCus);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create file: " + this.fCus);
            }
            return 0;
        }

        cusList.clear();

        try ( BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

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
                        VipCustomer customer = new VipCustomer(id, name, email, phone, address, age, gender);
                        this.cusList.add(customer);
                    } else {
                        RegularCustomer customer = new RegularCustomer(id, name, email, phone, address, age, gender);
                        this.cusList.add(customer);
                    }

                }
            }
        } catch (IOException | NumberFormatException e) {
            return -1; // Lỗi đọc file hoặc lỗi ép kiểu dữ liệu
        }
        return 1; // Thành công
    }

    public int loadProduct() {
        File fileName = new File(this.fPro);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create file: " + this.fPro);
            }
            return 0;
        }

        this.proList.clear();

        try ( BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] tokens = line.split("\\|");
                if (tokens.length >= 6) {
                    String productId = tokens[1].trim();
                    String productName = tokens[2].trim();
                    String category = tokens[3].trim();
                    double price = Double.parseDouble(tokens[4].trim());
                    int stock = Integer.parseInt(tokens[5].trim());

                    Product product = new Product(productId, productName, category, price, stock);
                    this.proList.add(product);
                }
            }
        } catch (IOException | NumberFormatException e) {
            return -1;
        }
        return 1;
    }

    public int loadTransaction() {
        File fileName = new File(this.fTra);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create file: " + this.fTra);
            }
            return 0;
        }

        this.traList.clear();

        try ( BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] tokens = line.split("\\|");

                // Tối thiểu phải có 11 phần tử (từ index 0 đến 10) thì mới đủ thông tin nền tảng
                if (tokens.length >= 13) {
                    try {
                        // a. Đọc thông tin Transaction ID ở đầu dòng
                        String transId = tokens[1].trim();

                        // b. Đọc thông tin Customer (từ index 2 đến 7)
                        String cusId = tokens[2].trim();
                        String cusName = tokens[3].trim();
                        String cusPhone = tokens[4].trim();
                        String cusEmail = tokens[5].trim();
                        String cusAddress = tokens[6].trim();
                        int cusAge = Integer.parseInt(tokens[7].trim());
                        String cusGender = tokens[8].trim();
                        double discount = Double.parseDouble(tokens[9].trim());

                        // c. Đọc thông tin Transaction dạng chuỗi (từ index 8 đến 10)
                        String dateStr = tokens[10].trim();
                        double totalAmount = Double.parseDouble(tokens[11].trim());
                        String status = tokens[12].trim();

                        // 1. Phân loại và gom thông tin Customer trước
                        Customer customer = null;
                        if (discount > 0.0) {
                            customer = new VipCustomer(cusId, cusName, cusPhone, cusEmail, cusAddress, cusAge, cusGender);
                        } else {
                            customer = new RegularCustomer(cusId, cusName, cusPhone, cusEmail, cusAddress, cusAge, cusGender);
                        }

                        // 2. HOÀN THIỆN DANH SÁCH ITEMS TRƯỚC (Khởi tạo một HashMap rỗng để nạp sản phẩm)
                        HashMap<Product, Integer> items = new HashMap<>();

                        // Vòng lặp bóc tách và nạp sản phẩm vào HashMap 'items' vừa tạo
                        int i = 13;
                        while (i < tokens.length) {
                            String currentToken = tokens[i].trim();
                            if (currentToken.equals("#") || currentToken.isEmpty()) {
                                break;
                            }

                            int quantity = Integer.parseInt(currentToken);
                            String prodId = tokens[i + 1].trim();
                            String prodName = tokens[i + 2].trim();
                            String prodCat = tokens[i + 3].trim();
                            double prodPrice = Double.parseDouble(tokens[i + 4].trim());
                            int prodStock = Integer.parseInt(tokens[i + 5].trim());

                            Product product = new Product(prodId, prodName, prodCat, prodPrice, prodStock);

                            // Thêm trực tiếp vào bảng danh sách items cục bộ
                            items.put(product, quantity);

                            i += 6;
                        }

                        // 3. KHỞI TẠO TRANSACTON: Lúc này tất cả nguyên liệu bao gồm 'items' đã đầy đủ
                        // Đồng thời ép chuỗi ngày 'dateStr' sang kiểu 'LocalDate' bằng hàm parse()
                        java.time.LocalDate localDate = java.time.LocalDate.parse(dateStr);

                        Transaction transaction = new Transaction(transId, customer, localDate, totalAmount, status, items);

                        // 4. Lưu hóa đơn hoàn chỉnh vào danh sách hệ thống
                        this.traList.add(transaction);

                    } catch (Exception e) {
                        System.out.println("Lỗi xử lý định dạng dòng hóa đơn, bỏ qua dòng này.");
                    }
                }
            }
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }
}
