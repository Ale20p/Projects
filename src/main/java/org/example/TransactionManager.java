package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionManager {
    private static final String TRANSACTIONS_FILE = "transactions.csv";
    private List<Transaction> transactions = new ArrayList<>();
    private AccountManager accountManager;

    public TransactionManager(AccountManager accountManager) {
        this.accountManager = accountManager;
        try {
            loadTransactions();
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

    private void loadTransactions() throws IOException {
        List<String[]> data = CSVUtility.readCSV(TRANSACTIONS_FILE);
        for (String[] line : data) {
            if (line.length >= 5) {  // Ensure there are enough elements in the line
                Transaction transaction = new Transaction(line[0], line[1], Double.parseDouble(line[2]), line[3], line[4]);
                transactions.add(transaction);
            }
        }
    }

    public void saveTransactions() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Transaction transaction : transactions) {
            data.add(new String[]{
                    transaction.getTransactionId(), transaction.getType(), String.valueOf(transaction.getAmount()),
                    transaction.getAccountNumber(), transaction.getStatus()
            });
        }
        CSVUtility.writeCSV(TRANSACTIONS_FILE, data, false); // Overwrite the existing file
    }

    public void logTransaction(Transaction transaction) {
        transactions.add(transaction);
        try {
            saveTransactions();  // Save the updated transactions to file
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public List<Transaction> getPendingTransactions() {
        List<Transaction> pendingTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if ("Pending".equalsIgnoreCase(transaction.getStatus())) {
                pendingTransactions.add(transaction);
            }
        }
        return pendingTransactions;
    }

    public void approveTransaction(String transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equals(transactionId) && "Pending".equalsIgnoreCase(transaction.getStatus())) {
                transaction.setStatus("Approved");
                Account account = accountManager.getAccount(transaction.getAccountNumber());
                if (account != null) {
                    if ("Deposit".equalsIgnoreCase(transaction.getType())) {
                        account.deposit(transaction.getAmount());
                    } else if ("Withdrawal".equalsIgnoreCase(transaction.getType())) {
                        try {
                            account.withdraw(transaction.getAmount());
                        } catch (InsufficientFundsException e) {
                            System.err.println("Insufficient funds for withdrawal: " + e.getMessage());
                        }
                    }
                }
                break;
            }
        }
        try {
            saveTransactions();  // Save the updated transactions to file
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }
}
