package org.example;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public interface UI {
    void displayDashboard();
}


class CustomerUI implements UI {
    private Customer customer;
    private Scanner scanner;
    private Account selectedAccount;  // To hold the selected account

    public CustomerUI(Customer customer, Scanner scanner) {
        this.customer = customer;
        this.scanner = scanner;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Welcome, " + customer.getName());
        if (selectAccount()) {  // Select account at the beginning of the session
            int action;
            do {
                System.out.println("Selected Account Number: " + selectedAccount.getAccountNumber() + ", Balance: $" + selectedAccount.getBalance());
                System.out.println("Available Actions:");
                System.out.println("1. Deposit Funds");
                System.out.println("2. Withdraw Funds");
                System.out.println("3. Transfer Funds");
                System.out.println("4. View Transactions");
                System.out.println("5. Apply for a Loan");
                System.out.println("0. Return to Main Menu");
                System.out.println("Please select an action:");

                action = scanner.nextInt();
                scanner.nextLine(); // Clear buffer after numeric input

                switch (action) {
                    case 1:
                        performDeposit();
                        break;
                    case 2:
                        performWithdrawal();
                        break;
                    case 3:
                        performTransfer();
                        break;
                    case 4:
                        // Implement view transactions functionality
                        break;
                    case 5:
                        // Implement loan application functionality
                        break;
                    case 0:
                        break; // Return to main menu
                    default:
                        System.out.println("Invalid action. Please try again.");
                        break;
                }
            } while (action != 0);
        } else {
            System.out.println("No valid account selected or available.");
        }
    }

    private void performDeposit() {
        System.out.println("Enter the amount to deposit:");
        double amount = scanner.nextDouble();
        selectedAccount.deposit(amount);
    }

    private void performWithdrawal() {
        System.out.println("Enter the amount to withdraw:");
        double amount = scanner.nextDouble();
        try {
            selectedAccount.withdraw(amount);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }

    private void performTransfer() {
        System.out.println("Select the destination account by number:");
        Account destinationAccount = selectAccount(true);
        if (destinationAccount == null || destinationAccount == selectedAccount) {
            System.out.println("Invalid selection for destination account.");
            return;
        }

        System.out.println("Enter the amount to transfer:");
        double amount = scanner.nextDouble();
        try {
            if (selectedAccount.getBalance() >= amount) {
                selectedAccount.withdraw(amount);
                destinationAccount.deposit(amount);
                System.out.println("Transferred $" + amount + " from account " + selectedAccount.getAccountNumber() + " to " + destinationAccount.getAccountNumber());
            } else {
                System.out.println("Insufficient funds for this transfer.");
            }
        } catch (InsufficientFundsException e) {
            System.out.println("Failed to transfer: " + e.getMessage());
        }
    }

    private boolean selectAccount() {
        List<Account> accounts = customer.getAccountsList();
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return false;
        }
        if (accounts.size() == 1) {
            selectedAccount = accounts.get(0);
            return true;
        }

        System.out.println("Select an account by number:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + ", Balance: $" + accounts.get(i).getBalance());
        }

        int accountIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Clear buffer
        if (accountIndex >= 0 && accountIndex < accounts.size()) {
            selectedAccount = accounts.get(accountIndex);
            return true;
        } else {
            System.out.println("Invalid account selection.");
            return false;
        }
    }

    private Account selectAccount(boolean forTransfer) {
        List<Account> accounts = customer.getAccountsList();
        if (forTransfer && accounts.size() < 2) {
            System.out.println("Not enough accounts available for transfer.");
            return null;
        }

        for (int i = 0; i < accounts.size(); i++) {
            if (!accounts.get(i).equals(selectedAccount) || !forTransfer) {  // Show all accounts if not for transfer
                System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + ", Balance: $" + accounts.get(i).getBalance());
            }
        }

        int accountIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Clear buffer
        if (accountIndex >= 0 && accountIndex < accounts.size()) {
            return accounts.get(accountIndex);
        } else {
            System.out.println("Invalid account selection.");
            return null;
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


