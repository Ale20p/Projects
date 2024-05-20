package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Auditable{
    private String accountNumber;
    private String customerId;
    private double balance;
    private String accountType;
    private TransactionManager transactionManager;
    private List<Transaction> transactions;

    public Account(String accountNumber, String customerId, double balance, String accountType, TransactionManager transactionManager) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.accountType = accountType;
        this.transactionManager = transactionManager;
        this.transactions = new ArrayList<>();
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

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void deposit(double amount) {
        adjustBalance(amount);
        Transaction transaction = new Transaction("Deposit", amount, accountNumber, null);
        transactions.add(transaction);
        if (transactionManager != null) {
            transactionManager.logTransaction(transaction);
        }
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        adjustBalance(-amount);
        Transaction transaction = new Transaction("Withdrawal", amount, accountNumber, null);
        transactions.add(transaction);
        if (transactionManager != null) {
            transactionManager.logTransaction(transaction);
        }
    }

    public void transfer(double amount, Account destinationAccount) throws InsufficientFundsException {
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for transfer.");
        }
        adjustBalance(-amount);
        destinationAccount.deposit(amount);
        Transaction transaction = new Transaction("Transfer", amount, accountNumber, destinationAccount.getAccountNumber());
        transactions.add(transaction);
        if (transactionManager != null) {
            transactionManager.logTransaction(transaction);
        }
    }

    protected void adjustBalance(double amount) {
        balance += amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public List<Transaction> getHighValueTransactions(double threshold) {
        List<Transaction> highValueTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 5000.00) {
                highValueTransactions.add(transaction);
            }
        }
        return highValueTransactions;
    }
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
