package org.example;

public class Transaction {
    private String transactionId;
    private String type;
    private double amount;
    private Account account;
    private boolean isProcessed;
    private boolean isApproved;

    public Transaction(String type, double amount, Account account) {
        this.transactionId = java.util.UUID.randomUUID().toString();  // Generate unique ID
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.isProcessed = true;  // Direct transactions are immediately processed
        this.isApproved = true;  // Assume approval for direct transactions
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Account getAccount() {
        return account;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public boolean isApproved() {
        return isApproved;
    }
}
