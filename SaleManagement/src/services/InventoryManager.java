package services;

import models.Product;
import models.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    public InventoryManager() {
    }

    /**
     * Checks if all products in the transaction have sufficient stock.
     * Returns true if stock is sufficient for all items, false otherwise.
     * Does NOT modify stock - that is done separately after confirmation.
     */
    public boolean checkStock(Transaction transaction, ArrayList<Product> productList) {
        HashMap<Product, Integer> cartItems = transaction.getItems();

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            String buyProductId = entry.getKey().getProductId();
            int buyQuantity = entry.getValue();

            Product inventoryProduct = findProductById(productList, buyProductId);

            if (inventoryProduct == null) {
                System.out.println("Error: Product ID [" + buyProductId + "] not found in inventory.");
                return false;
            }

            if (inventoryProduct.getStock() < buyQuantity) {
                System.out.println("Error: Product [" + inventoryProduct.getProductName() +
                                   "] only has " + inventoryProduct.getStock() +
                                   " in stock. Cannot sell " + buyQuantity + ".");
                return false;
            }
        }
        return true;
    }

    /**
     * Deducts stock for all products in the transaction.
     * Should only be called AFTER checkStock returns true.
     */
    public void deductStock(Transaction transaction, ArrayList<Product> productList) {
        HashMap<Product, Integer> cartItems = transaction.getItems();

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            String buyProductId = entry.getKey().getProductId();
            int buyQuantity = entry.getValue();

            Product inventoryProduct = findProductById(productList, buyProductId);
            int newStock = inventoryProduct.getStock() - buyQuantity;
            inventoryProduct.setStock(newStock);
        }
    }

    private Product findProductById(ArrayList<Product> productList, String productId) {
        for (Product p : productList) {
            if (p.getProductId().equals(productId)) {
                return p;
            }
        }
        return null;
    }
}
