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

    public CustomerUI(Customer customer, Scanner scanner, CustomerManager customerManager) {
        this.customer = customer;
        this.scanner = scanner;
        this.customerManager = customerManager;
    }

    @Override
    public void displayDashboard() {
        int action;
        do {
            System.out.println("Customer Dashboard: " + customer.getName());
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. View Transactions");
            System.out.println("5. Apply for Loan");
            System.out.println("6. View Loans");
            System.out.println("0. Exit");
            System.out.println("Choose an action:");
            action = Integer.parseInt(scanner.nextLine());
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
                    System.out.println("Exiting customer dashboard...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (action != 0);
    }

    private void performDeposit() {
        Account account = selectAccount();
        if (account == null) return;
        System.out.println("Enter amount to deposit:");
        double amount = Double.parseDouble(scanner.nextLine());
        account.deposit(amount);
        try {
            customerManager.saveAccounts();
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    private void performWithdrawal() {
        Account account = selectAccount();
        if (account == null) return;
        System.out.println("Enter amount to withdraw:");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            account.withdraw(amount);
            try {
                customerManager.saveAccounts();
            } catch (IOException e) {
                System.err.println("Error saving accounts: " + e.getMessage());
            }
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void performTransfer() {
        System.out.println("Select source account:");
        Account sourceAccount = selectAccount();
        if (sourceAccount == null) return;
        System.out.println("Select destination account:");
        Account destinationAccount = selectAccount();
        if (destinationAccount == null) return;
        System.out.println("Enter amount to transfer:");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            sourceAccount.transfer(amount, destinationAccount);
            try {
                customerManager.saveAccounts();
            } catch (IOException e) {
                System.err.println("Error saving accounts: " + e.getMessage());
            }
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewTransactions() {
        for (Account account : customer.getAccountsList()) {
            System.out.println("Transactions for account " + account.getAccountNumber() + ":");
            for (Transaction transaction : account.getTransactions()) {
                System.out.println(transaction.getType() + ": $" + transaction.getAmount() + " - " + transaction.getStatus());
            }
        }
    }

    private void applyForLoan() {
        System.out.println("Enter loan amount:");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.println("Enter interest rate:");
        double interestRate = Double.parseDouble(scanner.nextLine());
        Loan loan = new Loan(amount, interestRate);
        customer.addLoan(loan);
        System.out.println("Loan applied successfully. Pending approval.");
        try {
            customerManager.saveLoans();
        } catch (IOException e) {
            System.err.println("Error saving loans: " + e.getMessage());
        }
    }

    private void viewLoans() {
        for (Loan loan : customer.getLoans()) {
            System.out.println("Loan Amount: $" + loan.getLoanAmount() + " - Interest Rate: " + loan.getInterestRate() + "% - Approved: " + (loan.isApproved() ? "Yes" : "No") + " - Paid Off: " + (loan.isPaidOff() ? "Yes" : "No"));
        }
    }

    private Account selectAccount() {
        System.out.println("Select an account:");
        for (int i = 0; i < customer.getAccountsList().size(); i++) {
            Account account = customer.getAccountsList().get(i);
            System.out.println((i + 1) + ". " + account.getAccountType() + " - " + account.getAccountNumber() + ": Balance $" + account.getBalance());
        }
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice >= 0 && choice < customer.getAccountsList().size()) {
            return customer.getAccountsList().get(choice);
        }
        System.out.println("Invalid selection.");
        return null;
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
            System.out.println("1. Approve Loans");
            System.out.println("2. Add Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. Generate Customer Report");
            System.out.println("5. Add Customer Account");
            System.out.println("6. Delete Customer Account");
            System.out.println("0. Exit");
            System.out.println("Choose an action:");
            action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    approveLoans();
                    break;
                case 2:
                    addCustomer();
                    break;
                case 3:
                    deleteCustomer();
                    break;
                case 4:
                    generateCustomerReport();
                    break;
                case 5:
                    addCustomerAccount();
                    break;
                case 6:
                    deleteCustomerAccount();
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
                    account.setBalance(account.getBalance() + loan.getLoanAmount());
                    System.out.println("Loan approved and funds deposited to account " + account.getAccountNumber());
                    try {
                        customerManager.saveLoans();  // Save the updated loan information to file
                        customerManager.saveAccounts();  // Save the updated account information to file
                        customerManager.loadLoans();  // Reload loans after saving
                        customerManager.loadAccounts();  // Reload accounts after saving
                    } catch (IOException e) {
                        System.err.println("Error saving loans or accounts: " + e.getMessage());
                    }
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
        System.out.println("Enter Customer Email:");
        String email = scanner.nextLine();
        Customer newCustomer = new Customer(id, name, password, email);
        customerManager.addCustomer(newCustomer);
        System.out.println("Customer added successfully.");
    }

    private void deleteCustomer() {
        System.out.println("Enter Customer ID to delete:");
        String customerId = scanner.nextLine();
        boolean success = customerManager.deleteCustomer(customerId);
        if (success) {
            System.out.println("Customer successfully deleted.");
            try {
                customerManager.loadCustomers();  // Reload customers after deletion
                customerManager.loadAccounts();  // Reload accounts after deletion
                customerManager.loadLoans();  // Reload loans after deletion
            } catch (IOException e) {
                System.err.println("Error loading customers, accounts, or loans: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to delete customer.");
        }
    }

    private void generateCustomerReport() {
        System.out.println("Enter Customer ID for report:");
        String customerId = scanner.nextLine();
        String report = customerManager.generateCustomerReport(customerId);
        System.out.println(report);
    }

    private void addCustomerAccount() {
        System.out.println("Enter Customer ID:");
        String customerId = scanner.nextLine();
        Customer customer = customerManager.getCustomer(customerId);
        if (customer == null) {
            System.out.println("No such customer found.");
            return;
        }
        System.out.println("Enter account type (Savings/Checking):");
        String type = scanner.nextLine();
        System.out.println("Enter initial balance:");
        double balance = Double.parseDouble(scanner.nextLine());
        Account account = type.equalsIgnoreCase("Savings") ?
                new SavingsAccount(UUID.randomUUID().toString(), customerId, balance, transactionManager) :
                new CheckingAccount(UUID.randomUUID().toString(), customerId, balance, transactionManager);
        customer.addAccount(account);
        try {
            customerManager.saveAccounts();  // Save the updated account information to file
            customerManager.loadAccounts();  // Reload accounts after saving
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
        System.out.println("Account added successfully.");
    }

    private void deleteCustomerAccount() {
        System.out.println("Enter Customer ID:");
        String customerId = scanner.nextLine();
        Customer customer = customerManager.getCustomer(customerId);
        if (customer == null) {
            System.out.println("No such customer found.");
            return;
        }
        System.out.println("Enter Account Number to delete:");
        String accountNumber = scanner.nextLine();
        boolean removed = customer.deleteAccount(accountNumber);
        if (removed) {
            try {
                customerManager.saveAccounts();  // Save the updated account information to file
                customerManager.loadAccounts();  // Reload accounts after saving
                System.out.println("Account deleted successfully.");
            } catch (IOException e) {
                System.err.println("Error saving accounts: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to delete account.");
        }
    }
}







