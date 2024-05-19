package org.example;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerID;
    private String name;
    private String password;
    private String email;
    private List<Account> accountsList;
    private List<Loan> loans;

    // Constructor for new customer registration
    public Customer(String name, String password, String email) {
        this.customerID = java.util.UUID.randomUUID().toString();  // Generate a new unique ID for the customer
        this.name = name;
        this.password = password;
        this.email = email;
        this.accountsList = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    // Existing constructor for loading customers from CSV
    public Customer(String customerID, String name, String password, String email) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.email = email;
        this.accountsList = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Account> getAccountsList() {
        return accountsList;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }
}
