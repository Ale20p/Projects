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
        if (customer.getAccountsList().isEmpty()) {
            System.out.println("No accounts available. Please contact your bank manager.");
            return;
        }

        if (!selectAccount(false)) {  // Let the customer select the account at the start for other operations
            System.out.println("No valid account selected or available. Exiting dashboard.");
            return;
        }

        int action;
        do {
            System.out.println("Selected Account Number: " + selectedAccount.getAccountNumber() + ", Balance: $" + selectedAccount.getBalance());
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
                    if (!selectAccount(true)) {  // Select account for transfer, ensuring it's different
                        System.out.println("Transfer canceled or invalid account selection.");
                        break;
                    }
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
                    return; // Return to main menu
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (action != 0);
    }

    private boolean selectAccount(boolean forTransfer) {
        List<Account> accounts = customer.getAccountsList();
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return false;
        }

        System.out.println("Select an account by number:");
        for (int i = 0; i < accounts.size(); i++) {
            if (!forTransfer || accounts.get(i) != selectedAccount) {  // Exclude the selected account for transfer
                System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + ", Balance: $" + accounts.get(i).getBalance());
            }
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
        System.out.println("Enter the amount to transfer:");
        double amount = scanner.nextDouble();
        try {
            if (selectedAccount.getBalance() >= amount) {
                selectedAccount.withdraw(amount);
                Account destinationAccount = selectedAccount; // The second selection is stored in the same reference
                destinationAccount.deposit(amount);
                System.out.println("Transferred $" + amount + " from account " + selectedAccount.getAccountNumber() + " to " + destinationAccount.getAccountNumber());
            } else {
                System.out.println("Insufficient funds for this transfer.");
            }
        } catch (InsufficientFundsException e) {
            System.out.println("Failed to transfer: " + e.getMessage());
        }
    }

    private void viewTransactions() {
        List<Transaction> transactions = selectedAccount.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
        } else {
            System.out.println("Transaction history:");
            for (Transaction transaction : transactions) {
                System.out.printf("Type: %s, Amount: $%.2f, Approved: %s\n",
                        transaction.getType(), transaction.getAmount(), transaction.isApproved() ? "Yes" : "No");
            }
        }
    }

    private void applyForLoan() {
        System.out.println("Enter the loan amount:");
        double amount = scanner.nextDouble();
        System.out.println("Enter the interest rate (e.g., 5.5 for 5.5%):");
        double interestRate = scanner.nextDouble();
        Loan loan = new Loan(amount, interestRate);
        customer.addLoan(loan);
        System.out.println("Loan application submitted for $" + amount + " at " + interestRate + "% interest.");
    }

    private void viewLoans() {
        List<Loan> loans = customer.getLoans();
        if (loans.isEmpty()) {
            System.out.println("You have no loans.");
            return;
        }
        System.out.println("Your loans:");
        for (Loan loan : loans) {
            System.out.printf("Amount: $%.2f, Interest Rate: %.2f%%, Approved: %s\n",
                    loan.getLoanAmount(), loan.getInterestRate(), loan.isApproved() ? "Yes" : "No");
        }
    }
}


import java.util.Scanner;

public class ManagerUI implements UI {
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
        System.out.println("Manager Dashboard: " + bankManager.getName());
        String action;
        do {
            System.out.println("\nAvailable Actions:");
            System.out.println("1. Approve Transactions");
            System.out.println("2. Add Customer");
            System.out.println("3. Add Account to Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Delete Account");
            System.out.println("6. Generate Customer Reports");
            System.out.println("0. Exit");
            System.out.print("Choose an action: ");
            action = scanner.nextLine();

            switch (action) {
                case "1":
                    approveTransactions();
                    break;
                case "2":
                    addCustomer();
                    break;
                case "3":
                    addAccountToCustomer();
                    break;
                case "4":
                    deleteCustomer();
                    break;
                case "5":
                    deleteAccount();
                    break;
                case "6":
                    generateReports();
                    break;
                case "0":
                    System.out.println("Exiting manager dashboard...");
                    break;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (!"0".equals(action));
    }

    private void approveTransactions() {
        List<Transaction> pendingTransactions = transactionManager.getPendingTransactions();
        if (pendingTransactions.isEmpty()) {
            System.out.println("No pending transactions to approve.");
            return;
        }
        System.out.println("Pending Transactions:");
        for (Transaction transaction : pendingTransactions) {
            System.out.println(transaction.getTransactionId() + " - $" + transaction.getAmount() + " - " + transaction.getType());
        }
        System.out.println("Enter transaction ID to approve or 'all' to approve all:");
        String input = scanner.nextLine();
        if ("all".equalsIgnoreCase(input)) {
            for (Transaction transaction : pendingTransactions) {
                transactionManager.approveTransaction(transaction.getTransactionId());
                System.out.println("Transaction " + transaction.getTransactionId() + " approved.");
            }
        } else {
            transactionManager.approveTransaction(input);
            System.out.println("Transaction " + input + " approved.");
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

    private void addAccountToCustomer() {
        System.out.println("Enter Customer ID:");
        String customerId = scanner.nextLine();
        Customer customer = customerManager.getCustomer(customerId);
        if (customer != null) {
            System.out.println("Enter Account Number:");
            String accountNumber = scanner.nextLine();
            System.out.println("Enter Initial Balance:");
            double initialBalance = Double.parseDouble(scanner.nextLine());
            Account newAccount = new SavingsAccount(accountNumber, initialBalance);  // Assuming all new accounts are SavingsAccounts
            customer.addAccount(newAccount);
            System.out.println("Account added successfully to customer " + customerId);
        } else {
            System.out.println("Customer not found.");
        }
    }

    private void deleteCustomer() {
        System.out.println("Enter Customer ID to delete:");
        String id = scanner.nextLine();
        customerManager.deleteCustomer(id);
    }

    private void deleteAccount() {
        System.out.println("Enter Customer ID:");
        String customerId = scanner.nextLine();
        System.out.println("Enter Account Number to delete:");
        String accountNumber = scanner.nextLine();
        customerManager.deleteAccount(customerId, accountNumber);
    }

    private void generateReports() {
        List<String> reports = customerManager.generateCustomerReports();
        System.out.println("Customer Reports:");
        for (String report : reports) {
            System.out.println(report);
        }
    }
}
