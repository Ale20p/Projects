package org.example;

import java.util.ArrayList;
import java.util.List;

public interface Auditable {
    List<Transaction> getHighValueTransactions(double threshold);
}

class TransactionUtils {
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


class SortUtils {
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



