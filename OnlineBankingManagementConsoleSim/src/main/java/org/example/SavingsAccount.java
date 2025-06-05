package org.example;

/**
 * The SavingsAccount class represents a savings account in the banking system.
 * It extends the Account class and provides specific functionality for savings accounts and assuring the rules for the account aren't breached.
 *
 * @author Alessandro Pomponi
 */
public class SavingsAccount extends Account {
    private static final double MIN_BALANCE = 10.00;

    /**
     * Constructs a SavingsAccount with the specified details.
     *
     * @param accountNumber the unique identifier for the account
     * @param customerId the ID of the customer who owns the account
     * @param balance the initial balance of the account
     * @param transactionManager the transaction manager associated with the account
     */
    public SavingsAccount(String accountNumber, String customerId, double balance, TransactionManager transactionManager) {
        super(accountNumber, customerId, balance, "Savings", transactionManager);
    }

    /**
     * Validates the balance of the savings account.
     * Ensures that the balance is not less than $10.
     *
     * @throws IllegalArgumentException if the balance is less than $10
     */
    @Override
    public void validateBalance() throws IllegalArgumentException {
        if (getBalance() < MIN_BALANCE) {
            throw new IllegalArgumentException("Savings account balance cannot be less than $10.");
        }
    }
}

