package org.example;

public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 100.00; // Minimum balance required

    public SavingsAccount(String accountNumber, String customerID, double initialBalance) {
        super(accountNumber, customerID, initialBalance);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new InsufficientFundsException("Withdrawal amount must be positive.");
        }
        if (this.balance - amount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException("Insufficient funds: Withdrawal would put balance below minimum required.");
        }
        this.balance -= amount;
        this.transactions.add(new Transaction("Withdrawal", amount, this));
        System.out.println("Withdrawn: " + amount + ". New balance: " + this.balance);
    }
}


