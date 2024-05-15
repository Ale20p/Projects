package org.example;

public class Transaction {
    private String transactionId;
    private String type;
    private double amount;
    private String accountNumber;
    private String status;  // New field to track transaction status

    public Transaction(String transactionId, String type, double amount, String accountNumber, String status) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.status = status;
    }

    // Getters and setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
