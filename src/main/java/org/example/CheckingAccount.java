package org.example;

import java.util.UUID;

public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = -500.00; // Example overdraft limit

    public CheckingAccount(String accountNumber, String customerID, double initialBalance) {
        super(accountNumber, customerID, initialBalance);
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new InsufficientFundsException("Withdrawal amount must be positive.");
        }
        if (this.balance - amount < OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException("Overdraft limit exceeded.");
        }
        this.balance -= amount;
        this.transactions.add(new Transaction(UUID.randomUUID().toString(), "Withdrawal", amount, this));  // Log transaction
        System.out.println("Withdrawn: " + amount + ". New balance: " + this.balance);
    }
}



