package org.example;

public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = -500.0;

    public CheckingAccount(String accountNumber, String customerID, double balance, TransactionManager transactionManager) {
        super(accountNumber, customerID, balance, transactionManager);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (getBalance() - amount < OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException("Withdrawal would exceed overdraft limit.");
        }
        super.withdraw(amount); // Call the parent class withdraw method
    }
}






