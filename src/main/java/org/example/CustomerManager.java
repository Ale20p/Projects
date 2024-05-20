package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {
    private List<Customer> customers;
    private String customersFilePath;
    private List<Loan> loans;
    private String loansFilePath;
    private AccountManager accountManager;

    public CustomerManager(String customersFilePath, String loansFilePath, AccountManager accountManager) {
        this.customersFilePath = customersFilePath;
        this.loansFilePath = loansFilePath;
        this.accountManager = accountManager;
        this.customers = new ArrayList<>();
        this.loans = new ArrayList<>();
        loadCustomers();
        loadLoans();
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomers();
    }

    public Customer authenticateCustomer(String email, String password) {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    public Customer getCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerID().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public boolean deleteCustomer(String customerId) {
        Customer customer = getCustomer(customerId);
        if (customer != null) {
            customers.remove(customer);
            accountManager.removeAccountsByCustomerId(customerId);
            saveCustomers();
            return true;
        }
        return false;
    }

    public String generateCustomerReport(String customerId) {
        Customer customer = getCustomer(customerId);
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
                        .append(transaction.getAmount()).append(" Status: ")
                        .append(transaction.getStatus()).append("\n");
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
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Loan> getLoans() {
        return loans;
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLoans() {
        Loan.saveLoans(loans, loansFilePath);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
        saveLoans();
    }

    public void updateLoan(Loan loan) {
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).getLoanId().equals(loan.getLoanId())) {
                loans.set(i, loan);
                break;
            }
        }
        saveLoans();
    }

    public Customer getCustomerByLoan(Loan loan) {
        for (Customer customer : customers) {
            for (Account account : customer.getAccountsList()) {
                if (account.getAccountNumber().equals(loan.getAccountNumber())) {
                    return customer;
                }
            }
        }
        return null;
    }

    public List<Loan> getPendingLoans() {
        List<Loan> pendingLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (!loan.isApproved()) {
                pendingLoans.add(loan);
            }
        }
        return pendingLoans;
    }

    public void approveLoan(Loan loan) {
        loan.setApproved(true);
        updateLoan(loan);
    }
}
