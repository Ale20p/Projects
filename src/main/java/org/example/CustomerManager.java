package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerManager {
    private Map<String, Customer> customers = new HashMap<>();
    private static final String CUSTOMER_FILE = "customers.csv";
    private static final String ACCOUNTS_FILE = "accounts.csv";

    public CustomerManager() {
        try {
            loadCustomers();
            loadAccounts();
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private void loadCustomers() throws IOException {
        List<String[]> data = CSVUtility.readCSV(CUSTOMER_FILE);
        for (String[] line : data) {
            if (line.length >= 3) {  // Ensure there are enough elements in the line
                Customer customer = new Customer(line[0], line[1], line[2]); // Assuming format: customerID, name, password
                customers.put(customer.getCustomerID(), customer);
            }
        }
    }

    private void loadAccounts() throws IOException {
        List<String[]> data = CSVUtility.readCSV(ACCOUNTS_FILE);
        for (String[] line : data) {
            if (line.length >= 4) {  // Ensure there are enough elements in the line
                String accountType = line[0];
                String accountNumber = line[1];
                String customerId = line[2];
                double balance = Double.parseDouble(line[3]);

                Customer customer = customers.get(customerId);
                if (customer != null) {
                    Account account;
                    if ("Savings".equalsIgnoreCase(accountType)) {
                        account = new SavingsAccount(accountNumber, customerId, balance);
                    } else {
                        account = new CheckingAccount(accountNumber, customerId, balance);
                    }
                    customer.addAccount(account);
                }
            }
        }
    }

    public void saveCustomers() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Customer customer : customers.values()) {
            data.add(new String[]{customer.getCustomerID(), customer.getName(), customer.getPassword()});
        }
        CSVUtility.writeCSV(CUSTOMER_FILE, data, false); // Overwrite the existing file
        saveAccounts();
    }

    public void saveAccounts() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Customer customer : customers.values()) {
            for (Account account : customer.getAccountsList()) {
                data.add(new String[]{
                        account.getAccountType(), account.getAccountNumber(), customer.getCustomerID(), String.valueOf(account.getBalance())
                });
            }
        }
        CSVUtility.writeCSV(ACCOUNTS_FILE, data, false); // Overwrite the existing file
    }

    public void addCustomer(Customer customer) {
        if (!customers.containsKey(customer.getCustomerID())) {
            customers.put(customer.getCustomerID(), customer);
            try {
                saveCustomers();
            } catch (IOException e) {
                System.err.println("Error saving customers: " + e.getMessage());
            }
        }
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public boolean deleteCustomer(String customerId) {
        if (customers.containsKey(customerId)) {
            customers.remove(customerId);
            try {
                saveCustomers();
                return true;
            } catch (IOException e) {
                System.err.println("Error deleting customer: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    // Additional methods for handling pending loans and generating reports

    public List<Loan> getPendingLoans() {
        return customers.values().stream()
                .flatMap(customer -> customer.getLoans().stream())
                .filter(loan -> !loan.isApproved())
                .collect(Collectors.toList());
    }

    public Customer getCustomerByLoan(Loan loan) {
        return customers.values().stream()
                .filter(customer -> customer.getLoans().contains(loan))
                .findFirst()
                .orElse(null);
    }

    public String generateCustomerReport(String customerId) {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            return "No customer found with ID: " + customerId;
        }
        StringBuilder report = new StringBuilder();
        report.append("Customer Report for ").append(customer.getName()).append(":\n");
        report.append("Accounts:\n");
        for (Account account : customer.getAccountsList()) {
            report.append(account.getAccountType()).append(" - ").append(account.getAccountNumber())
                    .append(": Balance $").append(account.getBalance()).append("\n");
        }
        report.append("Loans:\n");
        for (Loan loan : customer.getLoans()) {
            report.append("Loan Amount: $").append(loan.getLoanAmount())
                    .append(", Rate: ").append(loan.getInterestRate())
                    .append("%, Approved: ").append(loan.isApproved() ? "Yes" : "No").append("\n");
        }
        return report.toString();
    }
}
