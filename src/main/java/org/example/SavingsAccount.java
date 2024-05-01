package org.example;

public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 100.00;

    public SavingsAccount(String accountNumber, double initialBalance) {
        super(accountNumber, initialBalance);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }
        if (this.balance - amount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException("Withdrawal would put balance below minimum required.");
        }
        this.balance -= amount;
        transactions.add(new Transaction("Withdrawal", amount, this));
        System.out.println("Withdrawn: " + amount + ". New balance: " + this.balance);
    }
}

