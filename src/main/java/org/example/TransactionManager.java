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

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        saveTransactions();
    }

    public List<Transaction> getTransactions() {
        return transactions;
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

    public void approveTransaction(Transaction transaction) {
        transaction.setStatus("Approved");
        Account sourceAccount = transaction.getSourceAccount();
        Account destinationAccount = transaction.getDestinationAccount();

        try {
            if ("Transfer".equalsIgnoreCase(transaction.getType()) && destinationAccount != null) {
                sourceAccount.withdraw(transaction.getAmount());
                destinationAccount.deposit(transaction.getAmount());
            } else if ("Withdrawal".equalsIgnoreCase(transaction.getType())) {
                sourceAccount.withdraw(transaction.getAmount());
            } else if ("Deposit".equalsIgnoreCase(transaction.getType())) {
                sourceAccount.deposit(transaction.getAmount());
            }
        } catch (InsufficientFundsException e) {
            e.printStackTrace();
        }

        saveTransactions();
        accountManager.saveAccounts();
    }

    public void loadTransactions() {
        transactions.clear();
        try {
            List<String[]> data = CSVUtility.readCSV(transactionsFilePath);
            for (String[] row : data) {
                String transactionId = row[0];
                String type = row[1];
                double amount = Double.parseDouble(row[2]);
                String accountNumber = row[3];
                String status = row[4];
                Account sourceAccount = accountManager.getAccount(accountNumber);
                Account destinationAccount = row.length > 5 ? accountManager.getAccount(row[5]) : null;
                Transaction transaction = new Transaction(type, amount, sourceAccount, destinationAccount);
                transaction.setStatus(status);
                transactions.add(transaction);
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
                    transaction.getSourceAccount().getAccountNumber(),
                    transaction.getStatus(),
                    transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getAccountNumber() : ""
            });
        }
        try {
            CSVUtility.writeCSV(transactionsFilePath, data, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logTransaction(Transaction transaction) {
        addTransaction(transaction);
    }
}
