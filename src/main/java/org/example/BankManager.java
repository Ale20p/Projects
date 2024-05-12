package org.example;

public class BankManager {
    private String managerId;
    private String password;
    private String name;
    private CustomerManager customerManager;
    private TransactionManager transactionManager;

    public BankManager(String managerId, String password, String name, CustomerManager customerManager, TransactionManager transactionManager) {
        this.managerId = managerId;
        this.password = password;
        this.name = name;
        this.customerManager = customerManager;
        this.transactionManager = transactionManager;
    }

    // Authenticates the manager with given credentials
    public boolean authenticateManager(String managerId, String password) {
        return this.managerId.equals(managerId) && this.password.equals(password);
    }

    // Getter for CustomerManager
    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    // Getter for TransactionManager
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    // Getter for the manager's name
    public String getName() {
        return name;
    }

    // Additional methods can be added to support other functionalities as needed
}
