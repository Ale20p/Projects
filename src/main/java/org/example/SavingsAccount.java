package org.example;

class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 50.00;
    private double interestRate;

    public SavingsAccount(String accNum, double initDeposit, double rate) {
        super(accNum, initDeposit);
        this.interestRate = rate;
    }

    @Override
    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (this.balance - amount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException("Insufficient balance.");
        }
        setBalance(getBalance() - amount);
    }

    public void addInterest() {
        this.balance += this.balance * interestRate / 100;
    }
}
