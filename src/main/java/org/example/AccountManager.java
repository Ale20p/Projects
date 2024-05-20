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
        account.setTransactionManager(transactionManager);
        accounts.add(account);
        saveAccounts();
    }

    public Account getAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public List<Account> getAccountsByCustomerId(String customerId) {
        List<Account> customerAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getCustomerId().equals(customerId)) {
                customerAccounts.add(account);
            }
        }
        return customerAccounts;
    }

    public void removeAccountsByCustomerId(String customerId) {
        accounts.removeIf(account -> account.getCustomerId().equals(customerId));
        saveAccounts();
    }

    public void removeAccount(String accountNumber) {
        accounts.removeIf(account -> account.getAccountNumber().equals(accountNumber));
        saveAccounts();
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
        } catch (IOException e) {
            e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        for (Account account : accounts) {
            account.setTransactionManager(transactionManager);
        }
    }
}
