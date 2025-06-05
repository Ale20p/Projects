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
     * Validates the balance of the checking account.
     * Ensures that the balance is not less than the overdraft limit of -$500.
     *
     * @throws IllegalArgumentException if the balance is less than -$500
     */
    @Override
    public void validateBalance() throws IllegalArgumentException {
        if (getBalance() < OVERDRAFT_LIMIT) {
            throw new IllegalArgumentException("Checking account balance cannot be less than -$500.");
        }
    }
}
