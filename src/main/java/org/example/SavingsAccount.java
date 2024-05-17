package org.example;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, String customerID, double balance, TransactionManager transactionManager) {
        super(accountNumber, customerID, balance, transactionManager);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        super.withdraw(amount); // Call the parent class withdraw method
    }
}


