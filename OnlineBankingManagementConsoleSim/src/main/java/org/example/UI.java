package org.example;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * The UI interface defines a method for displaying the dashboard for different user types.
 */
public interface UI {
    /**
     * Displays the dashboard for the user.
     */
    void displayDashboard();
}

/**
 * The CustomerUI class implements the UI interface and provides the customer dashboard functionality.
 */
class CustomerUI implements UI {
    private Customer customer;
    private Scanner scanner;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private TransactionManager transactionManager;

    /**
     * Constructs a CustomerUI with the specified customer, scanner, customer manager, account manager, and transaction manager.
     *
     * @param customer the customer
     * @param scanner the scanner for user input
     * @param customerManager the customer manager
     * @param accountManager the account manager
     * @param transactionManager the transaction manager
     */
    public CustomerUI(Customer customer, Scanner scanner, CustomerManager customerManager, AccountManager accountManager, TransactionManager transactionManager) {
        this.customer = customer;
        this.scanner = scanner;
        this.customerManager = customerManager;
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
    }

    /**
     * Displays the customer dashboard and handles customer actions.
     */
    @Override
    public void displayDashboard() {
        int action = -1;
        do {
            System.out.println("Customer Dashboard: " + customer.getName());
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. View Transactions");
            System.out.println("5. Apply for Loan");
            System.out.println("6. View Loans");
            System.out.println("7. Open Account");
            System.out.println("8. View Accounts (Sorted by Balance)");
            System.out.println("9. Pay Off Loan");
            System.out.println("0. Logout");
            System.out.println("Choose an action:");
            try {
                action = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
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
                    viewAccounts();
                    break;
                case 9:
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

    /**
     * Displays and handles the view accounts action for the customer.
     */
    private void viewAccounts() {
        try {
            List<Account> accounts = accountManager.getAccountsByCustomerId(customer.getCustomerID());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
                return;
            }
            SortUtils.quickSortAccounts(accounts, 0, accounts.size() - 1);
            System.out.println("Accounts sorted by balance (decreasing order):");
            for (Account account : accounts) {
                System.out.println(account.getAccountType() + " (" + account.getAccountNumber() + "): $" + account.getBalance());
            }
        } catch (Exception e) {
            System.err.println("Error viewing accounts: " + e.getMessage());
        }
    }

    /**
     * Handles the deposit action for the customer.
     */
    private void deposit() {
        try {
            Account account = selectAccount();
            if (account == null) return;
            System.out.print("Enter amount to deposit: ");
            double amount = Double.parseDouble(scanner.nextLine());
            account.deposit(amount);
            accountManager.saveAccounts();
            transactionManager.saveTransactions();
            System.out.println("Deposit successful.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error during deposit: " + e.getMessage());
        }
    }

    /**
     * Handles the withdrawal action for the customer.
     */
    private void withdraw() {
        try {
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
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error during withdrawal: " + e.getMessage());
        }
    }

    /**
     * Handles the transfer action for the customer.
     */
    private void transfer() {
        try {
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
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error during transfer: " + e.getMessage());
        }
    }

    /**
     * Displays and handles the view transactions action for the customer.
     */
    private void viewTransactions() {
        try {
            Account account = selectAccount();
            if (account == null) return;
            List<Transaction> transactions = account.getTransactions();
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                SortUtils.mergeSortTransactions(transactions);  // Sort transactions by date in decreasing order
                System.out.println("Transactions:");
                for (Transaction transaction : transactions) {
                    System.out.println(transaction.getType() + ": $" + transaction.getAmount() + " Date: " + transaction.getFormattedDate() + " Status: " + transaction.getStatus());
                }
            }
        } catch (Exception e) {
            System.err.println("Error viewing transactions: " + e.getMessage());
        }
    }

    /**
     * Handles the apply for loan action for the customer.
     */
    private void applyForLoan() {
        try {
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
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error applying for loan: " + e.getMessage());
        }
    }

    /**
     * Displays and handles the view loans action for the customer.
     */
    private void viewLoans() {
        try {
            List<Loan> loans = customer.getLoans();
            if (loans.isEmpty()) {
                System.out.println("No loans found.");
            } else {
                for (Loan loan : loans) {
                    System.out.println("Loan Amount: $" + loan.getLoanAmount() + " Interest Rate: " + loan.getInterestRate() + "% Approved: " + (loan.isApproved() ? "Yes" : "No") + " Paid Off: " + (loan.isPaidOff() ? "Yes" : "No"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error viewing loans: " + e.getMessage());
        }
    }

    /**
     * Handles the open account action for the customer.
     */
    private void openAccount() {
        try {
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
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error opening account: " + e.getMessage());
        }
    }

    /**
     * Handles the pay-off loan action for the customer.
     */
    private void payOffLoan() {
        try {
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
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error paying off loan: " + e.getMessage());
        }
    }

    /**
     * Prompts the customer to select an account from their list of accounts.
     *
     * @return the selected account, or null if no account is selected
     */
    private Account selectAccount() {
        try {
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
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("Error selecting account: " + e.getMessage());
        }
        return null;
    }
}

/**
 * The ManagerUI class implements the UI interface and provides the manager dashboard functionality.
 * It allows the bank manager to perform various actions such as approving loans, adding customers,
 * deleting customers, generating customer reports, and viewing all customers.
 *
 * @author
 */
class ManagerUI implements UI {
    private BankManager bankManager;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private Scanner scanner;

    /**
     * Constructs a ManagerUI with the specified bank manager, customer manager, account manager, and scanner.
     *
     * @param bankManager the bank manager
     * @param customerManager the customer manager
     * @param accountManager the account manager
     * @param scanner the scanner for user input
     */
    public ManagerUI(BankManager bankManager, CustomerManager customerManager, AccountManager accountManager, Scanner scanner) {
        this.bankManager = bankManager;
        this.customerManager = customerManager;
        this.accountManager = accountManager;
        this.scanner = scanner;
    }

    /**
     * Displays the manager dashboard and handles manager actions.
     * The dashboard provides options for approving loans, adding customers, deleting customers,
     * generating customer reports, and viewing all customers.
     */
    @Override
    public void displayDashboard() {
        int action = -1;
        do {
            System.out.println("Manager Dashboard: " + bankManager.getManagerId());
            System.out.println("1. Approve Loans");
            System.out.println("2. Add Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. Generate Customer Report");
            System.out.println("5. View All Customers");  // New option
            System.out.println("0. Logout");
            System.out.println("Choose an action:");
            try {
                action = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
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
                    viewAllCustomers();  // New case
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

    /**
     * Approves pending loans for customers.
     * Prompts the manager to approve each pending loan. If approved, the loan amount is deposited into the
     * associated account.
     */
    private void approveLoans() {
        try {
            List<Loan> pendingLoans = customerManager.getPendingLoans();
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
        } catch (Exception e) {
            System.err.println("Error approving loans: " + e.getMessage());
        }
    }

    /**
     * Adds a new customer to the system.
     * Prompts the manager to enter the new customer's name, password, and email.
     */
    private void addCustomer() {
        try {
            System.out.print("Enter customer name: ");
            String name = scanner.nextLine();
            System.out.print("Enter customer password: ");
            String password = scanner.nextLine();
            System.out.print("Enter customer email: ");
            String email = scanner.nextLine();
            Customer newCustomer = new Customer(name, password, email);
            customerManager.addCustomer(newCustomer);
            System.out.println("Customer added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    /**
     * Deletes a customer from the system by their customer ID.
     * Prompts the manager to enter the customer ID of the customer to be deleted.
     */
    private void deleteCustomer() {
        try {
            System.out.print("Enter customer ID to delete: ");
            String customerId = scanner.nextLine();
            boolean success = customerManager.deleteCustomer(customerId);
            if (success) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("Customer not found.");
            }
        } catch (Exception e) {
            System.err.println("Error deleting customer: " + e.getMessage());
        }
    }

    /**
     * Generates a report for a customer by their customer ID.
     * Prompts the manager to enter the customer ID and displays the generated report.
     */
    private void generateCustomerReport() {
        try {
            System.out.print("Enter customer ID for report: ");
            String customerId = scanner.nextLine();
            String report = customerManager.generateCustomerReport(customerId);
            System.out.println(report);
        } catch (Exception e) {
            System.err.println("Error generating customer report: " + e.getMessage());
        }
    }

    /**
     * Displays the list of all customers.
     * Retrieves and displays the customer ID and name for each customer in the system.
     */
    private void viewAllCustomers() {  // New method
        try {
            List<Customer> customers = customerManager.getAllCustomers();
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
                return;
            }
            System.out.println("List of all customers:");
            for (Customer customer : customers) {
                System.out.println("Customer ID: " + customer.getCustomerID() + ", Name: " + customer.getName());
            }
        } catch (Exception e) {
            System.err.println("Error viewing customers: " + e.getMessage());
        }
    }
}

