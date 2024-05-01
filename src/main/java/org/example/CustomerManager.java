package org.example;

import java.util.*;

public class CustomerManager {
    private Map<String, Customer> customers = new HashMap<>();

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerID(), customer);
        System.out.println("Customer added: " + customer.getName());
    }

    public void deleteCustomer(String customerId) {
        if (customers.containsKey(customerId)) {
            customers.remove(customerId);
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    public void deleteAccount(String customerId, String accountNumber) {
        Customer customer = getCustomer(customerId);
        if (customer != null) {
            Iterator<Account> it = customer.getAccountsList().iterator();
            while (it.hasNext()) {
                Account account = it.next();
                if (account.getAccountNumber().equals(accountNumber)) {
                    it.remove();
                    System.out.println("Account " + accountNumber + " deleted successfully from customer " + customerId);
                    return;
                }
            }
            System.out.println("Account not found.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    public Customer getCustomer(String customerID) {
        return customers.get(customerID);
    }

    public Map<String, Customer> getAllCustomers() {
        return new HashMap<>(customers);
    }

    public boolean authenticateCustomer(String customerId, String password) {
        Customer customer = customers.get(customerId);
        return customer != null && customer.getPassword().equals(password);
    }

    public String generateCustomerReport(String customerId) {
        Customer customer = customers.get(customerId);
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
}
