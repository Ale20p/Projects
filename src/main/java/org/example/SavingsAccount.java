package org.example;

/**
 * The SavingsAccount class represents a savings account in the banking system.
 * It extends the Account class and provides specific functionality for savings accounts.
 *
 * @author Alessandro Pomponi
 */
public class SavingsAccount extends Account {

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
}

