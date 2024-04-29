package org.example;

public class Transaction {
    private String transactionId;
    private double amount;
    private Account account;  // Assume each transaction is linked to an Account
    private boolean isProcessed;
    private boolean isApproved;

    public Transaction(String transactionId, double amount, Account account) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.account = account;
        this.isProcessed = false;
        this.isApproved = false;
    }

    public void execute() throws Exception {
        if (!isApproved) {
            throw new Exception("Transaction not approved.");
        }
        account.withdraw(amount);  // Assuming this is a withdrawal for simplicity
        isProcessed = true;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public boolean isApproved() {
        return isApproved;
    }
}


