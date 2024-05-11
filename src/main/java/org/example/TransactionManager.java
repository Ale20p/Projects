package org.example;

import java.io.IOException;
import java.util.*;

public class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();
    private static final String TRANSACTION_FILE = "transactions.csv";

    public TransactionManager() {
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
                Account account = findAccount(line[3]);  // This method needs to be implemented to find an account by accountNumber
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

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    private Account findAccount(String accountNumber) {
        // Implementation needed to return the Account object from an AccountManager or repository
        // This requires access to the AccountManager class or a similar repository that holds accounts
        return null;
    }

    // Additional methods to handle other transaction operations can be added as needed
}
