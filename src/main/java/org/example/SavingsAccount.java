package org.example;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, String customerId, double balance) {
        super(accountNumber, customerId, balance, "Savings");
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
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
        if (getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds for transfer.");
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
