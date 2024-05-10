package org.example;

import java.util.List;
import java.util.Scanner;

public interface UI {
    void displayDashboard();
}

class CustomerUI implements UI {
    private Customer customer;
    private Scanner scanner;
    private Account selectedAccount;  // To hold the selected account for session-based operations

    public CustomerUI(Customer customer, Scanner scanner) {
        this.customer = customer;
        this.scanner = scanner;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Welcome, " + customer.getName());
        if (customer.getAccountsList().isEmpty()) {
            System.out.println("No accounts available. Please contact your bank manager.");
            return;
        }

        // Allow the customer to select the account at the start for other operations
        selectedAccount = selectAccount();
        if (selectedAccount == null) {
            System.out.println("No valid account selected or available. Exiting dashboard.");
            return;
        }

        int action;
        do {
            System.out.println("Selected Account Number: " + selectedAccount.getAccountNumber() + " - Balance: $" + String.format("%.2f", selectedAccount.getBalance()));
            System.out.println("Available Actions:");
            System.out.println("1. Deposit Funds");
            System.out.println("2. Withdraw Funds");
            System.out.println("3. Transfer Funds");
            System.out.println("4. View Transactions");
            System.out.println("5. Apply for a Loan");
            System.out.println("6. View My Loans");
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
                    viewTransactions();
                    break;
                case 5:
                    applyForLoan();
                    break;
                case 6:
                    viewLoans();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (action != 0);
    }

    private Account selectAccount() {
        List<Account> accounts = customer.getAccountsList();
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return null;
        }

        System.out.println("Select an account by number:");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". Account Number: " + account.getAccountNumber() + " - Balance: $" + String.format("%.2f", account.getBalance()));
        }

        int accountIndex = scanner.nextInt() - 1;
        scanner.nextLine();  // Clear the newline
        if (accountIndex >= 0 && accountIndex < accounts.size()) {
            return accounts.get(accountIndex);
        } else {
            System.out.println("Invalid account selection.");
            return null;
        }
    }

    private void performDeposit() {
        System.out.println("Enter the amount to deposit:");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Clear the newline
        selectedAccount.deposit(amount);
    }

    private void performWithdrawal() {
        System.out.println("Enter the amount to withdraw:");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Clear the newline
        try {
            selectedAccount.withdraw(amount);
        } catch (InsufficientFundsException e) {
            System.out.println("Failed to withdraw: " + e.getMessage());
        }
    }

    private void performTransfer() {
        System.out.println("Select the destination account for transfer:");
        Account destinationAccount = selectAccount();
        if (destinationAccount == null || destinationAccount == selectedAccount) {
            System.out.println("Invalid destination account selection or same as source account.");
            return;
        }

        System.out.println("Enter the amount to transfer:");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Clear the newline

        if (selectedAccount.getBalance() >= amount) {
            try {
                selectedAccount.withdraw(amount);
                destinationAccount.deposit(amount);
            } catch (InsufficientFundsException e) {
                System.out.println("Failed to transfer: " + e.getMessage());
                // Optionally, rollback the withdrawal if necessary, though this would be handled in the Account class
            }
        } else {
            System.out.println("Insufficient funds for this transfer.");
        }
    }

    private void viewTransactions() {
        List<Transaction> transactions = selectedAccount.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
        } else {
            System.out.println("Transaction history for Account " + selectedAccount.getAccountNumber() + ":");
            for (Transaction transaction : transactions) {
                System.out.printf("Type: %s, Amount: $%.2f, Approved: %s\n", transaction.getType(), transaction.getAmount(), transaction.isApproved() ? "Yes" : "No");
            }
        }
    }

    private void applyForLoan() {
        System.out.println("Enter the loan amount:");
        double amount = scanner.nextDouble();
        System.out.println("Enter the interest rate (e.g., 5.5 for 5.5%):");
        double interestRate = scanner.nextDouble();
        scanner.nextLine(); // Clear the newline
        Loan loan = new Loan(amount, interestRate);
        customer.addLoan(loan);
        System.out.println("Loan application submitted for $" + String.format("%.2f", amount) + " at " + interestRate + "% interest.");
    }

    private void viewLoans() {
        List<Loan> loans = customer.getLoans();
        if (loans.isEmpty()) {
            System.out.println("You have no loans.");
        } else {
            System.out.println("Your loans:");
            for (Loan loan : loans) {
                System.out.printf("Amount: $%.2f, Interest Rate: %.2f%%, Approved: %s\n", loan.getLoanAmount(), loan.getInterestRate(), loan.isApproved() ? "Yes" : "No");
            }
        }
    }
}

class ManagerUI implements UI {
    private BankManager bankManager;
    private TransactionManager transactionManager;
    private CustomerManager customerManager;
    private Scanner scanner;

    public ManagerUI(BankManager bankManager, TransactionManager transactionManager, CustomerManager customerManager, Scanner scanner) {
        this.bankManager = bankManager;
        this.transactionManager = transactionManager;
        this.customerManager = customerManager;
        this.scanner = scanner;
    }

    @Override
    public void displayDashboard() {
        int action;
        do {
            System.out.println("Manager Dashboard: " + bankManager.getName());
            System.out.println("1. Approve Transactions");
            System.out.println("2. Approve Loans");
            System.out.println("3. Add Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Generate Customer Report");
            System.out.println("0. Exit");
            System.out.println("Choose an action:");
            action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1:
                    approveTransactions();
                    break;
                case 2:
                    approveLoans();
                    break;
                case 3:
                    addCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 5:
                    generateCustomerReport();
                    break;
                case 0:
                    System.out.println("Exiting manager dashboard...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (action != 0);
    }

    private void approveTransactions() {
        List<Transaction> pendingTransactions = transactionManager.getPendingTransactions();
        if (pendingTransactions.isEmpty()) {
            System.out.println("No pending transactions to approve.");
            return;
        }
        for (Transaction transaction : pendingTransactions) {
            System.out.println("Transaction ID: " + transaction.getTransactionId() + " - $" + transaction.getAmount() + " - " + transaction.getType());
            System.out.println("Approve? (yes/no)");
            String response = scanner.nextLine();
            if ("yes".equalsIgnoreCase(response)) {
                transactionManager.approveTransaction(transaction.getTransactionId());
            }
        }
    }

    private void approveLoans() {
        List<Loan> pendingLoans = customerManager.getPendingLoans();
        if (pendingLoans.isEmpty()) {
            System.out.println("No pending loans to approve.");
            return;
        }
        for (Loan loan : pendingLoans) {
            System.out.printf("Loan Request: $%.2f at %.2f%% interest. Approve? (yes/no)\n", loan.getLoanAmount(), loan.getInterestRate());
            String response = scanner.nextLine();
            if ("yes".equalsIgnoreCase(response)) {
                loan.approveLoan();
                Customer customer = customerManager.getCustomerByLoan(loan);
                if (customer != null) {
                    Account account = customer.getAccountsList().get(0);  // Assuming money goes to the first account or enhance this logic
                    account.deposit(loan.getLoanAmount());
                    System.out.println("Loan approved and funds deposited to account " + account.getAccountNumber());
                } else {
                    System.out.println("Failed to find customer for the loan.");
                }
            }
        }
    }

    private void addCustomer() {
        System.out.println("Enter Customer ID:");
        String id = scanner.nextLine();
        System.out.println("Enter Customer Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Customer Password:");
        String password = scanner.nextLine();
        Customer newCustomer = new Customer(id, name, password);
        customerManager.addCustomer(newCustomer);
    }

    private void deleteCustomer() {
        System.out.println("Enter Customer ID to delete:");
        String customerId = scanner.nextLine();
        customerManager.deleteCustomer(customerId);
    }

    private void generateCustomerReport() {
        System.out.println("Enter Customer ID for report:");
        String customerId = scanner.nextLine();
        String report = customerManager.generateCustomerReport(customerId);
        System.out.println(report);
    }
}
