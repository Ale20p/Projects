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
            System.out.println("4. View Accounts");
            System.out.println("5. View Transactions");
            System.out.println("6. Apply for Loan");
            System.out.println("7. View Loans");
            System.out.println("8. Pay Off Loan");
            System.out.println("9. Open New Account");
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
                    viewAccounts();
                    break;
                case 5:
                    viewTransactions();
                    break;
                case 6:
                    applyForLoan();
                    break;
                case 7:
                    viewLoans();
                    break;
                case 8:
                    payOffLoan();
                    break;
                case 9:
                    openNewAccount();
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
        System.out.println("Enter amount to deposit:");
        double amount = Double.parseDouble(scanner.nextLine());
        account.deposit(amount);
        accountManager.saveAccounts();
        System.out.println("Deposit successful.");
    }

    private void withdraw() {
        Account account = selectAccount();
        System.out.println("Enter amount to withdraw:");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            account.withdraw(amount);
            accountManager.saveAccounts();
            System.out.println("Withdrawal successful.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void transfer() {
        Account sourceAccount = selectAccount();
        System.out.println("Enter destination account number:");
        String destinationAccountNumber = scanner.nextLine();
        Account destinationAccount = accountManager.getAccount(destinationAccountNumber);
        if (destinationAccount == null) {
            System.out.println("Destination account not found.");
            return;
        }
        System.out.println("Enter amount to transfer:");
        double amount = Double.parseDouble(scanner.nextLine());
        try {
            sourceAccount.transfer(amount, destinationAccount);
            accountManager.saveAccounts();
            System.out.println("Transfer successful.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAccounts() {
        List<Account> accounts = customer.getAccountsList();
        System.out.println("Accounts:");
        for (Account account : accounts) {
            System.out.println("  - " + account.getAccountType() + " (" + account.getAccountNumber() + "): $" + account.getBalance());
        }
    }

    private void viewTransactions() {
        Account account = selectAccount();
        List<Transaction> transactions = account.getTransactions();
        System.out.println("Transactions:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getType() + ": $" + transaction.getAmount() + " Status: " + transaction.getStatus());
        }
    }

    private void applyForLoan() {
        System.out.println("Enter loan amount:");
        double amount = Double.parseDouble(scanner.nextLine());
        Account account = selectAccount();
        Loan loan = new Loan(amount, account.getAccountNumber());
        customer.addLoan(loan);
        customerManager.addLoan(loan);
        System.out.println("Loan application submitted.");
    }

    private void viewLoans() {
        List<Loan> loans = customer.getLoans();
        System.out.println("Loans:");
        for (Loan loan : loans) {
            System.out.println("  - Loan Amount: $" + loan.getLoanAmount() + ", Interest Rate: " + loan.getInterestRate() + "%, Approved: " + (loan.isApproved() ? "Yes" : "No") + ", Paid Off: " + (loan.isPaidOff() ? "Yes" : "No"));
        }
    }

    private void payOffLoan() {
        System.out.println("Select a loan to pay off:");
        List<Loan> loans = customer.getLoans();
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.isApproved() && !loan.isPaidOff()) {
                System.out.println((i + 1) + ". Loan Amount: $" + loan.getLoanAmount() + ", Interest Rate: " + loan.getInterestRate() + "%");
            }
        }
        int choice = Integer.parseInt(scanner.nextLine());
        Loan loan = loans.get(choice - 1);
        Account account = selectAccount();
        try {
            account.withdraw(loan.getLoanAmount());
            loan.payOffLoan();
            customerManager.updateLoan(loan);
            accountManager.saveAccounts();
            System.out.println("Loan paid off successfully.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void openNewAccount() {
        System.out.println("Choose account type:");
        System.out.println("1. Savings");
        System.out.println("2. Checking");
        int choice = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter initial balance:");
        double balance = Double.parseDouble(scanner.nextLine());

        Account account;
        if (choice == 1) {
            account = new SavingsAccount(java.util.UUID.randomUUID().toString(), customer.getCustomerID(), balance);
        } else {
            account = new CheckingAccount(java.util.UUID.randomUUID().toString(), customer.getCustomerID(), balance);
        }

        customer.getAccountsList().add(account);
        accountManager.addAccount(account);
        System.out.println("New account created successfully.");
    }

    private Account selectAccount() {
        List<Account> accounts = customer.getAccountsList();
        System.out.println("Select an account:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i).getAccountType() + " (" + accounts.get(i).getAccountNumber() + "): $" + accounts.get(i).getBalance());
        }
        int choice = Integer.parseInt(scanner.nextLine());
        return accounts.get(choice - 1);
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
            System.out.println("Manager Dashboard: " + bankManager.getManagerId());
            System.out.println("1. Approve Transactions");
            System.out.println("2. Approve Loans");
            System.out.println("3. Add Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Generate Customer Report");
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
                    addCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 5:
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

    private void approveTransactions() {
        List<Transaction> pendingTransactions = transactionManager.getPendingTransactions();
        for (Transaction transaction : pendingTransactions) {
            System.out.println("Approve transaction: " + transaction.getType() + " of $" + transaction.getAmount() + " for account " + transaction.getSourceAccount().getAccountNumber() + "? (yes/no)");
            String approval = scanner.nextLine();
            if (approval.equalsIgnoreCase("yes")) {
                transactionManager.approveTransaction(transaction);
                System.out.println("Transaction approved.");
            } else {
                System.out.println("Transaction not approved.");
            }
        }
        transactionManager.saveTransactions();
    }

    private void approveLoans() {
        List<Loan> pendingLoans = customerManager.getPendingLoans();
        for (Loan loan : pendingLoans) {
            System.out.println("Approve loan: " + loan.getLoanAmount() + " for account " + loan.getAccountNumber() + "? (yes/no)");
            String approval = scanner.nextLine();
            if (approval.equalsIgnoreCase("yes")) {
                loan.approveLoan();
                Account account = accountManager.getAccount(loan.getAccountNumber());
                account.deposit(loan.getLoanAmount());
                System.out.println("Loan approved and funds deposited.");
            } else {
                System.out.println("Loan not approved.");
            }
        }
        customerManager.saveLoans();
        accountManager.saveAccounts();
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



















