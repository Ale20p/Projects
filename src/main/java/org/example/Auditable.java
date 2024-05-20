package org.example;

import java.util.ArrayList;
import java.util.List;

public interface Auditable {
    List<Transaction> getHighValueTransactions(double threshold);
}

class TransactionUtils {
    public static List<Transaction> binarySearchHighValueTransactions(List<Transaction> transactions, double threshold) {
        transactions.sort((t1, t2) -> Double.compare(t1.getAmount(), t2.getAmount()));

        int left = 0;
        int right = transactions.size() - 1;
        int mid;

        // Find the first transaction that is greater than or equal to the threshold
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (transactions.get(mid).getAmount() >= threshold) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        // Collect all transactions greater than or equal to the threshold
        List<Transaction> highValueTransactions = new ArrayList<>();
        for (int i = left; i < transactions.size(); i++) {
            if (transactions.get(i).getAmount() >= threshold) {
                highValueTransactions.add(transactions.get(i));
            }
        }
        return highValueTransactions;
    }
}

