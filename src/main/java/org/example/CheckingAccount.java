package org.example;

/**
 * The CheckingAccount class represents a checking account in the banking system.
 * It extends the Account class and provides additional functionality specific to checking accounts,
 * such as handling overdraft limits.
 *
 * @author Alessandro Pomponi
 */
public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = -500.00;

    /**
     * Constructs a CheckingAccount with the specified details.
     *
     * @param accountNumber the unique identifier for the account
     * @param customerId the ID of the customer who owns the account
     * @param balance the initial balance of the account
     * @param transactionManager the transaction manager associated with the account
     */
    public CheckingAccount(String accountNumber, String customerId, double balance, TransactionManager transactionManager) {
        super(accountNumber, customerId, balance, "Checking", transactionManager);
    }

    /**
     * Withdraws the specified amount from the account.
     * Ensures that the withdrawal does not exceed the overdraft limit.
     *
     * @param amount the amount to withdraw
     * @throws InsufficientFundsException if the withdrawal exceeds the overdraft limit
     */
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
