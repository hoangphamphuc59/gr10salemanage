package services.Validation;

import services.ProductManager;
import static services.Validation.StringUtils.*;


public class ProductValidation implements Validator<String> {

    private final ProductManager productManager;

    
    public ProductValidation(ProductManager productManager) {
        this.productManager = productManager;
    }

   
    @Override
    public String validate(String fieldName, String rawValue) throws IllegalArgumentException {
        if (rawValue == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }

        switch (fieldName.toLowerCase()) {
            case "id":
                
                String cleanId = skipSpace(rawValue).toUpperCase(); 
                
                if (!cleanId.startsWith("PRO")) {
                    throw new IllegalArgumentException("Product ID must start with 'PRO'!");
                }
                
                String numberPart = cleanId.substring(3);
                if (numberPart.isEmpty() || !isDigitString(numberPart)) {
                    throw new IllegalArgumentException("Product ID must be followed by digits (e.g., PRO123)!");
                }
                
                return cleanId;

            case "name":
                
                String name = myTrim(rawValue);
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Product name cannot be empty");
                }
                return name;

            case "category":
                String category = myTrim(rawValue);
                if (category.isEmpty()) {
                    throw new IllegalArgumentException("Category cannot be empty");
                }
                return category;

            case "price":
                String priceStr = skipSpace(rawValue);
                if (priceStr.isEmpty()) {
                    throw new IllegalArgumentException("Price cannot be empty");
                }
                try {
                    double price = Double.parseDouble(priceStr);
                    if (price <= 0) {
                        throw new IllegalArgumentException("Price must be greater than 0");
                    }
                    
                    return priceStr;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Price must be a valid number");
                }

            case "stock":
                String stockStr = skipSpace(rawValue);
                if (stockStr.isEmpty()) {
                    throw new IllegalArgumentException("Stock cannot be empty");
                }
                try {
                    int stock = Integer.parseInt(stockStr);
                    if (stock < 0) {
                        throw new IllegalArgumentException("Stock must be 0 or positive");
                    }
                    
                    return stockStr;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Stock must be a valid integer");
                }

            default:
                
                return rawValue;
        }
    }

    
    public void ensureProductExists(String productId) throws IllegalArgumentException {
        if (productManager.findById(productId) == null) {
            throw new IllegalArgumentException("Product with ID '" + productId + "' does not exist.");
        }
    }

    
}