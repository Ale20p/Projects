package org.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The TransactionManager class is responsible for managing transactions in the banking system.
 * It provides functionalities to log, load, and save transactions.
 *
 * @author Alessandro Pomponi
 */
public class TransactionManager {
    private List<Transaction> transactions;
    private String transactionsFilePath;
    private AccountManager accountManager;

    /**
     * Constructs a TransactionManager with the specified file path for transaction data and the account manager.
     *
     * @param transactionsFilePath the file path where transaction data is stored
     * @param accountManager the account manager for managing accounts associated with transactions
     */
    public TransactionManager(String transactionsFilePath, AccountManager accountManager) {
        this.transactionsFilePath = transactionsFilePath;
        this.accountManager = accountManager;
        this.transactions = new ArrayList<>();
        loadTransactions();
    }

    /**
     * Logs a transaction by adding it to the list and saving the updated list to the file.
     *
     * @param transaction the transaction to be logged
     */
    public void logTransaction(Transaction transaction) {
        try {
            transactions.add(transaction);
            saveTransactions();
        } catch (Exception e) {
            System.err.println("Error logging transaction: " + e.getMessage());
        }
    }

    /**
     * Loads transactions from the CSV file specified by the transactionsFilePath.
     */
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
                Date date = new Date(Long.parseLong(row[6]));  // Parse the date from the CSV
                Transaction transaction = new Transaction(transactionId, type, amount, sourceAccountNumber, destinationAccountNumber, status, date);
                transactions.add(transaction);

                // Link transaction to the source account
                Account sourceAccount = accountManager.getAccount(sourceAccountNumber);
                if (sourceAccount != null) {
                    sourceAccount.getTransactions().add(transaction);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing transaction data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error loading transactions: " + e.getMessage());
        }
    }

    /**
     * Saves the list of transactions to the CSV file specified by the transactionsFilePath.
     */
    public void saveTransactions() {
        List<String[]> data = new ArrayList<>();
        for (Transaction transaction : transactions) {
            data.add(new String[]{
                    transaction.getTransactionId(),
                    transaction.getType(),
                    String.valueOf(transaction.getAmount()),
                    transaction.getSourceAccountNumber(),
                    transaction.getDestinationAccountNumber(),
                    transaction.getStatus(),
                    String.valueOf(transaction.getDate().getTime())  // Save the date as a long
            });
        }
        try {
            CSVUtility.writeCSV(transactionsFilePath, data, false);
        } catch (Exception e) {
            System.err.println("Unexpected error saving transactions: " + e.getMessage());
        }
    }
}
