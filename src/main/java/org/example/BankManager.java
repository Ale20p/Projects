package org.example;

import java.util.ArrayList;
import java.util.List;

public class BankManager {
    private String managerId;
    private String name;
    private CustomerManager customerManager;
    private List<Transaction> pendingTransactions; // List to hold pending transactions for review

    public BankManager(String managerId, String name, CustomerManager customerManager) {
        this.managerId = managerId;
        this.name = name;
        this.customerManager = customerManager;
    }

    public BankManager(String managerId, String name) {
        this.managerId = managerId;
        this.name = name;
        this.pendingTransactions = new ArrayList<>();
    }

    public void approveLargeWithdrawal(String customerId, double amount) {
        Customer customer = customerManager.getCustomer(customerId);
        if (customer != null && canApproveWithdrawal(customer, amount)) {
            System.out.println("Withdrawal approved for customer " + customerId);
            // Additional logic to process the approval
        } else {
            System.out.println("Withdrawal denied for customer " + customerId);
        }
    }

    private boolean canApproveWithdrawal(Customer customer, double amount) {
        // Define logic to determine if the withdrawal can be approved
        return true; // Simplified
    }

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

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    // Method to add a transaction to the pending list
    public void addPendingTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    // Method to retrieve the list of pending transactions
    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }
}

