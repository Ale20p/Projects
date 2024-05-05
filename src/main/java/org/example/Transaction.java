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
        this.isProcessed = false;  // Transactions start as not processed
        this.isApproved = false;  // Transactions over $5000 are not approved by default
    }

    // Method to mark the transaction as approved and processed
    public void approveTransaction() {
        this.isApproved = true;
        this.isProcessed = true;
        applyTransaction();
    }

    // Applies the transaction after approval
    private void applyTransaction() {
        if ("Deposit".equals(type)) {
            account.setBalance(account.getBalance() + amount);
        } else if ("Withdrawal".equals(type)) {
            account.setBalance(account.getBalance() - amount);
        }
        System.out.println("Transaction processed: " + type + " of $" + amount);
    }

    // Getters and setters
    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
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
}
