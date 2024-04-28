package org.example;

import java.util.List;

public class Transaction implements TransactionOperations {
    private String transactionId;
    private double amount;
    private Account account;

    public Transaction(String transactionId, double amount, Account account) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.account = account;
    }

    @Override
    public void execute() throws TransactionException {
        // Implementation for executing a transaction
    }

    @Override
    public void reverse() throws TransactionException {
        // Implementation for reversing a transaction
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

class TransactionException extends Exception {
    public TransactionException(String message) {
        super(message);
    }
}

interface ITransactionService {
    void processTransaction(Transaction transaction) throws TransactionException;
    List<Transaction> getTransactionsForAccount(String accountNumber);
}

