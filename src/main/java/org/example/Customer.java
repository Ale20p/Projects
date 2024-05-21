package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Customer class represents a customer in the banking system.
 * It provides functionalities to manage customer information, accounts, and loans.
 *
 * @author Alessandro Pomponi
 */
public class Customer {
    private String customerID;
    private String name;
    private String password;
    private String email;
    private List<Account> accounts;
    private List<Loan> loans;

    /**
     * Constructs a Customer with the specified name, password, and email.
     * A unique customer ID is generated automatically.
     *
     * @param name the name of the customer
     * @param password the password for the customer
     * @param email the email address of the customer
     */
    public Customer(String name, String password, String email) {
        this.customerID = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    /**
     * Constructs a Customer with the specified customer ID, name, password, and email.
     *
     * @param customerID the unique identifier for the customer
     * @param name the name of the customer
     * @param password the password for the customer
     * @param email the email address of the customer
     */
    public Customer(String customerID, String name, String password, String email) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    /**
     * Returns the customer ID.
     *
     * @return the customer ID
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * Returns the name of the customer.
     *
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the password of the customer.
     *
     * @return the password of the customer
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the email address of the customer.
     *
     * @return the email address of the customer
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the list of accounts associated with the customer.
     *
     * @return the list of accounts
     */
    public List<Account> getAccountsList() {
        return accounts;
    }

    /**
     * Returns the list of loans associated with the customer.
     *
     * @return the list of loans
     */
    public List<Loan> getLoans() {
        return loans;
    }

    /**
     * Adds an account to the customer's list of accounts.
     *
     * @param account the account to be added
     */
    public void addAccount(Account account) {
        try {
            accounts.add(account);
        } catch (Exception e) {
            System.err.println("Error adding account: " + e.getMessage());
        }
    }

    /**
     * Adds a loan to the customer's list of loans.
     *
     * @param loan the loan to be added
     */
    public void addLoan(Loan loan) {
        try {
            loans.add(loan);
        } catch (Exception e) {
            System.err.println("Error adding loan: " + e.getMessage());
        }
    }
}
