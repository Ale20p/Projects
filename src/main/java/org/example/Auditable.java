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

class SortUtils {
    public static void quickSortAccounts(List<Account> accounts, int low, int high) {
        if (low < high) {
            int pi = partition(accounts, low, high);
            quickSortAccounts(accounts, low, pi - 1);
            quickSortAccounts(accounts, pi + 1, high);
        }
    }

    private static int partition(List<Account> accounts, int low, int high) {
        double pivot = accounts.get(high).getBalance();
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (accounts.get(j).getBalance() > pivot) {  // Sorting in decreasing order
                i++;
                Account temp = accounts.get(i);
                accounts.set(i, accounts.get(j));
                accounts.set(j, temp);
            }
        }

        Account temp = accounts.get(i + 1);
        accounts.set(i + 1, accounts.get(high));
        accounts.set(high, temp);

        return i + 1;
    }
}


