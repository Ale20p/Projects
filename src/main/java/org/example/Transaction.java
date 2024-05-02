package org.example;

public class Transaction {
    private String transactionId;
    private String type;
    private double amount;
    private Account account;
    private boolean isProcessed;
    private boolean isApproved;

    public Transaction(String type, double amount, Account account) {
        this.transactionId = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.isProcessed = false;  // Transactions need approval
        this.isApproved = false;  // Default to not approved
    }

    public void approveTransaction() {
        this.isApproved = true;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public boolean isApproved() {
        return isApproved;
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
}
