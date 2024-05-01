package org.example;

import java.util.ArrayList;
import java.util.List;

public class BankManager {
    private String managerId;
    private String password;  // Add password field
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

    // Method to review and approve large transactions
    public void reviewLargeTransactions() {
        List<Transaction> pendingTransactions = transactionManager.getPendingTransactions();
        for (Transaction transaction : pendingTransactions) {
            if (transaction.getAmount() > 10000) {  // Assuming $10,000 is the threshold for large transactions
//                transaction.setApproved(true);
                System.out.println("Transaction approved: " + transaction.getTransactionId());
            }
        }
    }

    // Method to process all transactions
    public void processAllTransactions() {
//        transactionManager.processTransactions();
    }

    // Methods to manage customer profiles
    public void addCustomerProfile(Customer customer) {
        customerManager.addCustomer(customer);
        System.out.println("New customer profile added: " + customer.getName());
    }

    public void updateCustomerInfo(String customerId, String newName) {
        if (customerManager.updateCustomerInfo(customerId, newName)) {
            System.out.println("Customer information updated: " + newName);
        } else {
            System.out.println("Customer update failed for ID: " + customerId);
        }
    }

    // Getters and Setters
    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean authenticateManager(String managerId, String password) {
        return this.managerId.equals(managerId) && this.password.equals(password); // Assuming password field exists
    }

}
