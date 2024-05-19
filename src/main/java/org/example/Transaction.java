package org.example;

public class Transaction {
    private String transactionId;
    private String type;
    private double amount;
    private Account account;
    private String status;

    public Transaction(String transactionId, String type, double amount, Account account) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.status = "Pending"; // Default status
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

    public String getStatus() {
        return status;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
