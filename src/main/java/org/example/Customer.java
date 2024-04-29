package org.example;

import java.io.*;
import java.util.*;

public class Customer {
    private String customerID;
    private String name;
    private String password;  // Password field
    private List<Account> accounts;

    public Customer(String customerID, String name, String password) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

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

    public List<Account> getAccountsList() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}
