package org.example;

import java.util.List;
import java.util.ArrayList;

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

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }
        if (amount > 5000) {
            // Add to pending transactions requiring approval
            Transaction transaction = new Transaction("Deposit", amount, this);
            transactions.add(transaction);
            System.out.println("Deposit of " + amount + " is pending approval.");
        } else {
            this.balance += amount;
            transactions.add(new Transaction("Deposit", amount, this));
            System.out.println("Deposited: " + amount + ". New balance: " + this.balance);
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
