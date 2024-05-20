package org.example;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction {
    private String transactionId;
    private String type;
    private double amount;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String status;
    private Date date;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Transaction(String type, double amount, String sourceAccountNumber, String destinationAccountNumber) {
        this.transactionId = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.status = "Completed";  // Default status to "Completed" for now
        this.date = new Date();  // Set the transaction date to the current date
    }

    public Transaction(String transactionId, String type, double amount, String sourceAccountNumber, String destinationAccountNumber, String status, Date date) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.status = status;
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormattedDate() {
        return DATE_FORMAT.format(date);
    }

    public static List<Transaction> loadTransactions(String transactionsFilePath) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            List<String[]> data = CSVUtility.readCSV(transactionsFilePath);
            for (String[] row : data) {
                String transactionId = row[0];
                String type = row[1];
                double amount = Double.parseDouble(row[2]);
                String sourceAccountNumber = row[3];
                String destinationAccountNumber = row[4];
                String status = row[5];
                Date date = new Date(Long.parseLong(row[6]));  // Parse the date from the CSV
                Transaction transaction = new Transaction(transactionId, type, amount, sourceAccountNumber, destinationAccountNumber, status, date);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static void saveTransactions(List<Transaction> transactions, String transactionsFilePath) {
        List<String[]> data = new ArrayList<>();
        for (Transaction transaction : transactions) {
            data.add(new String[]{
                    transaction.getTransactionId(),
                    transaction.getType(),
                    String.valueOf(transaction.getAmount()),
                    transaction.getSourceAccountNumber(),
                    transaction.getDestinationAccountNumber(),
                    transaction.getStatus(),
                    String.valueOf(transaction.getDate().getTime())  // Save the date as a long
            });
        }
        try {
            CSVUtility.writeCSV(transactionsFilePath, data, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
