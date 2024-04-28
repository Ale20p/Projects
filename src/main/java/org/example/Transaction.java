package org.example;

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
}
