package data;

public class VipCustomer extends Customer {

    public VipCustomer() {
        super();
    }

    public VipCustomer(String id, String name, String phone, 
            String email, String address) {
        super(id, name, email, phone, address);
    }

    @Override
    public double getDiscount() {
        return 0.05;
    }
}
