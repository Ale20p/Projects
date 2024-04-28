package org.example;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String customerID;
    private String name;
    private Map<String, Account> accounts;

    public Customer(String customerID, String name) {
        this.customerID = customerID;
        this.name = name;
        this.accounts = new HashMap<>();
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void closeAccount(String accountNumber) {
        accounts.remove(accountNumber);
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public Collection<Account> getAccountsList() {
        return accounts.values();
    }
}

