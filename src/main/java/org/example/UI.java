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
    private TransactionManager transactionManager;

    public CustomerUI(Customer customer, Scanner scanner, CustomerManager customerManager, TransactionManager transactionManager) {
        this.customer = customer;
        this.scanner = scanner;
        this.customerManager = customerManager;
        this.transactionManager = transactionManager;
    }

    @Override
    public void displayDashboard() {
        int action;
        do {
            System.out.println("Customer Dashboard: " + customer.getName());
            System.out.println("1. View Accounts");
            System.out.println("2. View Transactions");
            System.out.println("3. Apply for Loan");
            System.out.println("4. View Loans");
            System.out.println("5. Deposit Money");
            System.out.println("6. Withdraw Money");
            System.out.println("7. Open New Account");
            System.out.println("8. Pay Off Loan");
            System.out.println("9. Transfer Money");
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
                    applyForLoan();
                    break;
                case 4:
                    viewLoans();
                    break;
                case 5:
                    depositMoney();
                    break;
                case 6:
                    withdrawMoney();
                    break;
                case 7:
                    openNewAccount();
                    break;
                case 8:
                    payOffLoan();
                    break;
                case 9:
                    performTransfer();
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
            System.out.println(account.getAccountType() + " - " + account.getAccountNumber() + ": Balance $" + account.getBalance());
        }
    }

    private void viewTransactions() {
        System.out.println("Transactions:");
        for (Account account : customer.getAccountsList()) {
            System.out.println("Account: " + account.getAccountNumber());
            for (Transaction transaction : account.getTransactions()) {
                System.out.println(transaction.getType() + ": $" + transaction.getAmount());
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
        System.out.println("Loan application submitted.");
        try {
            customerManager.saveLoans();  // Save loans whenever a new loan is created
        } catch (IOException e) {
            System.err.println("Error saving loans: " + e.getMessage());
        }
    }

    private void viewLoans() {
        System.out.println("Loans:");
        for (Loan loan : customer.getLoans()) {
            System.out.println("Loan Amount: $" + loan.getLoanAmount() + ", Interest Rate: " + loan.getInterestRate() + "%, Approved: " + (loan.isApproved() ? "Yes" : "No") + ", Paid Off: " + (loan.isPaidOff() ? "Yes" : "No"));
        }
    }

    private void depositMoney() {
        Account account = selectAccount();
        if (account != null) {
            System.out.println("Enter amount to deposit:");
            double amount = Double.parseDouble(scanner.nextLine());
            account.deposit(amount);
            System.out.println("Deposit processed.");
        } else {
            System.out.println("No account selected.");
        }
    }

    private void withdrawMoney() {
        Account account = selectAccount();
        if (account != null) {
            System.out.println("Enter amount to withdraw:");
            double amount = Double.parseDouble(scanner.nextLine());
            try {
                account.withdraw(amount);
                System.out.println("Withdrawal processed.");
            } catch (InsufficientFundsException e) {
                System.out.println("Insufficient funds for withdrawal.");
            }
        } else {
            System.out.println("No account selected.");
        }
    }

    private void openNewAccount() {
        System.out.println("Enter account type (Savings/Checking):");
        String type = scanner.nextLine();
        System.out.println("Enter initial balance:");
        double balance = Double.parseDouble(scanner.nextLine());
        Account account = type.equalsIgnoreCase("Savings") ?
                new SavingsAccount(UUID.randomUUID().toString(), customer.getCustomerID(), balance, transactionManager) :
                new CheckingAccount(UUID.randomUUID().toString(), customer.getCustomerID(), balance, transactionManager);
        customer.addAccount(account);
        try {
            customerManager.saveAccounts();  // Save accounts whenever a new one is created
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
        System.out.println("Account opened successfully.");
    }

    private void payOffLoan() {
        System.out.println("Loans:");
        int loanIndex = 1;
        for (Loan loan : customer.getLoans()) {
            System.out.println(loanIndex++ + ". Loan Amount: $" + loan.getLoanAmount() + ", Interest Rate: " + loan.getInterestRate() + "%, Approved: " + (loan.isApproved() ? "Yes" : "No") + ", Paid Off: " + (loan.isPaidOff() ? "Yes" : "No"));
        }
        System.out.println("Enter the number of the loan you want to pay off:");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice > 0 && choice <= customer.getLoans().size()) {
            Loan loan = customer.getLoans().get(choice - 1);
            loan.payOffLoan();
            try {
                customerManager.saveLoans();  // Save loans whenever a loan is paid off
            } catch (IOException e) {
                System.err.println("Error saving loans: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid choice. No loan paid off.");
        }
    }

    private void performTransfer() {
        System.out.println("Select source account:");
        Account sourceAccount = selectAccount();
        if (sourceAccount == null) {
            System.out.println("No source account selected.");
            return;
        }

        System.out.println("Select destination account:");
        Account destinationAccount = selectAccount();
        if (destinationAccount == null) {
            System.out.println("No destination account selected.");
            return;
        }

        if (sourceAccount.equals(destinationAccount)) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        System.out.println("Enter amount to transfer:");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            sourceAccount.transfer(amount, destinationAccount);
            System.out.println("Transfer processed.");
        } catch (InsufficientFundsException e) {
            System.out.println("Insufficient funds for transfer.");
        }
    }

    private Account selectAccount() {
        List<Account> accounts = customer.getAccountsList();
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return null;
        }
        System.out.println("Select an account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getAccountType() + " - " + account.getAccountNumber() + ": Balance $" + account.getBalance());
        }
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice > 0 && choice <= accounts.size()) {
            return accounts.get(choice - 1);
        } else {
            System.out.println("Invalid choice.");
            return null;
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
            System.out.println("6. Add Customer Account");
            System.out.println("7. Delete Customer Account");
            System.out.println("0. Exit");
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
                    addCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 5:
                    generateCustomerReport();
                    break;
                case 6:
                    addCustomerAccount();
                    break;
                case 7:
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
                System.out.println("Transaction approved.");
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
                    try {
                        customerManager.saveLoans();  // Save the updated loan information to file
                        customerManager.saveAccounts();  // Save the updated account information to file
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
            } catch (IOException e) {
                System.err.println("Error saving accounts: " + e.getMessage());
            }
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Failed to delete account.");
        }
    }
}



