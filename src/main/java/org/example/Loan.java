package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Loan {
    private String loanId;
    private double loanAmount;
    private double interestRate;
    private boolean approved;
    private boolean paidOff;
    private String accountNumber;

    public Loan(double loanAmount, String accountNumber) {
        this.loanId = java.util.UUID.randomUUID().toString();
        this.loanAmount = loanAmount;
        this.interestRate = 0.05; // Constant 5% interest rate
        this.accountNumber = accountNumber;
        this.approved = false;
        this.paidOff = false;
    }

    public String getLoanId() {
        return loanId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public boolean isApproved() {
        return approved;
    }

    public void approveLoan() {
        this.approved = true;
    }

    public boolean isPaidOff() {
        return paidOff;
    }

    public void payOffLoan() {
        this.paidOff = true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    // Static method to load loans from a CSV file
    public static List<Loan> loadLoans(String loansFilePath) {
        List<Loan> loans = new ArrayList<>();
        try {
            List<String[]> data = CSVUtility.readCSV(loansFilePath);
            for (String[] row : data) {
                String loanId = row[0];
                double loanAmount = Double.parseDouble(row[1]);
                double interestRate = Double.parseDouble(row[2]);
                boolean approved = Boolean.parseBoolean(row[3]);
                boolean paidOff = Boolean.parseBoolean(row[4]);
                String accountNumber = row[5];
                Loan loan = new Loan(loanAmount, accountNumber);
                loan.loanId = loanId;
                loan.approved = approved;
                loan.paidOff = paidOff;
                loans.add(loan);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loans;
    }

    // Static method to save loans to a CSV file
    public static void saveLoans(List<Loan> loans, String loansFilePath) {
        List<String[]> data = new ArrayList<>();
        for (Loan loan : loans) {
            data.add(new String[]{
                    loan.getLoanId(),
                    String.valueOf(loan.getLoanAmount()),
                    String.valueOf(loan.getInterestRate()),
                    String.valueOf(loan.isApproved()),
                    String.valueOf(loan.isPaidOff()),
                    loan.getAccountNumber()
            });
        }
        try {
            CSVUtility.writeCSV(loansFilePath, data, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
