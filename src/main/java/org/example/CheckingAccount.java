package org.example;

public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = -500.00;

    public CheckingAccount(String accountNumber, String customerId, double balance, TransactionManager transactionManager) {
        super(accountNumber, customerId, balance, "Checking", transactionManager);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        try {
            if (getBalance() - amount < OVERDRAFT_LIMIT) {
                throw new InsufficientFundsException("Overdraft limit exceeded.");
            }
            super.withdraw(amount);
        } catch (InsufficientFundsException e) {
            System.err.println("Error during withdrawal: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error during withdrawal: " + e.getMessage());
        }
    }
}

