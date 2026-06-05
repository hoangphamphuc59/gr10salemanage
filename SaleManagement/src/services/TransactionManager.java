package services;

import models.Customer;
import models.Product;
import models.Transaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransactionManager {

    private ArrayList<Transaction> transactionList;
    private InventoryManager inventoryManager;

    public TransactionManager() {
        this.transactionList = new ArrayList<>();
        this.inventoryManager = new InventoryManager();
    }

    public ArrayList<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public Transaction findById(String id) {
        for (Transaction t : transactionList) {
            if (t.getTransactionId().equalsIgnoreCase(id)) {
                return t;
            }
        }
        return null;
    }

    public String generateTransactionId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Transaction createTransaction(String transId, Customer customer, HashMap<Product, Integer> cart) {
        if (customer == null || cart == null || cart.isEmpty()) return null;

        Transaction transaction = new Transaction(transId, customer, LocalDate.now(), 0.0, "PENDING", cart);
        transaction.calculateTotal();
        return transaction;
    }

    /**
     * Confirms a transaction by checking inventory stock.
     * Returns true if confirmed, false if failed.
     */
    public boolean confirmTransaction(Transaction transaction, ArrayList<Product> productList) {
        if (transaction == null || transaction.isEmpty()) return false;

        String status = transaction.getStatus();
        if ("CANCELLED".equalsIgnoreCase(status) || "CONFIRMED".equalsIgnoreCase(status) || "FAILED".equalsIgnoreCase(status)) {
            return false;
        }

        // Delegate stock check to InventoryManager
        boolean stockOk = inventoryManager.checkStock(transaction, productList);

        if (stockOk) {
            inventoryManager.deductStock(transaction, productList);
            transaction.calculateTotal();
            transaction.setStatus("CONFIRMED");
            transactionList.add(transaction);
            return true;
        } else {
            transaction.setStatus("FAILED");
            return false;
        }
    }

    public void cancelTransaction(Transaction transaction) {
        if (transaction != null) {
            transaction.setStatus("CANCELLED");
        }
    }

    public boolean updateProductQuantity(Transaction transaction, String productId, int newQty) {
        if (transaction == null) return false;

        String status = transaction.getStatus();
        if ("CONFIRMED".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)) {
            return false;
        }

        HashMap<Product, Integer> items = transaction.getItems();
        Product targetProduct = null;
        for (Product p : items.keySet()) {
            if (p.getProductId().equals(productId)) {
                targetProduct = p;
                break;
            }
        }

        if (targetProduct == null) return false;

        if (newQty <= 0) {
            items.remove(targetProduct);
        } else {
            items.put(targetProduct, newQty);
        }
        transaction.calculateTotal();
        return true;
    }
}
