package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Serializable {
    private String accountNumber;
    private String customerId;
    private double balance;
    private String accountType;
    private transient TransactionManager transactionManager;
    private List<Transaction> transactions;  // Add this field

    public Account(String accountNumber, String customerId, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.accountType = accountType;
        this.transactions = new ArrayList<>();  // Initialize the transactions list
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public List<Transaction> getTransactions() {
        return transactions;  // Return the transactions list
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        balance -= amount;
    }

    public abstract void transfer(double amount, Account destinationAccount) throws InsufficientFundsException;

    public void setBalance(double balance) {
        this.balance = balance;
    }
}







class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}


