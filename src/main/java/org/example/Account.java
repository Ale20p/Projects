package org.example;

abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected final static double MIN_BALANCE = 100.0;

    public Account(String accNum, double initDeposit) {
        this.accountNumber = accNum;
        this.balance = initDeposit;
    }

    public abstract void deposit(double amount);
    public abstract void withdraw(double amount) throws InsufficientFundsException;
    public double checkBalance() {
        return balance;
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
}

interface IAccountService {
    void createAccount(Customer customer, Account account);
    void deleteAccount(String accountNumber);
    Account getAccount(String accountNumber);
}