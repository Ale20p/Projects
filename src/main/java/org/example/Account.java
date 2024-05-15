package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Account {
    private String accountNumber;
    private String customerID;
    private double balance;
    private List<Transaction> transactions;
    private TransactionManager transactionManager;

    public Account(String accountNumber, String customerID, double balance, TransactionManager transactionManager) {
        this.accountNumber = accountNumber;
        this.customerID = customerID;
        this.balance = balance;
        this.transactions = new ArrayList<>();
        this.transactionManager = transactionManager;
    }

    public void deposit(double amount) {
        if (amount > 5000) {
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), "Deposit", amount, accountNumber, "Pending");
            transactions.add(transaction);
            transactionManager.logTransaction(transaction);
            System.out.println("Deposit of $" + amount + " is pending approval.");
        } else {
            balance += amount;
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), "Deposit", amount, accountNumber, "Approved");
            transactions.add(transaction);
            transactionManager.logTransaction(transaction);
            System.out.println("Deposit successful. New balance: $" + balance);
        }
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        if (amount > 5000) {
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), "Withdrawal", amount, accountNumber, "Pending");
            transactions.add(transaction);
            transactionManager.logTransaction(transaction);
            System.out.println("Withdrawal of $" + amount + " is pending approval.");
        } else {
            balance -= amount;
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), "Withdrawal", amount, accountNumber, "Approved");
            transactions.add(transaction);
            transactionManager.logTransaction(transaction);
            System.out.println("Withdrawal successful. New balance: $" + balance);
        }
    }

    public void transfer(double amount, Account destinationAccount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for transfer.");
        }
        if (amount > 5000) {
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), "Transfer", amount, accountNumber, "Pending");
            transactions.add(transaction);
            transactionManager.logTransaction(transaction);
            System.out.println("Transfer of $" + amount + " is pending approval.");
        } else {
            balance -= amount;
            destinationAccount.deposit(amount);
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), "Transfer", amount, accountNumber, "Approved");
            transactions.add(transaction);
            transactionManager.logTransaction(transaction);
            System.out.println("Transfer successful. New balance: $" + balance);
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerID() {
        return customerID;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getAccountType() {
        return this instanceof SavingsAccount ? "Savings" : "Checking";
    }
}





class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
