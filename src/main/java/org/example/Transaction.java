package org.example;

public class Transaction {
    private final String transactionId;  // Make transactionId immutable
    private final String type;
    private final double amount;
    private final Account account;
    private boolean isProcessed;
    private boolean isApproved;

    // Updated constructor that includes transactionId
    public Transaction(String transactionId, String type, double amount, Account account) {
        this.transactionId = transactionId;
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

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
