package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private List<Account> accounts;
    private String accountsFilePath;
    private TransactionManager transactionManager;

    public AccountManager(String accountsFilePath, TransactionManager transactionManager) {
        this.accountsFilePath = accountsFilePath;
        this.transactionManager = transactionManager;
        this.accounts = new ArrayList<>();
        loadAccounts();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        try {
            account.setTransactionManager(transactionManager);
            accounts.add(account);
            saveAccounts();
        } catch (Exception e) {
            System.err.println("Error adding account: " + e.getMessage());
        }
    }

    public Account getAccount(String accountNumber) {
        try {
            for (Account account : accounts) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting account: " + e.getMessage());
        }
        return null;
    }

    public List<Account> getAccountsByCustomerId(String customerId) {
        List<Account> customerAccounts = new ArrayList<>();
        try {
            for (Account account : accounts) {
                if (account.getCustomerId().equals(customerId)) {
                    customerAccounts.add(account);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting accounts by customer ID: " + e.getMessage());
        }
        return customerAccounts;
    }

    public void removeAccountsByCustomerId(String customerId) {
        try {
            accounts.removeIf(account -> account.getCustomerId().equals(customerId));
            saveAccounts();
        } catch (Exception e) {
            System.err.println("Error removing accounts by customer ID: " + e.getMessage());
        }
    }

    public void removeAccount(String accountNumber) {
        try {
            accounts.removeIf(account -> account.getAccountNumber().equals(accountNumber));
            saveAccounts();
        } catch (Exception e) {
            System.err.println("Error removing account: " + e.getMessage());
        }
    }

    public void loadAccounts() {
        accounts.clear();
        try {
            List<String[]> data = CSVUtility.readCSV(accountsFilePath);
            for (String[] row : data) {
                String accountNumber = row[0];
                String customerId = row[1];
                double balance = Double.parseDouble(row[2]);
                String accountType = row[3];
                Account account;
                if ("Savings".equalsIgnoreCase(accountType)) {
                    account = new SavingsAccount(accountNumber, customerId, balance, transactionManager);
                } else {
                    account = new CheckingAccount(accountNumber, customerId, balance, transactionManager);
                }
                accounts.add(account);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing account balance: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error loading accounts: " + e.getMessage());
        }
    }

    public void saveAccounts() {
        List<String[]> data = new ArrayList<>();
        for (Account account : accounts) {
            data.add(new String[]{
                    account.getAccountNumber(),
                    account.getCustomerId(),
                    String.valueOf(account.getBalance()),
                    account.getAccountType()
            });
        }
        try {
            CSVUtility.writeCSV(accountsFilePath, data, false);
        } catch (Exception e) {
            System.err.println("Unexpected error saving accounts: " + e.getMessage());
        }
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        for (Account account : accounts) {
            account.setTransactionManager(transactionManager);
        }
    }
}
