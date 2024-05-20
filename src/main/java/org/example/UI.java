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
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. View Transactions");
            System.out.println("5. Apply for Loan");
            System.out.println("6. View Loans");
            System.out.println("7. Open Account");
            System.out.println("8. Pay Off Loan");
            System.out.println("0. Logout");
            System.out.println("Choose an action:");
            action = Integer.parseInt(scanner.nextLine());
            switch (action) {
                case 1:
                    deposit();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    transfer();
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
                case 7:
                    openAccount();
                    break;
                case 8:
                    payOffLoan();
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

    private void deposit() {
        Account account = selectAccount();
        if (account == null) return;
        System.out.print("Enter amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine());
        account.deposit(amount);
        accountManager.saveAccounts();
        transactionManager.saveTransactions();
        System.out.println("Deposit successful.");
    }

    private void withdraw() {
        Account account = selectAccount();
        if (account == null) return;
        System.out.print("Enter amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            account.withdraw(amount);
            accountManager.saveAccounts();
            transactionManager.saveTransactions();
            System.out.println("Withdrawal successful.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void transfer() {
        System.out.println("Select source account:");
        Account sourceAccount = selectAccount();
        if (sourceAccount == null) return;
        System.out.println("Select destination account:");
        Account destinationAccount = selectAccount();
        if (destinationAccount == null) return;
        System.out.print("Enter amount to transfer: ");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            sourceAccount.transfer(amount, destinationAccount);
            accountManager.saveAccounts();
            transactionManager.saveTransactions();
            System.out.println("Transfer successful.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewTransactions() {
        Account account = selectAccount();
        if (account == null) return;
        List<Transaction> transactions = account.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getType() + ": $" + transaction.getAmount() + " Status: " + transaction.getStatus());
            }
        }
    }

    private void applyForLoan() {
        System.out.print("Enter loan amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.println("Select account to receive the loan:");
        Account account = selectAccount();
        if (account == null) return;
        Loan loan = new Loan(amount, account.getAccountNumber());
        customer.addLoan(loan);
        customerManager.addLoan(loan);
        customerManager.saveCustomers();
        customerManager.saveLoans();
        System.out.println("Loan application submitted.");
    }

    private void viewLoans() {
        List<Loan> loans = customer.getLoans();
        if (loans.isEmpty()) {
            System.out.println("No loans found.");
        } else {
            for (Loan loan : loans) {
                System.out.println("Loan Amount: $" + loan.getLoanAmount() + " Interest Rate: " + loan.getInterestRate() + "% Approved: " + (loan.isApproved() ? "Yes" : "No") + " Paid Off: " + (loan.isPaidOff() ? "Yes" : "No"));
            }
        }
    }

    private void openAccount() {
        System.out.println("Select account type:");
        System.out.println("1. Savings");
        System.out.println("2. Checking");
        int accountType = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter initial deposit amount: ");
        double balance = Double.parseDouble(scanner.nextLine());
        Account account;
        if (accountType == 1) {
            account = new SavingsAccount(UUID.randomUUID().toString(), customer.getCustomerID(), balance, transactionManager);
        } else {
            account = new CheckingAccount(UUID.randomUUID().toString(), customer.getCustomerID(), balance, transactionManager);
        }
        accountManager.addAccount(account);
        customer.addAccount(account);
        customerManager.saveCustomers();
        accountManager.saveAccounts();
        System.out.println("Account opened successfully.");
    }

    private void payOffLoan() {
        System.out.println("Select a loan to pay off:");
        List<Loan> loans = customer.getLoans();
        if (loans.isEmpty()) {
            System.out.println("No loans found.");
            return;
        }
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            System.out.println((i + 1) + ". Loan Amount: $" + loan.getLoanAmount() + " Interest Rate: " + loan.getInterestRate() + "% Approved: " + (loan.isApproved() ? "Yes" : "No") + " Paid Off: " + (loan.isPaidOff() ? "Yes" : "No"));
        }
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > loans.size()) {
            System.out.println("Invalid loan selection. Please try again.");
            return;
        }
        Loan selectedLoan = loans.get(choice - 1);
        if (!selectedLoan.isApproved()) {
            System.out.println("Loan not approved yet.");
            return;
        }
        if (selectedLoan.isPaidOff()) {
            System.out.println("Loan already paid off.");
            return;
        }
        double totalAmount = selectedLoan.getLoanAmount() * (1 + selectedLoan.getInterestRate());
        System.out.println("Total amount to pay off: $" + totalAmount);
        Account account = selectAccount();
        if (account == null) return;
        try {
            account.withdraw(totalAmount);
            selectedLoan.payOffLoan();
            accountManager.saveAccounts();
            customerManager.saveLoans();
            System.out.println("Loan paid off successfully.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Account selectAccount() {
        List<Account> accounts = accountManager.getAccountsByCustomerId(customer.getCustomerID());
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return null;
        }
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getAccountType() + " (" + account.getAccountNumber() + "): $" + account.getBalance());
        }
        System.out.print("Select an account: ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > accounts.size()) {
            System.out.println("Invalid account selection. Please try again.");
            return null;
        }
        return accounts.get(choice - 1);
    }
}




class ManagerUI implements UI {
    private BankManager bankManager;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private Scanner scanner;

    public ManagerUI(BankManager bankManager, CustomerManager customerManager, AccountManager accountManager, Scanner scanner) {
        this.bankManager = bankManager;
        this.customerManager = customerManager;
        this.accountManager = accountManager;
        this.scanner = scanner;
    }

    @Override
    public void displayDashboard() {
        int action;
        do {
            System.out.println("Manager Dashboard: " + bankManager.getManagerId());
            System.out.println("1. Approve Loans");
            System.out.println("2. Add Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. Generate Customer Report");
            System.out.println("0. Logout");
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
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        } while (action != 0);
    }

    private void approveLoans() {
        List<Loan> pendingLoans = customerManager.getLoans();
        for (Loan loan : pendingLoans) {
            if (!loan.isApproved()) {
                System.out.println("Approve loan: " + loan.getLoanAmount() + " for account " + loan.getAccountNumber() + "? (yes/no)");
                String approval = scanner.nextLine();
                if (approval.equalsIgnoreCase("yes")) {
                    loan.setApproved(true);
                    Account account = accountManager.getAccount(loan.getAccountNumber());
                    if (account != null) {
                        account.deposit(loan.getLoanAmount());
                        accountManager.saveAccounts();
                    }
                    System.out.println("Loan approved and funds deposited.");
                } else {
                    System.out.println("Loan not approved.");
                }
            }
        }
        customerManager.saveLoans();
    }

    private void addCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer password: ");
        String password = scanner.nextLine();
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();
        Customer newCustomer = new Customer(name, password, email);
        customerManager.addCustomer(newCustomer);
        System.out.println("Customer added successfully.");
    }

    private void deleteCustomer() {
        System.out.print("Enter customer ID to delete: ");
        String customerId = scanner.nextLine();
        boolean success = customerManager.deleteCustomer(customerId);
        if (success) {
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    private void generateCustomerReport() {
        System.out.print("Enter customer ID for report: ");
        String customerId = scanner.nextLine();
        String report = customerManager.generateCustomerReport(customerId);
        System.out.println(report);
    }
}

























