package services;

import models.Product;
import java.util.ArrayList;

public class ProductManager {

    private ArrayList<Product> productList;

    public ProductManager() {
        this.productList = new ArrayList<>();
    }



    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public Product findById(String id) {
        for (Product p : productList) {
            if (p.getProductId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByNameOrCategory(String keyword) {
        ArrayList<Product> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Product p : productList) {
            if (p.getProductName().toLowerCase().contains(lowerKeyword) ||
                p.getCategory().toLowerCase().contains(lowerKeyword)) {
                results.add(p);
            }
        }
        return results;
    }

    public boolean addProduct(String id, String name, String category, double price, int stock) {
        if (id == null || id.trim().isEmpty()) return false;
        if (name == null || name.trim().isEmpty()) return false;
        if (category == null || category.trim().isEmpty()) return false;
        if (price <= 0) return false;
        if (stock < 0) return false;
        if (findById(id) != null) return false;

        productList.add(new Product(id, name, category, price, stock));
        return true;
    }

    public boolean updateProduct(String id, String newName, String newCategory, double newPrice, int newStock) {
        Product p = findById(id);
        if (p == null) return false;
        if (newName == null || newName.trim().isEmpty()) return false;
        if (newCategory == null || newCategory.trim().isEmpty()) return false;
        if (newPrice <= 0) return false;
        if (newStock < 0) return false;

        p.setProductName(newName);
        p.setCategory(newCategory);
        p.setPrice(newPrice);
        p.setStock(newStock);
        return true;
    }

    public boolean removeProduct(String id) {
        Product p = findById(id);
        if (p == null) return false;
        productList.remove(p);
        return true;
    }
}
