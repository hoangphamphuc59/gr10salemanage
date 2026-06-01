package data;

import java.util.List;
import java.util.ArrayList;
public class Customer {

    private List<Customer> customerList = new ArrayList<>();
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private int age;
    private String gender;
    protected double discount = 0.00;

    public Customer() {

    }

    public Customer(String id, String name, String email,
            String phone, String address, int age, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDiscount() {
        return discount;
    }

    public Customer findCustomerById(String id) {
        for (Customer c : customerList) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void addCustomer(Customer c) {
        if (c == null) {
            System.out.println("Error: Cannot add an empty customer.");
            return;
        }
        if (findCustomerById(c.getId()) != null) {
            System.out.println("Error: Customer with ID " + c.getId() + " already exists.");
        } else {
            customerList.add(c);
            System.out.println("Success: Customer '" + c.getName() + "' added successfully.");
        }
    }
    
    public void removeCustomer(String id) {
        Customer customerToRemove = findCustomerById(id);
        if (customerToRemove != null) {
            customerList.remove(customerToRemove);
            System.out.println("Success: Customer with ID " + id + " has been removed.");
        } else {
            System.out.println("Error: Customer with ID " + id + " not found. Cannot remove.");
        }
    }
}
