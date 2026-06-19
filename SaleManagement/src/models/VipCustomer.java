package models;

public class VipCustomer extends Customer {

    public VipCustomer() {
        super();
    }

    public VipCustomer(String id, String name, String email,
               String phone, String address, int age, String gender) {
        super(id, name, email, phone, address, age, gender);
    }

    @Override
    public double getDiscount() {
        return 0.05;
    }
}
