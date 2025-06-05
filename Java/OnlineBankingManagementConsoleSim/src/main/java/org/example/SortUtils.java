package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The SortUtils class provides utility methods for sorting accounts and transactions.
 *
 * @author Alessandro Pomponi
 */
class SortUtils {
    /**
     * Sorts the list of accounts using quick sort in decreasing order of balance.
     *
     * @param accounts the list of accounts
     * @param low      the starting index
     * @param high     the ending index
     * @throws IllegalArgumentException if the indices are invalid
     */
    public static void quickSortAccounts(List<Account> accounts, int low, int high) {
        try {
            if (low < high) {
                int pi = partition(accounts, low, high);
                quickSortAccounts(accounts, low, pi - 1);
                quickSortAccounts(accounts, pi + 1, high);
            }
        } catch (Exception e) {
            System.err.println("Error during quick sort: " + e.getMessage());
        }
    }

    /**
     * Partitions the list of accounts for quick sort.
     *
     * @param accounts the list of accounts
     * @param low      the starting index
     * @param high     the ending index
     * @return the partitioning index
     * @throws IllegalArgumentException if the indices are invalid
     */
    private static int partition(List<Account> accounts, int low, int high) {
        try {
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
        } catch (Exception e) {
            System.err.println("Error during partitioning: " + e.getMessage());
            return -1;  // Return an invalid index to indicate an error
        }
    }

    /**
     * Sorts the list of transactions using merge sort in decreasing order by date.
     *
     * @param transactions the list of transactions
     * @throws IllegalArgumentException if the transactions list is null
     */
    public static void mergeSortTransactions(List<Transaction> transactions) {
        try {
            if (transactions.size() < 2) {
                return;
            }
            int mid = transactions.size() / 2;
            List<Transaction> left = new ArrayList<>(transactions.subList(0, mid));
            List<Transaction> right = new ArrayList<>(transactions.subList(mid, transactions.size()));
            mergeSortTransactions(left);
            mergeSortTransactions(right);
            merge(transactions, left, right);
        } catch (Exception e) {
            System.err.println("Error during merge sort: " + e.getMessage());
        }
    }

    /**
     * Merges two lists of transactions in decreasing order by date.
     *
     * @param transactions the list of transactions
     * @param left         the left sublist
     * @param right        the right sublist
     * @throws IllegalArgumentException if any of the lists are null
     */
    private static void merge(List<Transaction> transactions, List<Transaction> left, List<Transaction> right) {
        try {
            int i = 0, j = 0, k = 0;
            while (i < left.size() && j < right.size()) {
                if (left.get(i).getDate().after(right.get(j).getDate())) {
                    transactions.set(k++, left.get(i++));
                } else {
                    transactions.set(k++, right.get(j++));
                }
            }
            while (i < left.size()) {
                transactions.set(k++, left.get(i++));
            }
            while (j < right.size()) {
                transactions.set(k++, right.get(j++));
            }
        } catch (Exception e) {
            System.err.println("Error during merging: " + e.getMessage());
        }
    }
}
