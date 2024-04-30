package org.example;

class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 100.00;
    private double interestRate;

    public SavingsAccount(String accNum, double initDeposit) {
        super(accNum, initDeposit);
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.println("Withdraw amount must be positive.");
            return;
        }
        if (this.balance - amount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException("Withdrawal would put balance below the minimum required balance of " + MINIMUM_BALANCE);
        }
        this.balance -= amount;
        System.out.println("Withdrawn: " + amount + " New Balance: " + this.balance);
    }

    public void addInterest() {
        this.balance += this.balance * interestRate / 100;
    }
}
