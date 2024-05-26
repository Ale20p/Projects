package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The Account class represents a generic bank account.
 * It provides common functionalities for account management, including deposits, withdrawals, and transfers.
 *
 * @author Alessandro Pomponi
 */
abstract class Account implements Auditable {

    private String accountNumber;
    private String customerId;
    private double balance;
    private String accountType;
    private TransactionManager transactionManager;
    private List<Transaction> transactions;

    /**
     * Constructs an Account with the specified details.
     *
     * @param accountNumber       the unique identifier for the account
     * @param customerId          the ID of the customer who owns the account
     * @param balance             the initial balance of the account
     * @param accountType         the type of the account (e.g., "Savings", "Checking")
     * @param transactionManager  the transaction manager associated with the account
     */
    public Account(String accountNumber, String customerId, double balance, String accountType, TransactionManager transactionManager) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.accountType = accountType;
        this.transactionManager = transactionManager;
        this.transactions = new ArrayList<>();
    }

    /**
     * Returns the account number.
     *
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Returns the customer ID.
     *
     * @return the customer ID
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Returns the current balance.
     *
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Returns the account type.
     *
     * @return the account type
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the transaction manager.
     *
     * @param transactionManager the transaction manager to set
     */
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Deposits the specified amount into the account.
     *
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if the amount is less than or equal to zero
     */
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

    /**
     * Withdraws the specified amount from the account.
     *
     * @param amount the amount to withdraw
     * @throws InsufficientFundsException if the balance is less than the amount to withdraw
     * @throws IllegalArgumentException if the amount is less than or equal to zero
     */
    public void withdraw(double amount) throws InsufficientFundsException {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive.");
            }
            if (balance < amount) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal.");
            }
            adjustBalance(-amount);
            validateBalance();
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

    /**
     * Transfers the specified amount to another account.
     *
     * @param amount the amount to transfer
     * @param destinationAccount the account to transfer to
     * @throws InsufficientFundsException if the balance is less than the amount to transfer
     * @throws IllegalArgumentException if the amount is less than or equal to zero
     */
    public void transfer(double amount, Account destinationAccount) throws InsufficientFundsException {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive.");
            }
            if (balance < amount) {
                throw new InsufficientFundsException("Insufficient funds for transfer.");
            }
            adjustBalance(-amount);
            validateBalance();
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

    /**
     * Adjusts the balance by the specified amount.
     *
     * @param amount the amount to adjust the balance by
     */
    protected void adjustBalance(double amount) {
        balance += amount;
    }

    /**
     * Returns the list of transactions.
     *
     * @return the list of transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Returns the list of high-value transactions that exceed the specified threshold.
     *
     * @param threshold the threshold amount
     * @return the list of high-value transactions
     * @throws IllegalArgumentException if the threshold is less than or equal to zero
     */
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

    /**
     * Validates the balance according to the account rules.
     * @throws IllegalArgumentException if the balance is invalid.
     */
    public abstract void validateBalance() throws IllegalArgumentException;
}

/**
 * Exception thrown when an account has insufficient funds for a transaction.
 */
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
