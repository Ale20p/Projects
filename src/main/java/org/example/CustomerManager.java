package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CustomerManager {
    private Map<String, Customer> customers = new HashMap<>();

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerID(), customer);
    }

    public Customer getCustomer(String customerID) {
        return customers.get(customerID);
    }

    public void updateCustomerInfo(String customerID, String name) {
        if (customers.containsKey(customerID)) {
            Customer customer = customers.get(customerID);
            customer.setName(name);
        }
    }

    public void deleteCustomer(String customerID) {
        customers.remove(customerID);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
}
