package org.example;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber, String customerId, double balance) {
        super(accountNumber, customerId, balance, "Savings");
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        super.withdraw(amount);
        Transaction transaction = new Transaction(java.util.UUID.randomUUID().toString(), "Withdrawal", amount, this);
        getTransactionManager().logTransaction(transaction);
        addTransaction(transaction);  // Add transaction to the account
    }

    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        Transaction transaction = new Transaction(java.util.UUID.randomUUID().toString(), "Deposit", amount, this);
        getTransactionManager().logTransaction(transaction);
        addTransaction(transaction);  // Add transaction to the account
    }

    @Override
    public void transfer(double amount, Account destinationAccount) throws InsufficientFundsException {
        if (this.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        super.withdraw(amount);
        destinationAccount.deposit(amount);
        Transaction transaction = new Transaction(java.util.UUID.randomUUID().toString(), "Transfer", amount, this);
        getTransactionManager().logTransaction(transaction);
        addTransaction(transaction);  // Add transaction to the account
    }
}
