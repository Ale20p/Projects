package org.example;

class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accNum, double initDeposit, double rate) {
        super(accNum, initDeposit);
        this.interestRate = rate;
    }

    @Override
    public void deposit(double amount) {
        this.balance += amount;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (this.balance - amount < MIN_BALANCE)
            throw new InsufficientFundsException("Insufficient balance.");
        this.balance -= amount;
    }

    public void addInterest() {
        this.balance += this.balance * interestRate / 100;
    }
}
