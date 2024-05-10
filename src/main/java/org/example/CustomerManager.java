package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CustomerManager {
    private Map<String, Customer> customers;

    public CustomerManager() {
        this.customers = new HashMap<>();
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerID(), customer);
        System.out.println("Customer added: " + customer.getName());
    }

    public void deleteCustomer(String customerId) {
        if (customers.remove(customerId) != null) {
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    public Customer getCustomer(String customerID) {
        return customers.get(customerID);
    }

    public boolean authenticateCustomer(String customerId, String password) {
        Customer customer = customers.get(customerId);
        return customer != null && customer.getPassword().equals(password);
    }

    public String generateCustomerReport(String customerId) {
        Customer customer = getCustomer(customerId);
        if (customer != null) {
            StringBuilder report = new StringBuilder();
            report.append("Customer ID: ").append(customer.getCustomerID()).append("\n");
            report.append("Name: ").append(customer.getName()).append("\n");
            report.append("Accounts and Transactions:\n");
            for (Account account : customer.getAccountsList()) {
                report.append("\tAccount Number: ").append(account.getAccountNumber())
                        .append(", Balance: $").append(String.format("%.2f", account.getBalance())).append("\n");
                for (Transaction transaction : account.getTransactions()) {
                    report.append("\t\tTransaction ID: ").append(transaction.getTransactionId())
                            .append(", Type: ").append(transaction.getType())
                            .append(", Amount: $").append(String.format("%.2f", transaction.getAmount()))
                            .append(", Approved: ").append(transaction.isApproved() ? "Yes" : "No").append("\n");
                }
            }
            return report.toString();
        }
        return "Customer not found.";
    }

    public List<Loan> getPendingLoans() {
        List<Loan> pendingLoans = new ArrayList<>();
        for (Customer customer : customers.values()) {
            for (Loan loan : customer.getLoans()) {
                if (!loan.isApproved()) {
                    pendingLoans.add(loan);
                }
            }
        }
        return pendingLoans;
    }

    // New method to find a customer by a specific loan
    public Customer getCustomerByLoan(Loan loan) {
        for (Customer customer : customers.values()) {
            if (customer.getLoans().contains(loan)) {
                return customer;
            }
        }
        return null;  // Return null if no customer is found with the given loan
    }
}
