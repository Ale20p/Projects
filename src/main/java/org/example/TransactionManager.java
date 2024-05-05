package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionManager {
    private List<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getPendingTransactions() {
        // Only returns transactions that are greater than $5000 and not yet processed
        return transactions.stream()
                .filter(t -> !t.isProcessed() && t.getAmount() > 5000)
                .collect(Collectors.toList());
    }

    public void approveTransaction(String transactionId) {
        Transaction transaction = findTransactionById(transactionId);
        if (transaction != null && !transaction.isProcessed()) {
            transaction.approveTransaction();
            // Apply the transaction if it involves financial changes such as deposits or withdrawals
            applyTransaction(transaction);
            System.out.println("Transaction approved and processed.");
        } else {
            System.out.println("Transaction not found or already processed.");
        }
    }

    private Transaction findTransactionById(String transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    private void applyTransaction(Transaction transaction) {
        if ("Deposit".equals(transaction.getType())) {
            transaction.getAccount().setBalance(transaction.getAccount().getBalance() + transaction.getAmount());
        } else if ("Withdrawal".equals(transaction.getType())) {
            try {
                transaction.getAccount().withdraw(transaction.getAmount()); // This could potentially throw an exception
            } catch (InsufficientFundsException e) {
                System.out.println("Error processing withdrawal: " + e.getMessage());
            }
        }
    }
}
