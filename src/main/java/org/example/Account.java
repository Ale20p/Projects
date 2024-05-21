package org.example;

import java.util.ArrayList;
import java.util.List;

abstract class Account implements Auditable {
    private String accountNumber;
    private String customerId;
    private double balance;
    private String accountType;
    private TransactionManager transactionManager;
    private List<Transaction> transactions;

    public Account(String accountNumber, String customerId, double balance, String accountType, TransactionManager transactionManager) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.accountType = accountType;
        this.transactionManager = transactionManager;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void deposit(double amount) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive.");
            }
            adjustBalance(amount);
            Transaction transaction = new Transaction("Deposit", amount, accountNumber, null);
            transactions.add(transaction);
            if (transactionManager != null) {
                transactionManager.logTransaction(transaction);
            }
        } catch (Exception e) {
            System.err.println("Error during deposit: " + e.getMessage());
        }
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive.");
            }
            if (balance < amount) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal.");
            }
            adjustBalance(-amount);
            Transaction transaction = new Transaction("Withdrawal", amount, accountNumber, null);
            transactions.add(transaction);
            if (transactionManager != null) {
                transactionManager.logTransaction(transaction);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error during withdrawal: " + e.getMessage());
            throw e;
        } catch (InsufficientFundsException e) {
            System.err.println("Insufficient funds: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error during withdrawal: " + e.getMessage());
        }
    }

    public void transfer(double amount, Account destinationAccount) throws InsufficientFundsException {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive.");
            }
            if (balance < amount) {
                throw new InsufficientFundsException("Insufficient funds for transfer.");
            }
            adjustBalance(-amount);
            destinationAccount.deposit(amount);
            Transaction transaction = new Transaction("Transfer", amount, accountNumber, destinationAccount.getAccountNumber());
            transactions.add(transaction);
            if (transactionManager != null) {
                transactionManager.logTransaction(transaction);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error during transfer: " + e.getMessage());
            throw e;
        } catch (InsufficientFundsException e) {
            System.err.println("Insufficient funds: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error during transfer: " + e.getMessage());
        }
    }

    protected void adjustBalance(double amount) {
        balance += amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public List<Transaction> getHighValueTransactions(double threshold) {
        try {
            if (threshold <= 0) {
                throw new IllegalArgumentException("Threshold must be positive.");
            }
            return TransactionUtils.binarySearchHighValueTransactions(transactions, threshold);
        } catch (Exception e) {
            System.err.println("Error during high-value transaction search: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}



class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
