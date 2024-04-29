package org.example;

public abstract class Account {
    protected String accountNumber;
    protected double balance;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // Method to deposit money into the account
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Deposited: " + amount + " New Balance: " + this.balance);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public abstract void withdraw(double amount) throws InsufficientFundsException;

    public double checkBalance() {
        return this.balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Utility method for printing the account details
    public void printAccountDetails() {
        System.out.println("Account Number: " + this.accountNumber + " Balance: " + this.balance);
    }
}

interface IAccountService {
    void createAccount(Customer customer, Account account);
    void deleteAccount(String accountNumber);
    Account getAccount(String accountNumber);
}