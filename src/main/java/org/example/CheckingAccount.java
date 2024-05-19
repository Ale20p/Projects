package org.example;

public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = -500.0;

    public CheckingAccount(String accountNumber, String customerId, double balance) {
        super(accountNumber, customerId, balance, "Checking");
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (this.getBalance() - amount < OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException("Overdraft limit exceeded");
        }
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
        if (this.getBalance() - amount < OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException("Overdraft limit exceeded");
        }
        super.withdraw(amount);
        destinationAccount.deposit(amount);
        Transaction transaction = new Transaction(java.util.UUID.randomUUID().toString(), "Transfer", amount, this);
        getTransactionManager().logTransaction(transaction);
        addTransaction(transaction);  // Add transaction to the account
    }
}
