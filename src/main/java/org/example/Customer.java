package org.example;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerID;
    private String name;
    private String password;
    private String email;
    private List<Account> accounts;
    private List<Loan> loans;

    public Customer(String customerID, String name, String password, String email) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.email = email;
        this.accounts = new ArrayList<>();
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

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void clearAccounts() {
        accounts.clear();
    }

    public List<Account> getAccountsList() {
        return accounts;
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public boolean deleteAccount(String accountNumber) {
        return accounts.removeIf(account -> account.getAccountNumber().equals(accountNumber));
    }
}
