package org.example;

import java.io.IOException;
import java.util.*;

public class CustomerManager {
    private Map<String, Customer> customers = new HashMap<>();
    private static final String CUSTOMER_FILE = "customers.csv";

    public CustomerManager() {
        try {
            loadCustomers();
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }

    private void loadCustomers() throws IOException {
        List<String[]> data = CSVUtility.readCSV(CUSTOMER_FILE);
        for (String[] line : data) {
            if (line.length >= 3) {  // Ensure there are enough elements in the line
                Customer customer = new Customer(line[0], line[1], line[2]); // Assuming format: customerID, name, password
                customers.put(customer.getCustomerID(), customer);
            }
        }
    }

    public void saveCustomers() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Customer customer : customers.values()) {
            data.add(new String[]{customer.getCustomerID(), customer.getName(), customer.getPassword()});
        }
        CSVUtility.writeCSV(CUSTOMER_FILE, data, false); // Overwrite the existing file
    }

    public void addCustomer(Customer customer) {
        if (!customers.containsKey(customer.getCustomerID())) {
            customers.put(customer.getCustomerID(), customer);
            try {
                saveCustomers();
            } catch (IOException e) {
                System.err.println("Error saving customers: " + e.getMessage());
            }
        }
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public boolean deleteCustomer(String customerId) {
        if (customers.containsKey(customerId)) {
            customers.remove(customerId);
            try {
                saveCustomers();
            } catch (IOException e) {
                System.err.println("Error saving customers: " + e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean updateCustomer(String customerId, String newName, String newPassword) {
        if (customers.containsKey(customerId)) {
            Customer customer = customers.get(customerId);
            customer.setName(newName);
            customer.setPassword(newPassword);
            try {
                saveCustomers();
            } catch (IOException e) {
                System.err.println("Error updating customers: " + e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }
}
