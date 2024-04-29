package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CustomerManager {
    private Map<String, Customer> customers;

    public CustomerManager() {
        this.customers = new HashMap<>();
    }

    // Add a new customer to the system
    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerID(), customer);
        System.out.println("Customer added: " + customer.getName());
    }

    // Retrieve a customer by their ID
    public Customer getCustomer(String customerID) {
        return customers.get(customerID);
    }

    // Update customer information
    public boolean updateCustomerInfo(String customerID, String newName) {
        Customer customer = customers.get(customerID);
        if (customer != null) {
            customer.setName(newName);
            System.out.println("Customer info updated for " + customerID + ": new name " + newName);
            return true;
        } else {
            System.out.println("Customer not found for ID: " + customerID);
            return false;
        }
    }

    // Delete a customer from the system
    public void deleteCustomer(String customerID) {
        if (customers.containsKey(customerID)) {
            customers.remove(customerID);
            System.out.println("Customer deleted: " + customerID);
        } else {
            System.out.println("Customer not found: " + customerID);
        }
    }

    // List all customers
    public List<Customer> listAllCustomers() {
        return new ArrayList<>(customers.values());
    }
}
