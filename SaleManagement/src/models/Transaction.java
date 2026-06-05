package models;

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

    public double calculateTotal() {
        this.totalAmount = 0.0;
        if (!isEmpty()) {
            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                Product product = entry.getKey();
                Integer quantity = entry.getValue();
                this.totalAmount += product.getPrice() * quantity;
            }
            if (this.customer != null && this.customer.getDiscount() > 0) {
                this.totalAmount = this.totalAmount * (1.0 - this.customer.getDiscount());
            }
        }
        return this.totalAmount;
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
            items.forEach((product, quantity) -> {
                System.out.printf("  - %s (Qty: %d) - $%.2f each\n",
                        product.getProductName(), quantity, product.getPrice());
            });
        }

        System.out.printf("Total Amount: $%.2f\n", this.calculateTotal());
        System.out.println("=========================================");
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
