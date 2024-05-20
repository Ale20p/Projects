package org.example;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, String customerId, double balance, TransactionManager transactionManager) {
        super(accountNumber, customerId, balance, "Savings", transactionManager);
    }
}
