package services;

import models.Customer;
import models.VipCustomer;
import models.RegularCustomer;
import java.util.ArrayList;

public class CustomerManager {

    private ArrayList<Customer> customerList;

    public CustomerManager() {
        this.customerList = new ArrayList<>();
    }

    public ArrayList<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(ArrayList<Customer> customerList) {
        this.customerList = customerList;
    }

    public Customer findById(String id) {
        for (Customer c : customerList) {
            if (c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    public boolean addCustomer(String id, String name, String phone, String email, String address, int age, String gender, boolean isVip) {
        if (id == null || id.trim().isEmpty()) return false;
        if (name == null || name.trim().isEmpty()) return false;
        if (findById(id) != null) return false;

        Customer customer;
        if (isVip) {
            customer = new VipCustomer(id, name, phone, email, address, age, gender);
        } else {
            customer = new RegularCustomer(id, name, phone, email, address, age, gender);
        }
        customerList.add(customer);
        return true;
    }

    public boolean updateCustomer(String id, String newName, String newPhone, String newEmail, String newAddress, int newAge, String newGender) {
        Customer c = findById(id);
        if (c == null) return false;
        if (newName == null || newName.trim().isEmpty()) return false;

        c.setName(newName);
        c.setPhone(newPhone);
        c.setEmail(newEmail);
        c.setAddress(newAddress);
        c.setAge(newAge);
        c.setGender(newGender);
        return true;
    }

    public boolean removeCustomer(String id) {
        Customer c = findById(id);
        if (c == null) return false;
        customerList.remove(c);
        return true;
    }
}
