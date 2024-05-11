package org.example;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerID;
    private String name;
    private String password;
    private List<Account> accounts;
    private List<Loan> loans;

    public Customer(String customerID, String name, String password) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Account> getAccountsList() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public List<Loan> getLoans() {
        return loans;
    }
}
