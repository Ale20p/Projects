package org.example;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        sortTransactions();  // Ensuring the list is sorted after each addition
    }

    public void executeTransactions() {
        for (Transaction transaction : transactions) {
            try {
                transaction.execute();
            } catch (TransactionException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }
        }
    }

    private void sortTransactions() {
        // Assuming Transaction has a comparable implementation based on the amount
        Collections.sort(transactions, (a, b) -> Double.compare(a.getAmount(), b.getAmount()));
    }

    public Transaction findTransactionByAmount(double amount) {
        int index = binarySearch(amount);
        if (index >= 0) {
            return transactions.get(index);
        }
        return null;
    }

    private int binarySearch(double amount) {
        int low = 0;
        int high = transactions.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Transaction midVal = transactions.get(mid);
            if (midVal.getAmount() < amount) {
                low = mid + 1;
            } else if (midVal.getAmount() > amount) {
                high = mid - 1;
            } else {
                return mid; // element found
            }
        }
        return -1; // element not found
    }
}

