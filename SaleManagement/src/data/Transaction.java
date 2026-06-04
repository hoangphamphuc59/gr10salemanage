package data;

import java.util.Map;
import java.time.LocalDate;
import java.util.HashMap;

public class Transaction {

    private final String transactionId;
    private Customer customer;
    private LocalDate date;
    private double totalAmount;
    private String status;
    private HashMap<Product, Integer> items;

    public Transaction(String transactionId, Customer customer, LocalDate date, double totalAmount, String status, HashMap<Product, Integer> items) {

        this.transactionId = transactionId;
        this.customer = customer;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
        if (items != null) {
            this.items = items;
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public HashMap<Product, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<Product, Integer> items) {
        if (items != null) {
            this.items = items;
        }
    }

    public void updateProductQuantity(String productId, int quantity) {
        // Vì Key là Product (không phải chuỗi String), ta cần duyệt qua các Key để tìm đúng đối tượng
        Product targetProduct = null;
        for (Product p : items.keySet()) {
            if (p.getProductId().equals(productId)) {
                targetProduct = p;
                break;
            }
        }

        if (targetProduct != null) {
            if (quantity <= 0) {
                // Xóa sản phẩm khỏi giỏ hàng
                items.remove(targetProduct);
                System.out.println("Product removed from transaction.");
            } else {
                // Ghi đè số lượng (Value) mới cho Key (Product) hiện tại
                items.put(targetProduct, quantity);
                System.out.println("Product quantity updated to " + quantity);
            }
            calculateTotal(); // Tự động cập nhật lại tổng tiền
        } else {
            System.out.println("Product ID " + productId + " not found in this transaction.");
        }
    }

    public double calculateTotal() {
        this.totalAmount = 0.0;
        if (!isEmpty()) {
            // Duyệt qua từng cặp (Sản phẩm - Số lượng mua) để tính tiền
            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                Product product = entry.getKey();
                Integer quantity = entry.getValue();
                this.totalAmount += product.getPrice() * quantity;
            }

            // Cập nhật thêm: Nếu khách hàng có chiết khấu (discount) thì giảm giá
            if (this.customer != null && this.customer.getDiscount() > 0) {
                this.totalAmount = this.totalAmount * (1.0 - this.customer.getDiscount());
            }
        }
        return this.totalAmount;
    }

    public void confirmTransaction(Inventory inventory, Management manager) {
        // 1. Kiểm tra giỏ hàng rỗng
        if (isEmpty()) {
            System.out.println("Can not confirm an empty transaction.");
            return;
        }

        // 2. Kiểm tra trạng thái hóa đơn hiện tại
        if ("CANCELLED".equalsIgnoreCase(this.status) || "CONFIRMED".equalsIgnoreCase(this.status) || "FAILED".equalsIgnoreCase(this.status)) {
            System.out.println("This transaction is already confirmes with a status: [" + this.status + "].");
            return;
        }

        System.out.println("Checking inventory for transaction " + this.transactionId + ", please wait...");

        // 3. Giao phó việc kiểm tra và trừ kho cho Inventory 
        // Trả về 1 nếu thành công, -1 nếu thiếu hàng hoặc lỗi file
        int stockCheckResult = inventory.updateProductStock(this, manager);

        // 4. Quyết định số phận của hóa đơn dựa vào kết quả từ Inventory
        if (stockCheckResult == 1) {
            calculateTotal(); // Chốt lại tổng tiền lần cuối cùng cho an toàn
            this.status = "CONFIRMED";
            System.out.println("Confirmed: transaction " + this.transactionId + " has been confirm.");
        } else {
            // Nếu kho không đủ hoặc file lỗi, hóa đơn bị đánh dấu FAILED (Thất bại)
            this.status = "FAILED";
            System.out.println("Failed: transaction " + this.transactionId + " has been failed.");
        }
    }

    public void cancelTransaction() {
        if ("CONFIRMED".equalsIgnoreCase(this.status)) {
            System.out.println("Transaction is already confirmed. Cancellation may require a refund process.");
        }
        this.status = "CANCELLED";
        System.out.println("Transaction " + this.transactionId + " has been CANCELLED.");
    }

    public void displayTransaction() {
        System.out.println("=========================================");
        System.out.println("Transaction ID: " + this.transactionId);
        System.out.println("Date: " + this.date);
        System.out.println("Status: " + this.status);
        System.out.println("Customer: " + (this.customer != null ? this.customer.getName() : "Unknown"));
        System.out.println("Items:");

        if (isEmpty()) {
            System.out.println("  (No items in this transaction)");
        } else {
            // Dùng Lambda để bóc tách trực tiếp Product (Key) và Quantity (Value)
            items.forEach((product, quantity) -> {
                System.out.printf("  - %s (Qty: %d) - $%.2f each\n",
                        product.getProductName(), quantity, product.getPrice());
            });
        }

        System.out.printf("Total Amount: $%.2f\n", this.calculateTotal());
        System.out.println("=========================================");
    }

    public boolean isEmpty() {
        // Thêm kiểm tra null để code an toàn tuyệt đối, tránh lỗi NullPointerException
        return items == null || items.isEmpty();
    }
}
