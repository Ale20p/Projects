package org.example;

import java.io.IOException;
import java.util.*;

public class CustomerManager {
    private Map<String, Customer> customers;
    private TransactionManager transactionManager;

    public CustomerManager(TransactionManager transactionManager) {
        this.customers = new HashMap<>();
        this.transactionManager = transactionManager;
        try {
            loadCustomers();
            loadAccounts();
            loadLoans();
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    public void loadCustomers() throws IOException {
        List<String[]> data = CSVUtility.readCSV("customers.csv");
        customers.clear(); // Clear the current map before reloading
        for (String[] line : data) {
            if (line.length >= 4) {
                Customer customer = new Customer(line[0], line[1], line[2], line[3]);
                customers.put(customer.getCustomerID(), customer);
            }
        }
    }

    public void loadAccounts() throws IOException {
        List<String[]> data = CSVUtility.readCSV("accounts.csv");
        for (String[] line : data) {
            if (line.length >= 4) {
                String accountNumber = line[0];
                String customerId = line[1];
                double balance = Double.parseDouble(line[2]);
                String type = line[3];
                Account account;
                if ("Savings".equalsIgnoreCase(type)) {
                    account = new SavingsAccount(accountNumber, customerId, balance, transactionManager);
                } else {
                    account = new CheckingAccount(accountNumber, customerId, balance, transactionManager);
                }
                Customer customer = customers.get(customerId);
                if (customer != null) {
                    customer.addAccount(account);
                }
            }
        }
    }

    public void loadLoans() throws IOException {
        List<String[]> data = CSVUtility.readCSV("loans.csv");
        for (String[] line : data) {
            if (line.length >= 5) {
                String customerId = line[0];
                double amount = Double.parseDouble(line[1]);
                double interestRate = Double.parseDouble(line[2]);
                boolean approved = Boolean.parseBoolean(line[3]);
                boolean paidOff = Boolean.parseBoolean(line[4]);
                Loan loan = new Loan(amount, interestRate);
                loan.setApproved(approved);
                loan.setPaidOff(paidOff);
                Customer customer = customers.get(customerId);
                if (customer != null) {
                    customer.addLoan(loan);
                }
            }
        }
    }

    public void saveCustomers() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Customer customer : customers.values()) {
            data.add(new String[]{customer.getCustomerID(), customer.getName(), customer.getPassword(), customer.getEmail()});
        }
        CSVUtility.writeCSV("customers.csv", data, false);
        loadCustomers(); // Reload customers after saving
    }

    public void saveAccounts() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Customer customer : customers.values()) {
            for (Account account : customer.getAccountsList()) {
                data.add(new String[]{account.getAccountNumber(), account.getCustomerID(), String.valueOf(account.getBalance()), account.getAccountType()});
            }
        }
        CSVUtility.writeCSV("accounts.csv", data, false);
        loadAccounts(); // Reload accounts after saving
    }

    public void saveLoans() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Customer customer : customers.values()) {
            for (Loan loan : customer.getLoans()) {
                data.add(new String[]{customer.getCustomerID(), String.valueOf(loan.getLoanAmount()), String.valueOf(loan.getInterestRate()), String.valueOf(loan.isApproved()), String.valueOf(loan.isPaidOff())});
            }
        }
        CSVUtility.writeCSV("loans.csv", data, false);
        loadLoans(); // Reload loans after saving
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerID(), customer);
        try {
            saveCustomers();
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    public boolean deleteCustomer(String customerId) {
        if (customers.remove(customerId) != null) {
            try {
                saveCustomers();
                saveAccounts();
                saveLoans();
                return true;
            } catch (IOException e) {
                System.err.println("Error saving data: " + e.getMessage());
            }
        }
        return false;
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public Customer getCustomerByEmail(String email) {
        for (Customer customer : customers.values()) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        return null;
    }

    public List<Loan> getPendingLoans() {
        List<Loan> pendingLoans = new ArrayList<>();
        for (Customer customer : customers.values()) {
            for (Loan loan : customer.getLoans()) {
                if (!loan.isApproved() && !loan.isPaidOff()) {
                    pendingLoans.add(loan);
                }
            }
        }
        return pendingLoans;
    }

    public Customer getCustomerByLoan(Loan loan) {
        for (Customer customer : customers.values()) {
            if (customer.getLoans().contains(loan)) {
                return customer;
            }
        }
        return null;
    }

    public String generateCustomerReport(String customerId) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            return "Customer not found.";
        }
        StringBuilder report = new StringBuilder();
        report.append("Customer Report for ").append(customer.getName()).append(" (").append(customer.getCustomerID()).append(")\n");
        report.append("Email: ").append(customer.getEmail()).append("\n\n");
        report.append("Accounts:\n");
        for (Account account : customer.getAccountsList()) {
            report.append(account.getAccountType()).append(" - ").append(account.getAccountNumber()).append(": Balance $").append(account.getBalance()).append("\n");
        }
        report.append("\n");
        report.append("Loans:\n");
        for (Loan loan : customer.getLoans()) {
            report.append("Loan Amount: $").append(loan.getLoanAmount()).append(" Interest Rate: ").append(loan.getInterestRate()).append("% Approved: ").append(loan.isApproved() ? "Yes" : "No").append(" Paid Off: ").append(loan.isPaidOff() ? "Yes" : "No").append("\n");
        }
        report.append("\n");
        report.append("Transactions:\n");
        for (Account account : customer.getAccountsList()) {
            report.append("Account: ").append(account.getAccountNumber()).append("\n");
            for (Transaction transaction : account.getTransactions()) {
                report.append(transaction.getType()).append(": $").append(transaction.getAmount()).append(" Status: ").append(transaction.getStatus()).append("\n");
            }
            report.append("\n");
        }
        return report.toString();
    }

    // Generate a unique Customer ID
    public String generateCustomerId() {
        return UUID.randomUUID().toString();
    }
}
