package org.example;

public class BankManager {
    private String managerId;
    private String password;
    private CustomerManager customerManager;

    public BankManager(String managerId, String password, CustomerManager customerManager) {
        this.managerId = managerId;
        this.password = password;
        this.customerManager = customerManager;
    }

    public String getManagerId() {
        return managerId;
    }

    public boolean authenticateManager(String password) {
        return this.password.equals(password);
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public String getName() {
        return managerId; // Assuming managerId is used as the name here. Adjust if needed.
    }
}
