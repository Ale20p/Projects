package org.example;

public interface TransactionOperations {
    void execute() throws TransactionException;
    void reverse() throws TransactionException;

}

class TransactionException extends Exception {

    // Constructor that accepts only a message
    public TransactionException(String message) {
        super(message);
    }

    // Constructor that accepts both a message and a cause
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    // Optionally, a constructor that only accepts a cause can be added
    public TransactionException(Throwable cause) {
        super(cause);
    }
}
