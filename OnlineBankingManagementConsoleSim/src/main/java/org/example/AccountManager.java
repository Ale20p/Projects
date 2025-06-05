package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The AccountManager class is responsible for managing the accounts in the banking system.
 * It provides functionalities to add, remove, and retrieve accounts, as well as to load and save account data.
 *
 * @author Alessandro Pomponi
 */
public class AccountManager {
    private List<Account> accounts;
    private String accountsFilePath;
    private TransactionManager transactionManager;

    /**
     * Constructs an AccountManager with the specified file path for account data and the transaction manager.
     *
     * @param accountsFilePath the file path where account data is stored
     * @param transactionManager the transaction manager for logging transactions
     */
    public AccountManager(String accountsFilePath, TransactionManager transactionManager) {
        this.accountsFilePath = accountsFilePath;
        this.transactionManager = transactionManager;
        this.accounts = new ArrayList<>();
        loadAccounts();
    }

    /**
     * Adds a new account to the list of accounts and saves the updated list to the file.
     *
     * @param account the account to be added
     */
    public void addAccount(Account account) {
        try {
            account.setTransactionManager(transactionManager);
            accounts.add(account);
            saveAccounts();
        } catch (Exception e) {
            System.err.println("Error adding account: " + e.getMessage());
        }
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber the account number
     * @return the account with the specified account number, or null if not found
     */
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

    /**
     * Retrieves a list of accounts associated with a specific customer ID.
     *
     * @param customerId the customer ID
     * @return the list of accounts associated with the customer ID
     */
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

    /**
     * Removes all accounts associated with a specific customer ID and saves the updated list to the file.
     *
     * @param customerId the customer ID
     */
    public void removeAccountsByCustomerId(String customerId) {
        try {
            accounts.removeIf(account -> account.getCustomerId().equals(customerId));
            saveAccounts();
        } catch (Exception e) {
            System.err.println("Error removing accounts by customer ID: " + e.getMessage());
        }
    }

    /**
     * Loads accounts from the CSV file specified by the accountsFilePath.
     */
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

    /**
     * Saves the current list of accounts to the CSV file specified by the accountsFilePath.
     */
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

    /**
     * Sets the transaction manager for all accounts.
     *
     * @param transactionManager the transaction manager to set
     */
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        for (Account account : accounts) {
            account.setTransactionManager(transactionManager);
        }
    }
}
