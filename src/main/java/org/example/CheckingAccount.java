package org.example;

class CheckingAccount extends Account {
    private static double monthlyFee = 10.0;
    private static final double MIN_BALANCE = 100.00;

    public CheckingAccount(String accNum, double initDeposit) {
        super(accNum, initDeposit);
    }

    @Override
    public void deposit(double amount) {
        this.balance += amount;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (balance - amount < MIN_BALANCE) throw new InsufficientFundsException("Insufficient balance.");
        this.balance -= amount;
    }

    public void applyMonthlyFee() {
        this.balance -= monthlyFee;
    }
}
