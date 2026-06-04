package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    public Inventory() {
    }

    public int updateProductStock(Transaction transaction, Management manager) {
        
        // 1. Lấy dữ liệu mới nhất từ file (đảm bảo số lượng kho là realtime)
        if (manager.loadProduct() == -1) {
            System.out.println("Lỗi: Không thể tải dữ liệu sản phẩm từ file.");
            return -1;
        }
        
        // 2. Lấy danh sách sản phẩm
        ArrayList<Product> currentProducts = manager.getProductList();
        HashMap<Product, Integer> cartItems = transaction.getItems();

        // BƯỚC 1: KIỂM TRA TRƯỚC (Two-pass check)
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            String buyProductId = entry.getKey().getProductId();
            int buyQuantity = entry.getValue();

            // Tìm sản phẩm trong list vừa load lên
            Product inventoryProduct = findProductById(currentProducts, buyProductId);

            if (inventoryProduct == null) {
                System.out.println("Lỗi: Không tìm thấy mã sản phẩm [" + buyProductId + "] trong kho.");
                return -1;
            }
            
            if (inventoryProduct.getStock() < buyQuantity) {
                System.out.println("Lỗi: Sản phẩm [" + inventoryProduct.getProductName() + 
                                   "] chỉ còn " + inventoryProduct.getStock() + 
                                   " cái. Không đủ để bán " + buyQuantity + " cái.");
                return -1; // Từ chối toàn bộ hóa đơn, kho chưa bị trừ gì cả
            }
        }

        // BƯỚC 2: TRỪ KHO (Khi chắc chắn 100% tất cả món hàng đều đủ số lượng)
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            String buyProductId = entry.getKey().getProductId();
            int buyQuantity = entry.getValue();

            Product inventoryProduct = findProductById(currentProducts, buyProductId);
            
            // Cập nhật số lượng mới vào đối tượng nằm trong currentProducts
            int newStock = inventoryProduct.getStock() - buyQuantity;
            inventoryProduct.setStock(newStock);
        }

        // 3. Đóng gói và lưu lại xuống file
        manager.setProductList(currentProducts);
        int saveStatus = manager.saveProduct();
        
        if (saveStatus == 1) {
            System.out.println("Cập nhật kho thành công.");
            return 1;
        } else {
            System.out.println("Lỗi: Cập nhật kho thất bại trong quá trình ghi file.");
            return -1;
        }
    }

    // Hàm hỗ trợ tìm kiếm nội bộ
    private Product findProductById(ArrayList<Product> productList, String productId) {
        for (Product p : productList) {
            if (p.getProductId().equals(productId)) {
                return p;
            }
        }
        return null; // Không tìm thấy
    }
}
