package org.example;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public interface UI {
    void displayDashboard();
}

class CustomerUI implements UI {
    private Customer customer;
    private Scanner scanner;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private TransactionManager transactionManager;

    public CustomerUI(Customer customer, Scanner scanner, CustomerManager customerManager, AccountManager accountManager, TransactionManager transactionManager) {
        this.customer = customer;
        this.scanner = scanner;
        this.customerManager = customerManager;
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
    }

    @Override
    public void displayDashboard() {
        int action;
        do {
            System.out.println("Customer Dashboard: " + customer.getName());
            System.out.println("1. View Accounts");
            System.out.println("2. View Transactions");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Apply for Loan");
            System.out.println("7. View Loans");
            System.out.println("0. Logout");
            System.out.println("Choose an action:");
            action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    viewAccounts();
                    break;
                case 2:
                    viewTransactions();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    withdraw();
                    break;
                case 5:
                    transfer();
                    break;
                case 6:
                    applyForLoan();
                    break;
                case 7:
                    viewLoans();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (action != 0);
    }

    private void viewAccounts() {
        System.out.println("Accounts:");
        for (Account account : customer.getAccountsList()) {
            System.out.println(account.getAccountType() + " (" + account.getAccountNumber() + "): $" + account.getBalance());
        }
    }

    private void viewTransactions() {
        System.out.println("Transactions:");
        for (Account account : customer.getAccountsList()) {
            for (Transaction transaction : account.getTransactions()) {
                System.out.println(transaction.getType() + ": $" + transaction.getAmount() + " Status: " + transaction.getStatus());
            }
        }
    }

    private void deposit() {
        Account account = selectAccount();
        if (account == null) return;

        System.out.println("Enter deposit amount:");
        double amount = Double.parseDouble(scanner.nextLine());
        account.deposit(amount);
        Transaction transaction = new Transaction(java.util.UUID.randomUUID().toString(), "Deposit", amount, account);
        transactionManager.logTransaction(transaction);
        account.addTransaction(transaction);  // Add transaction to the account
        System.out.println("Deposit successful.");
        accountManager.saveAccounts();
    }

    private void withdraw() {
        Account account = selectAccount();
        if (account == null) return;

        System.out.println("Enter withdrawal amount:");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            account.withdraw(amount);
            Transaction transaction = new Transaction(java.util.UUID.randomUUID().toString(), "Withdrawal", amount, account);
            transactionManager.logTransaction(transaction);
            account.addTransaction(transaction);  // Add transaction to the account
            System.out.println("Withdrawal successful.");
            accountManager.saveAccounts();
        } catch (InsufficientFundsException e) {
            System.out.println("Insufficient funds for withdrawal.");
        }
    }

    private void transfer() {
        Account sourceAccount = selectAccount();
        if (sourceAccount == null) return;

        Account destinationAccount = selectAccount();
        if (destinationAccount == null) return;

        System.out.println("Enter transfer amount:");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            sourceAccount.transfer(amount, destinationAccount);
            Transaction transaction = new Transaction(java.util.UUID.randomUUID().toString(), "Transfer", amount, sourceAccount);
            transactionManager.logTransaction(transaction);
            sourceAccount.addTransaction(transaction);  // Add transaction to the source account
            System.out.println("Transfer successful.");
            accountManager.saveAccounts();
        } catch (InsufficientFundsException e) {
            System.out.println("Insufficient funds for transfer.");
        }
    }

    private void applyForLoan() {
        System.out.println("Enter loan amount:");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter interest rate:");
        double interestRate = Double.parseDouble(scanner.nextLine());
        System.out.println("Select account to receive loan:");
        Account account = selectAccount();
        if (account == null) return;

        Loan loan = new Loan(amount, interestRate, account.getAccountNumber());
        customer.addLoan(loan);
        customerManager.addLoan(loan);
        System.out.println("Loan application submitted.");
        customerManager.saveLoans();
    }

    private void viewLoans() {
        System.out.println("Loans:");
        for (Loan loan : customer.getLoans()) {
            System.out.println("Loan Amount: $" + loan.getLoanAmount() + " Interest Rate: " + loan.getInterestRate() + "% Approved: " + (loan.isApproved() ? "Yes" : "No") + " Paid Off: " + (loan.isPaidOff() ? "Yes" : "No"));
        }
    }

    private Account selectAccount() {
        System.out.println("Select an account:");
        for (int i = 0; i < customer.getAccountsList().size(); i++) {
            Account account = customer.getAccountsList().get(i);
            System.out.println((i + 1) + ". " + account.getAccountType() + " (" + account.getAccountNumber() + "): $" + account.getBalance());
        }
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > customer.getAccountsList().size()) {
            System.out.println("Invalid choice.");
            return null;
        }
        return customer.getAccountsList().get(choice - 1);
    }
}





class ManagerUI implements UI {
    private BankManager bankManager;
    private TransactionManager transactionManager;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private Scanner scanner;

    public ManagerUI(BankManager bankManager, TransactionManager transactionManager, CustomerManager customerManager, AccountManager accountManager, Scanner scanner) {
        this.bankManager = bankManager;
        this.transactionManager = transactionManager;
        this.customerManager = customerManager;
        this.accountManager = accountManager;
        this.scanner = scanner;
    }

    @Override
    public void displayDashboard() {
        int action;
        do {
            System.out.println("Manager Dashboard: " + bankManager.getName());
            System.out.println("1. Approve Transactions");
            System.out.println("2. Approve Loans");
            System.out.println("3. Generate Customer Report");
            System.out.println("4. Add Customer Account");
            System.out.println("5. Delete Customer Account");
            System.out.println("6. Delete Customer");
            System.out.println("0. Logout");
            System.out.println("Choose an action:");
            action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    approveTransactions();
                    break;
                case 2:
                    approveLoans();
                    break;
                case 3:
                    generateCustomerReport();
                    break;
                case 4:
                    addCustomerAccount();
                    break;
                case 5:
                    deleteCustomerAccount();
                    break;
                case 6:
                    deleteCustomer();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (action != 0);
    }

    private void approveTransactions() {
        List<Transaction> pendingTransactions = transactionManager.getTransactions(); // Adjust this line as needed
        for (Transaction transaction : pendingTransactions) {
            if ("Pending".equals(transaction.getStatus())) {
                System.out.println("Approve transaction: " + transaction.getType() + " of $" + transaction.getAmount() + " for account " + transaction.getAccount().getAccountNumber() + "? (yes/no)");
                String response = scanner.nextLine();
                if ("yes".equalsIgnoreCase(response)) {
                    transaction.setStatus("Approved");
                    transactionManager.saveTransactions();
                    System.out.println("Transaction approved.");
                } else {
                    System.out.println("Transaction not approved.");
                }
            }
        }
    }

    private void approveLoans() {
        List<Loan> pendingLoans = customerManager.getPendingLoans();
        for (Loan loan : pendingLoans) {
            System.out.println("Approve loan of $" + loan.getLoanAmount() + " with interest rate " + loan.getInterestRate() + "% for account " + loan.getAccountNumber() + "? (yes/no)");
            String response = scanner.nextLine();
            if ("yes".equalsIgnoreCase(response)) {
                loan.approveLoan();
                Account account = accountManager.getAccount(loan.getAccountNumber());
                if (account != null) {
                    account.deposit(loan.getLoanAmount());
                    accountManager.saveAccounts();
                }
                customerManager.saveLoans();
                System.out.println("Loan approved.");
            } else {
                System.out.println("Loan not approved.");
            }
        }
    }

    private void generateCustomerReport() {
        System.out.println("Enter customer ID:");
        String customerId = scanner.nextLine();
        String report = customerManager.generateCustomerReport(customerId);
        System.out.println(report);
    }

    private void addCustomerAccount() {
        System.out.println("Enter customer ID:");
        String customerId = scanner.nextLine();
        Customer customer = customerManager.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Enter initial balance:");
        double balance = Double.parseDouble(scanner.nextLine());
        System.out.println("Choose account type:");
        System.out.println("1. Savings");
        System.out.println("2. Checking");
        int choice = Integer.parseInt(scanner.nextLine());

        Account account;
        if (choice == 1) {
            account = new SavingsAccount(java.util.UUID.randomUUID().toString(), customerId, balance);
        } else {
            account = new CheckingAccount(java.util.UUID.randomUUID().toString(), customerId, balance);
        }

        customer.getAccountsList().add(account);
        accountManager.addAccount(account);
        System.out.println("Account added successfully.");
    }

    private void deleteCustomerAccount() {
        System.out.println("Enter customer ID:");
        String customerId = scanner.nextLine();
        Customer customer = customerManager.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Enter account number to delete:");
        String accountNumber = scanner.nextLine();
        Account account = accountManager.getAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        customer.getAccountsList().remove(account);
        accountManager.removeAccountsByCustomerId(customerId);
        System.out.println("Account deleted successfully.");
    }

    private void deleteCustomer() {
        System.out.println("Enter customer ID:");
        String customerId = scanner.nextLine();
        boolean success = customerManager.deleteCustomer(customerId);
        if (success) {
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }
}















