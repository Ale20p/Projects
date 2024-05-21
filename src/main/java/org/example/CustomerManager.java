package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The CustomerManager class is responsible for managing customers in the banking system.
 * It provides functionalities to add, remove, and retrieve customers, as well as to load and save customer and loan data.
 *
 * @author Alessandro Pomponi
 */
public class CustomerManager {
    private List<Customer> customers;
    private String customersFilePath;
    private List<Loan> loans;
    private String loansFilePath;
    private AccountManager accountManager;

    /**
     * Constructs a CustomerManager with the specified file paths for customer and loan data, and the account manager.
     *
     * @param customersFilePath the file path where customer data is stored
     * @param loansFilePath the file path where loan data is stored
     * @param accountManager the account manager for managing accounts associated with customers
     */
    public CustomerManager(String customersFilePath, String loansFilePath, AccountManager accountManager) {
        this.customersFilePath = customersFilePath;
        this.loansFilePath = loansFilePath;
        this.accountManager = accountManager;
        this.customers = new ArrayList<>();
        this.loans = new ArrayList<>();
        loadCustomers();
        loadLoans();
    }

    /**
     * Returns the list of all customers.
     *
     * @return the list of all customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * Adds a new customer to the list of customers and saves the updated list to the file.
     *
     * @param customer the customer to be added
     */
    public void addCustomer(Customer customer) {
        try {
            customers.add(customer);
            saveCustomers();
        } catch (Exception e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    /**
     * Authenticates a customer with the provided email and password.
     *
     * @param email the email of the customer
     * @param password the password of the customer
     * @return the authenticated customer, or null if authentication fails
     */
    public Customer authenticateCustomer(String email, String password) {
        try {
            for (Customer customer : customers) {
                if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                    return customer;
                }
            }
        } catch (Exception e) {
            System.err.println("Error during customer authentication: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a customer by their customer ID.
     *
     * @param customerId the customer ID
     * @return the customer with the specified customer ID, or null if not found
     */
    public Customer getCustomer(String customerId) {
        try {
            for (Customer customer : customers) {
                if (customer.getCustomerID().equals(customerId)) {
                    return customer;
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting customer: " + e.getMessage());
        }
        return null;
    }

    /**
     * Deletes a customer by their customer ID and removes their associated accounts.
     *
     * @param customerId the customer ID
     * @return true if the customer was successfully deleted, false otherwise
     */
    public boolean deleteCustomer(String customerId) {
        try {
            Customer customer = getCustomer(customerId);
            if (customer != null) {
                customers.remove(customer);
                accountManager.removeAccountsByCustomerId(customerId);
                saveCustomers();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error deleting customer: " + e.getMessage());
        }
        return false;
    }

    /**
     * Generates a report for a customer by their customer ID.
     *
     * @param customerId the customer ID
     * @return the generated customer report as a string, or an error message if the customer is not found
     */
    public String generateCustomerReport(String customerId) {
        try {
            Customer customer = linearSearchCustomerById(customerId);  // Use linear search
            if (customer == null) {
                return "Customer not found.";
            }
            StringBuilder report = new StringBuilder();
            report.append("Customer ID: ").append(customer.getCustomerID()).append("\n");
            report.append("Name: ").append(customer.getName()).append("\n");
            report.append("Email: ").append(customer.getEmail()).append("\n");
            report.append("Accounts:\n");
            for (Account account : customer.getAccountsList()) {
                report.append("  - ").append(account.getAccountType()).append(" (")
                        .append(account.getAccountNumber()).append("): $")
                        .append(account.getBalance()).append("\n");
                report.append("    Transactions:\n");
                for (Transaction transaction : account.getTransactions()) {
                    report.append("      - ").append(transaction.getType()).append(": $")
                            .append(transaction.getAmount()).append(" Date: ")
                            .append(transaction.getFormattedDate()).append(" Status: ")
                            .append(transaction.getStatus()).append("\n");
                }
                List<Transaction> highValueTransactions = account.getHighValueTransactions(5000.00);
                if (!highValueTransactions.isEmpty()) {
                    report.append("    High Value Transactions (>$5000):\n");
                    for (Transaction transaction : highValueTransactions) {
                        report.append("      - ").append(transaction.getType()).append(": $")
                                .append(transaction.getAmount()).append(" Date: ")
                                .append(transaction.getFormattedDate()).append(" Status: ")
                                .append(transaction.getStatus()).append("\n");
                    }
                }
            }
            report.append("Loans:\n");
            for (Loan loan : customer.getLoans()) {
                report.append("  - Loan Amount: $").append(loan.getLoanAmount())
                        .append(" Interest Rate: ").append(loan.getInterestRate())
                        .append("% Approved: ").append(loan.isApproved() ? "Yes" : "No")
                        .append(" Paid Off: ").append(loan.isPaidOff() ? "Yes" : "No").append("\n");
            }
            return report.toString();
        } catch (Exception e) {
            System.err.println("Error generating customer report: " + e.getMessage());
            return "Error generating customer report.";
        }
    }

    /**
     * Loads customers from the CSV file specified by the customersFilePath.
     */
    public void loadCustomers() {
        customers.clear();
        try {
            List<String[]> data = CSVUtility.readCSV(customersFilePath);
            for (String[] row : data) {
                String customerId = row[0];
                String name = row[1];
                String password = row[2];
                String email = row[3];
                Customer customer = new Customer(customerId, name, password, email);
                List<Account> customerAccounts = accountManager.getAccountsByCustomerId(customerId);
                customer.getAccountsList().addAll(customerAccounts);
                customers.add(customer);
            }
        } catch (Exception e) {
            System.err.println("Unexpected error loading customers: " + e.getMessage());
        }
    }

    /**
     * Saves the current list of customers to the CSV file specified by the customersFilePath.
     */
    public void saveCustomers() {
        List<String[]> data = new ArrayList<>();
        for (Customer customer : customers) {
            data.add(new String[]{
                    customer.getCustomerID(),
                    customer.getName(),
                    customer.getPassword(),
                    customer.getEmail()
            });
        }
        try {
            CSVUtility.writeCSV(customersFilePath, data, false);
        } catch (Exception e) {
            System.err.println("Unexpected error saving customers: " + e.getMessage());
        }
    }

    /**
     * Returns the list of all loans.
     *
     * @return the list of all loans
     */
    public List<Loan> getLoans() {
        return loans;
    }

    /**
     * Loads loans from the CSV file specified by the loansFilePath.
     */
    public void loadLoans() {
        loans.clear();
        try {
            List<String[]> data = CSVUtility.readCSV(loansFilePath);
            for (String[] row : data) {
                String loanId = row[0];
                double loanAmount = Double.parseDouble(row[1]);
                double interestRate = Double.parseDouble(row[2]);
                boolean approved = Boolean.parseBoolean(row[3]);
                boolean paidOff = Boolean.parseBoolean(row[4]);
                String accountNumber = row[5];
                Loan loan = new Loan(loanAmount, accountNumber);
                loan.setLoanId(loanId);
                loan.setApproved(approved);
                loan.setPaidOff(paidOff);
                loans.add(loan);
                // Link loan to the customer
                Customer customer = getCustomerByLoan(loan);
                if (customer != null) {
                    customer.getLoans().add(loan);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing loan data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error loading loans: " + e.getMessage());
        }
    }

    /**
     * Saves the current list of loans to the CSV file specified by the loansFilePath.
     */
    public void saveLoans() {
        try {
            Loan.saveLoans(loans, loansFilePath);
        } catch (Exception e) {
            System.err.println("Error saving loans: " + e.getMessage());
        }
    }

    /**
     * Adds a loan to the list of loans and saves the updated list to the file.
     *
     * @param loan the loan to be added
     */
    public void addLoan(Loan loan) {
        try {
            loans.add(loan);
            saveLoans();
        } catch (Exception e) {
            System.err.println("Error adding loan: " + e.getMessage());
        }
    }

    /**
     * Updates the details of an existing loan and saves the updated list to the file.
     *
     * @param loan the loan to be updated
     */
    public void updateLoan(Loan loan) {
        try {
            for (int i = 0; i < loans.size(); i++) {
                if (loans.get(i).getLoanId().equals(loan.getLoanId())) {
                    loans.set(i, loan);
                    break;
                }
            }
            saveLoans();
        } catch (Exception e) {
            System.err.println("Error updating loan: " + e.getMessage());
        }
    }

    /**
     * Retrieves the customer associated with a specific loan.
     *
     * @param loan the loan to find the associated customer
     * @return the customer associated with the loan, or null if not found
     */
    public Customer getCustomerByLoan(Loan loan) {
        try {
            for (Customer customer : customers) {
                for (Account account : customer.getAccountsList()) {
                    if (account.getAccountNumber().equals(loan.getAccountNumber())) {
                        return customer;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting customer by loan: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns the list of pending loans that have not been approved.
     *
     * @return the list of pending loans
     */
    public List<Loan> getPendingLoans() {
        List<Loan> pendingLoans = new ArrayList<>();
        try {
            for (Loan loan : loans) {
                if (!loan.isApproved()) {
                    pendingLoans.add(loan);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting pending loans: " + e.getMessage());
        }
        return pendingLoans;
    }

    /**
     * Approves a loan and updates its status in the list.
     *
     * @param loan the loan to be approved
     */
    public void approveLoan(Loan loan) {
        try {
            loan.setApproved(true);
            updateLoan(loan);
        } catch (Exception e) {
            System.err.println("Error approving loan: " + e.getMessage());
        }
    }

    /**
     * Performs a linear search to find a customer by their customer ID.
     *
     * @param customerId the customer ID to search for
     * @return the customer with the specified customer ID, or null if not found
     */
    private Customer linearSearchCustomerById(String customerId) {
        try {
            for (Customer customer : customers) {
                if (customer.getCustomerID().equals(customerId)) {
                    return customer;
                }
            }
        } catch (Exception e) {
            System.err.println("Error during linear search for customer ID: " + e.getMessage());
        }
        return null;
    }
}
