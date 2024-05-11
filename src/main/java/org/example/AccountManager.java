package org.example;

import java.io.IOException;
import java.util.*;

public class AccountManager {
    private Map<String, Account> accounts = new HashMap<>();
    private static final String ACCOUNT_FILE = "accounts.csv";

    public AccountManager() {
        try {
            loadAccounts();
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
    }

    private void loadAccounts() throws IOException {
        List<String[]> data = CSVUtility.readCSV(ACCOUNT_FILE);
        for (String[] line : data) {
            if (line.length >= 4) {  // Ensure there are enough elements in the line
                Account account = createAccount(line[0], line[1], line[2], Double.parseDouble(line[3]));
                if (account != null) {
                    accounts.put(account.getAccountNumber(), account);
                }
            }
        }
    }

    private Account createAccount(String accountNumber, String customerID, String type, double balance) {
        switch (type) {
            case "Savings":
                return new SavingsAccount(accountNumber, customerID, balance);
            case "Checking":
                return new CheckingAccount(accountNumber, customerID, balance);
            default:
                System.err.println("Unknown account type: " + type);
                return null;
        }
    }

    public void saveAccounts() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (Account account : accounts.values()) {
            data.add(new String[]{account.getAccountNumber(), account.getCustomerID(), account.getClass().getSimpleName(), String.valueOf(account.getBalance())});
        }
        CSVUtility.writeCSV(ACCOUNT_FILE, data, false); // Overwrite the existing file
    }

    public void addAccount(Account account) {
        if (!accounts.containsKey(account.getAccountNumber())) {
            accounts.put(account.getAccountNumber(), account);
            try {
                saveAccounts();
            } catch (IOException e) {
                System.err.println("Error saving accounts: " + e.getMessage());
            }
        }
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public boolean deleteAccount(String accountNumber) {
        if (accounts.containsKey(accountNumber)) {
            accounts.remove(accountNumber);
            try {
                saveAccounts();
            } catch (IOException e) {
                System.err.println("Error saving accounts: " + e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean updateAccount(String accountNumber, String newType, double newBalance) {
        if (accounts.containsKey(accountNumber)) {
            Account account = accounts.get(accountNumber);
            if (account instanceof SavingsAccount && newType.equals("Savings") ||
                    account instanceof CheckingAccount && newType.equals("Checking")) {
                account.setBalance(newBalance);  // Assuming a setter method exists in the Account class
                try {
                    saveAccounts();
                } catch (IOException e) {
                    System.err.println("Error updating accounts: " + e.getMessage());
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    // Additional methods for handling other account operations can be added as needed
}
