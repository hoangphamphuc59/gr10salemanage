package data;

public class RegularCustomer extends Customer {

    // Constructor không tham số
    public RegularCustomer() {
        super();
    }

    // Constructor 5 tham số gọi đúng chuẩn theo thứ tự lớp cha
    public RegularCustomer(String id, String name, String phone, String email, String address) {
        // Lớp cha nhận email trước phone: (id, name, email, phone, address)
        super(id, name, email, phone, address);
    }
}
