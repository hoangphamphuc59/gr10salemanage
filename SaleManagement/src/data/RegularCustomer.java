package data;

public class RegularCustomer extends Customer {

    public RegularCustomer() {
        super();
    }

    public RegularCustomer(String id, String name, String email, 
               String phone, String address, int age, String gender) {
        super(id, name, email, phone, address, age, gender);
    }
}
