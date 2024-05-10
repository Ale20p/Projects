package org.example;

import java.util.List;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected List<Transaction> transactions;  // Store transactions for this account

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // Abstract method to return the account type as a String
    public abstract String getAccountType();

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;  // Directly update the balance
            transactions.add(new Transaction("Deposit", amount, this));  // Log transaction
            System.out.println("Deposited: " + amount + ". New balance: " + this.balance);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public abstract void withdraw(double amount) throws InsufficientFundsException;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}



class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
