package org.example;

import java.util.stream.Collectors;

public interface UI {
    void displayDashboard();
}

class CustomerUI implements UI {
    private Customer customer;

    public CustomerUI(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Welcome, " + customer.getName());
        System.out.println("Account Balance: " + customer.getAccountsList().stream()
                .map(Account::checkBalance)
                .collect(Collectors.toList()));
        System.out.println("Available actions:");
        System.out.println("1. View Transactions");
        System.out.println("2. Deposit Funds");
        System.out.println("3. Withdraw Funds");
        System.out.println("4. Apply for Loan");
        // Additional functionalities can be added here
    }
}

class ManagerUI implements UI {
    private BankManager manager;

    public ManagerUI(BankManager manager) {
        this.manager = manager;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Manager Portal: " + manager.getName());
        System.out.println("Pending Transactions: ");
        manager.getPendingTransactions().forEach(transaction -> {
            System.out.println("Transaction ID: " + transaction.getTransactionId() + " Amount: " + transaction.getAmount());
        });
        System.out.println("Actions:");
        System.out.println("1. Approve Transaction");
        System.out.println("2. Reject Transaction");
        System.out.println("3. Generate Report");
        // Additional managerial functionalities can be added here
    }
}