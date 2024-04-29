package org.example;

import java.util.Date;
import java.util.List;

public class Transaction implements TransactionOperations {
    private String transactionId;
    private double amount;
    private Account account;
    private Date transactionDate;

    public Transaction(String transactionId, double amount, Account account) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.account = account;
        this.transactionDate = new Date(); // Current date and time
    }

    @Override
    public void execute() throws TransactionException {
        try {
            account.withdraw(amount); // Assume it's a withdrawal for simplicity
        } catch (InsufficientFundsException e) {
            throw new TransactionException("Failed to execute transaction: " + e.getMessage());
        }
    }

    @Override
    public void reverse() throws TransactionException {
        // Implementation for reversing a transaction
    }

    public static int findTransactionByAmount(Transaction[] transactions, double amount) {
        int index = binarySearch(transactions, amount, 0, transactions.length - 1);
        return index; // Returns -1 if not found
    }

    private static int binarySearch(Transaction[] arr, double target, int low, int high) {
        while (low <= high) {
            int mid = low + (high - low) / 2;
            double midVal = arr[mid].getAmount();
            if (midVal < target) {
                low = mid + 1;
            } else if (midVal > target) {
                high = mid - 1;
            } else {
                return mid; // target found
            }
        }
        return -1; // target not found
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

