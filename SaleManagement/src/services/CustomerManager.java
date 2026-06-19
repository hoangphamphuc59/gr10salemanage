package services;

import models.Customer;

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

    public boolean addCustomer(Customer customer) {
        if (customer == null) return false;
        
        
        if (customer.getId() == null || customer.getId().isEmpty()) return false;
        if (customer.getName() == null || customer.getName().isEmpty()) return false;
        
        // Kiểm tra trùng ID
        if (findById(customer.getId()) != null) return false;

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
