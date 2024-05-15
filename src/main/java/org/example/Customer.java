package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Customer {
    private String customerID;
    private String name;
    private String password;
    private String email;  // New email field
    private List<Account> accounts; // List to hold customer accounts
    private List<Loan> loans; // List to manage loans associated with the customer

    public Customer(String customerID, String name, String password, String email) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public boolean deleteAccount(String accountNumber) {
        Iterator<Account> it = accounts.iterator();
        while (it.hasNext()) {
            Account account = it.next();
            if (account.getAccountNumber().equals(accountNumber)) {
                it.remove();
                return true; // Account found and removed
            }
        }
        return false; // No account found with the given account number
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    // Getters and setters
    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Account> getAccountsList() {
        return accounts;
    }

    public List<Loan> getLoans() {
        return loans;
    }
}
