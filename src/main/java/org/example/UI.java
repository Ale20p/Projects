package org.example;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public interface UI {
    void displayDashboard();
}

class CustomerUI implements UI {
    private Customer customer;
    private CustomerManager customerManager;
    private TransactionManager transactionManager;
    private Scanner scanner;

    public CustomerUI(Customer customer, CustomerManager customerManager, TransactionManager transactionManager, Scanner scanner) {
        this.customer = customer;
        this.customerManager = customerManager;
        this.transactionManager = transactionManager;
        this.scanner = scanner;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Welcome, " + customer.getName());
        System.out.println("Account Balances: ");
        for (Account account : customer.getAccountsList()) {
            System.out.println("Account Number: " + account.getAccountNumber() + ", Balance: $" + account.getBalance());
        }
        System.out.println("Available Actions:");
        System.out.println("1. Deposit Funds");
        System.out.println("2. Withdraw Funds");
        System.out.println("3. Transfer Funds");
        System.out.println("4. View Transactions");
        System.out.println("5. Apply for a Loan");
        System.out.println("0. Return to Main Menu");
        System.out.println("Please select an action by entering the corresponding number:");

        int action = scanner.nextInt();
        switch (action) {
            case 1:
                System.out.println("Enter the amount to deposit:");
                double depositAmount = scanner.nextDouble();
                customer.getAccountsList().get(0).deposit(depositAmount);
                break;
            case 2:
                System.out.println("Enter the amount to withdraw:");
                double withdrawAmount = scanner.nextDouble();
                try {
                    customer.getAccountsList().get(0).withdraw(withdrawAmount);
                } catch (InsufficientFundsException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 0:
                return;  // Return to main menu
            default:
                System.out.println("Invalid action. Please try again.");
                break;
        }
    }
}


class ManagerUI implements UI {
    private BankManager bankManager;
    private TransactionManager transactionManager;
    private Scanner scanner;

    public ManagerUI(BankManager bankManager, TransactionManager transactionManager, Scanner scanner) {
        this.bankManager = bankManager;
        this.transactionManager = transactionManager;
        this.scanner = scanner;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Manager Dashboard: " + bankManager.getName());
        System.out.println("Pending Transactions for Review:");
        List<Transaction> pendingTransactions = transactionManager.getPendingTransactions();
        for (Transaction transaction : pendingTransactions) {
            System.out.println("Transaction ID: " + transaction.getTransactionId() + ", Amount: $" + transaction.getAmount());
        }
        System.out.println("Available Actions:");
        System.out.println("1. Approve All Transactions");
        System.out.println("0. Return to Main Menu");
        System.out.println("Please select an action:");

        int action = scanner.nextInt();
        if (action == 1) {
            for (Transaction transaction : pendingTransactions) {
                transaction.setApproved(true);
                System.out.println("Transaction " + transaction.getTransactionId() + " approved.");
            }
        } else if (action == 0) {
            return;  // Return to main menu
        } else {
            System.out.println("Invalid action. Please try again.");
        }
    }
}


