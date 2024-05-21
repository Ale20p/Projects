package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The TransactionUtils class provides utility methods for handling transactions.
 *
 * @author Alessandro Pomponi
 */
class TransactionUtils {
    /**
     * Returns a list of high-value transactions that exceed the specified threshold using binary search.
     *
     * @param transactions the list of transactions
     * @param threshold    the threshold amount
     * @return the list of high-value transactions
     * @throws IllegalArgumentException if the threshold is less than or equal to zero
     */
    public static List<Transaction> binarySearchHighValueTransactions(List<Transaction> transactions, double threshold) {
        List<Transaction> highValueTransactions = new ArrayList<>();
        try {
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
            for (int i = left; i < transactions.size(); i++) {
                if (transactions.get(i).getAmount() >= threshold) {
                    highValueTransactions.add(transactions.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Error during binary search for high-value transactions: " + e.getMessage());
        }
        return highValueTransactions;
    }
}
