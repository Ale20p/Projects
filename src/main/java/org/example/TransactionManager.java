package org.example;

import java.util.List;
import java.util.ArrayList;

public class TransactionManager {
    private List<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        System.out.println("Transaction added: " + transaction.getTransactionId());
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public List<Transaction> getPendingTransactions() {
        List<Transaction> pendingTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (!transaction.isProcessed() && !transaction.isApproved()) {
                pendingTransactions.add(transaction);
            }
        }
        return pendingTransactions;
    }

    public void processTransactions() {
        for (Transaction transaction : transactions) {
            if (transaction.isApproved() && !transaction.isProcessed()) {
                try {
                    transaction.execute();
                    System.out.println("Transaction processed: " + transaction.getTransactionId());
                } catch (Exception e) {
                    System.out.println("Failed to process transaction: " + transaction.getTransactionId() + ", Reason: " + e.getMessage());
                }
            }
        }
    }
}
