package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CustomerManager {
    private Map<String, Customer> customers;

    public CustomerManager() {
        this.customers = new HashMap<String, Customer>();
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerID(), customer);
    }

    public Customer getCustomer(String customerID) {
        return customers.get(customerID);
    }

    public void updateCustomer(Customer customer) {
        customers.put(customer.getCustomerID(), customer);
    }

    public void deleteCustomer(String customerID) {
        customers.remove(customerID);
    }
}

