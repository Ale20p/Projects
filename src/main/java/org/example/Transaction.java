package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Transaction class represents a financial transaction in the banking system.
 * It provides functionalities to manage transaction details, such as type, amount, source and destination accounts, status, and date.
 *
 * @author Alessandro Pomponi
 */
public class Transaction {
    private String transactionId;
    private String type;
    private double amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String status;
    private Date date;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs a Transaction with the specified details.
     * The transaction ID is generated automatically, and the status is set to "Completed".
     *
     * @param type the type of the transaction (e.g., "Deposit", "Withdrawal", "Transfer")
     * @param amount the amount of the transaction
     * @param sourceAccountNumber the account number from which the transaction originates
     * @param destinationAccountNumber the account number to which the transaction is directed
     */
    public Transaction(String type, double amount, String sourceAccountNumber, String destinationAccountNumber) {
        this.transactionId = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.status = "Completed";  // Default status to "Completed" for now
        this.date = new Date();  // Set the transaction date to the current date
    }

    /**
     * Constructs a Transaction with the specified details.
     *
     * @param transactionId the unique identifier for the transaction
     * @param type the type of the transaction
     * @param amount the amount of the transaction
     * @param sourceAccountNumber the account number from which the transaction originates
     * @param destinationAccountNumber the account number to which the transaction is directed
     * @param status the status of the transaction (e.g., "Completed", "Pending")
     * @param date the date of the transaction
     */
    public Transaction(String transactionId, String type, double amount, String sourceAccountNumber, String destinationAccountNumber, String status, Date date) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.status = status;
        this.date = date;
    }

    /**
     * Returns the transaction ID.
     *
     * @return the transaction ID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Returns the type of the transaction.
     *
     * @return the type of the transaction
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the amount of the transaction.
     *
     * @return the amount of the transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the source account number.
     *
     * @return the source account number
     */
    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    /**
     * Returns the destination account number.
     *
     * @return the destination account number
     */
    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    /**
     * Returns the status of the transaction.
     *
     * @return the status of the transaction
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the date of the transaction.
     *
     * @return the date of the transaction
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the formatted date of the transaction as a string.
     *
     * @return the formatted date of the transaction
     */
    public String getFormattedDate() {
        return DATE_FORMAT.format(date);
    }
}
