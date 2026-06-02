package data;

import java.time.LocalDate;
import java.util.HashMap;

public class Transaction {

    private final String transactionId;
    private Customer customer;
    private LocalDate date;
    private double totalAmount;
    private String status;
    private HashMap<String, Product> items;

    public Transaction(String transactionId, Customer customer, LocalDate date, double totalAmount, String status, HashMap<String, Product> items) {

        this.transactionId = transactionId;
        this.customer = customer;
        this.date = date;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
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

    public HashMap<String, Product> getItems() {
        return items;
    }

    public void setItems(HashMap<String, Product> items) {
        this.items = items;
    }

    public void addProduct(String productId, int quantity) {

    }

    public void removeProduct(String productId) {

    }

    public void updateProductQuantity(String productId, int quantity) {
        //ProductQuantity > 0
    }

    public double calculateTotal() {
        return 0.0;
    }

    public void confirmTransaction() {

    }

    public void cancelTransaction() {

    }

    public void displayTransaction() {

    }

    public boolean isEmpty() {
        return true;
    }

    public void saveTransactionFile() {

    }

    public void loadTransactionFile() {

    }
}
