package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private List<Transaction> transactions;
    private String transactionsFilePath;
    private AccountManager accountManager;

    public TransactionManager(String transactionsFilePath, AccountManager accountManager) {
        this.transactionsFilePath = transactionsFilePath;
        this.accountManager = accountManager;
        this.transactions = new ArrayList<>();
        loadTransactions();
    }

    public void logTransaction(Transaction transaction) {
        transactions.add(transaction);
        saveTransactions();
    }

    public void loadTransactions() {
        transactions.clear();
        try {
            List<String[]> data = CSVUtility.readCSV(transactionsFilePath);
            for (String[] row : data) {
                String transactionId = row[0];
                String type = row[1];
                double amount = Double.parseDouble(row[2]);
                String sourceAccountNumber = row[3];
                String destinationAccountNumber = row[4];
                String status = row[5];
                Transaction transaction = new Transaction(transactionId, type, amount, sourceAccountNumber, destinationAccountNumber, status);
                transactions.add(transaction);
                // Link transaction to the source account
                Account sourceAccount = accountManager.getAccount(sourceAccountNumber);
                if (sourceAccount != null) {
                    sourceAccount.getTransactions().add(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTransactions() {
        List<String[]> data = new ArrayList<>();
        for (Transaction transaction : transactions) {
            data.add(new String[]{
                    transaction.getTransactionId(),
                    transaction.getType(),
                    String.valueOf(transaction.getAmount()),
                    transaction.getSourceAccountNumber(),
                    transaction.getDestinationAccountNumber(),
                    transaction.getStatus()
            });
        }
        try {
            CSVUtility.writeCSV(transactionsFilePath, data, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
