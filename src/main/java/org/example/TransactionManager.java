package org.example;

import java.util.List;
import java.util.ArrayList;

import java.util.List;
import java.util.ArrayList;

public class TransactionManager {
    private List<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
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
}
