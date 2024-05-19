package org.example;

public class Transaction {
    private String transactionId;
    private String type;
    private double amount;
    private Account sourceAccount;
    private Account destinationAccount;
    private String status;

    // Constructor for transactions with only a source account
    public Transaction(String type, double amount, Account sourceAccount) {
        this(type, amount, sourceAccount, null);
    }

    // Constructor for transactions with both source and destination accounts
    public Transaction(String type, double amount, Account sourceAccount, Account destinationAccount) {
        this.transactionId = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.status = "Pending"; // Default status for new transactions
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

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
