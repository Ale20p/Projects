package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static final String TRANSACTIONS_FILE = "transactions.csv";
    private List<Transaction> transactions;
    private AccountManager accountManager;

    public TransactionManager(AccountManager accountManager) {
        this.transactions = new ArrayList<>();
        this.accountManager = accountManager;
        try {
            loadTransactions();
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void loadTransactions() throws IOException {
        List<String[]> data = CSVUtility.readCSV(TRANSACTIONS_FILE);
        transactions.clear();
        for (String[] line : data) {
            if (line.length >= 5) {
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
        CSVUtility.writeCSV(TRANSACTIONS_FILE, data, false);
    }

    public void logTransaction(Transaction transaction) {
        transactions.add(transaction);
        try {
            saveTransactions();
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
