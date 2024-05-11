package org.example;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();
    private static final String TRANSACTION_FILE = "transactions.csv";
    private AccountManager accountManager;  // Reference to AccountManager to access accounts

    public TransactionManager(AccountManager accountManager) {
        this.accountManager = accountManager;  // Initialize with an instance of AccountManager
        try {
            loadTransactions();
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

    private void loadTransactions() throws IOException {
        List<String[]> data = CSVUtility.readCSV(TRANSACTION_FILE);
        for (String[] line : data) {
            if (line.length >= 6) {  // Assuming format: transactionId, type, amount, accountNumber, isProcessed, isApproved
                Account account = findAccount(line[3]);
                if (account != null) {
                    Transaction transaction = new Transaction(line[0], line[1], Double.parseDouble(line[2]), account);
                    transaction.setProcessed(Boolean.parseBoolean(line[4]));
                    transaction.setApproved(Boolean.parseBoolean(line[5]));
                    transactions.add(transaction);
                }
            }
        }
    }

    public void saveTransactions() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Transaction transaction : transactions) {
            data.add(new String[]{
                    transaction.getTransactionId(), transaction.getType(), String.valueOf(transaction.getAmount()),
                    transaction.getAccount().getAccountNumber(), String.valueOf(transaction.isProcessed()), String.valueOf(transaction.isApproved())
            });
        }
        CSVUtility.writeCSV(TRANSACTION_FILE, data, false); // Overwrite the existing file
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        try {
            saveTransactions();
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public boolean approveTransaction(String transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equals(transactionId) && !transaction.isApproved()) {
                transaction.approveTransaction();
                try {
                    saveTransactions();
                    return true;
                } catch (IOException e) {
                    System.err.println("Error updating transaction approval: " + e.getMessage());
                }
            }
        }
        return false;
    }

    public List<Transaction> getPendingTransactions() {
        return transactions.stream()
                .filter(transaction -> !transaction.isApproved())
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    private Account findAccount(String accountNumber) {
        return accountManager.getAccount(accountNumber);  // Correct usage of the instance
    }

    // Additional methods for handling other transaction operations can be added as needed
}
