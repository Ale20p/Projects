package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountManager {
    private Map<String, Account> accounts;
    private TransactionManager transactionManager;

    public AccountManager(TransactionManager transactionManager) {
        this.accounts = new HashMap<>();
        this.transactionManager = transactionManager;
        try {
            loadAccounts();
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        // Update transaction manager for all accounts
        for (Account account : accounts.values()) {
            account.setTransactionManager(transactionManager);
        }
    }

    private void loadAccounts() throws IOException {
        List<String[]> data = CSVUtility.readCSV("accounts.csv");
        for (String[] line : data) {
            if (line.length >= 4) {
                String accountNumber = line[0];
                String customerID = line[1];
                double balance = Double.parseDouble(line[2]);
                String type = line[3];

                Account account;
                if ("Savings".equalsIgnoreCase(type)) {
                    account = new SavingsAccount(accountNumber, customerID, balance, transactionManager);
                } else {
                    account = new CheckingAccount(accountNumber, customerID, balance, transactionManager);
                }

                accounts.put(accountNumber, account);
            }
        }
    }

    public void saveAccounts() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Account account : accounts.values()) {
            data.add(new String[]{account.getAccountNumber(), account.getCustomerID(), String.valueOf(account.getBalance()), account.getAccountType()});
        }
        CSVUtility.writeCSV("accounts.csv", data, false);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
        try {
            saveAccounts();
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    public boolean deleteAccount(String accountNumber) {
        if (accounts.remove(accountNumber) != null) {
            try {
                saveAccounts();
                return true;
            } catch (IOException e) {
                System.err.println("Error saving accounts: " + e.getMessage());
            }
        }
        return false;
    }
}
