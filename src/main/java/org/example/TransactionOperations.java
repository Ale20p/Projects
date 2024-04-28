package org.example;

public interface TransactionOperations {
    void execute() throws TransactionException;
    void reverse() throws TransactionException;

}
