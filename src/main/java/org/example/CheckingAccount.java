package org.example;

public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = -500.00;

    public CheckingAccount(String accountNumber, String customerId, double balance) {
        super(accountNumber, customerId, balance, "Checking");
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (getBalance() - amount < OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException("Insufficient funds, overdraft limit reached.");
        }
        adjustBalance(-amount);
        Transaction transaction = new Transaction("Withdrawal", amount, this, null);
        getTransactions().add(transaction);
        if (getTransactionManager() != null) {
            getTransactionManager().logTransaction(transaction);
        }
    }

    @Override
    public void transfer(double amount, Account destinationAccount) throws InsufficientFundsException {
        if (getBalance() - amount < OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException("Insufficient funds, overdraft limit reached.");
        }
        adjustBalance(-amount);
        destinationAccount.deposit(amount);
        Transaction transaction = new Transaction("Transfer", amount, this, destinationAccount);
        getTransactions().add(transaction);
        if (getTransactionManager() != null) {
            getTransactionManager().logTransaction(transaction);
        }
    }
}
