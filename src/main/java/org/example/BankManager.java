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

    public String getName() {
        return name;
    }

    public boolean authenticateManager(String managerId, String password) {
        return this.managerId.equals(managerId) && this.password.equals(password); // Assuming password field exists
    }

}
