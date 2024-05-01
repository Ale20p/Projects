package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class TransactionManager {
    private List<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getPendingTransactions() {
        List<Transaction> pending = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (!transaction.isProcessed() && !transaction.isApproved()) {
                pending.add(transaction);
            }
        }
        return pending;
    }

    public Transaction findTransactionById(String transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    public void approveTransaction(String transactionId) {
        Transaction transaction = findTransactionById(transactionId);
        if (transaction != null) {
            transaction.approveTransaction();
            transaction.setProcessed(true);
        }
    }
}

