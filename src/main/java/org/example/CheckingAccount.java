package org.example;

public class CheckingAccount extends Account {

    public CheckingAccount(String accountNumber, double initialBalance) {
        super(accountNumber, initialBalance);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }
        if (this.balance - amount < 0) {
            throw new InsufficientFundsException("Insufficient funds for the withdrawal.");
        }
        this.balance -= amount;
        transactions.add(new Transaction("Withdrawal", amount, this));
        System.out.println("Withdrawn: " + amount + ". New balance: " + this.balance);
    }
}

