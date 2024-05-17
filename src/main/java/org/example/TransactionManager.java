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
        loadTransactions();
    }

    public void logTransaction(Transaction transaction) {
        transactions.add(transaction);
        try {
            saveTransactions();
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
                        account.setBalance(account.getBalance() + transaction.getAmount());
                    } else if ("Withdrawal".equalsIgnoreCase(transaction.getType())) {
                        account.setBalance(account.getBalance() - transaction.getAmount());
                    } else if (transaction.getType().startsWith("Transfer")) {
                        String[] parts = transaction.getType().split(" ");
                        String destinationAccountNumber = parts.length > 1 ? parts[1] : null;
                        if (destinationAccountNumber != null) {
                            Account destinationAccount = accountManager.getAccount(destinationAccountNumber);
                            if (destinationAccount != null) {
                                account.setBalance(account.getBalance() - transaction.getAmount());
                                destinationAccount.setBalance(destinationAccount.getBalance() + transaction.getAmount());
                            }
                        }
                    }
                }
                break;
            }
        }
        try {
            saveTransactions();
            accountManager.saveAccounts();
            accountManager.loadAccounts();
        } catch (IOException e) {
            System.err.println("Error saving transactions or accounts: " + e.getMessage());
        }
    }

    public void rejectTransaction(String transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equals(transactionId) && "Pending".equalsIgnoreCase(transaction.getStatus())) {
                transaction.setStatus("Rejected");
                break;
            }
        }
        try {
            saveTransactions();
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }
}
