package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The Loan class represents a loan in the banking system.
 * It provides functionalities to manage loan details, such as amount, interest rate, approval status, and payment status.
 *
 * @author Alessandro Pomponi
 */
public class Loan {
    private String loanId;
    private double loanAmount;
    private static final double interestRate = 0.05;
    private boolean approved;
    private boolean paidOff;
    private String accountNumber;

    /**
     * Constructs a Loan with the specified loan amount and account number.
     * A unique loan ID is generated automatically, and the interest rate is set to a constant 5%.
     *
     * @param loanAmount the amount of the loan
     * @param accountNumber the account number associated with the loan
     */
    public Loan(double loanAmount, String accountNumber) {
        this.loanId = java.util.UUID.randomUUID().toString();
        this.loanAmount = loanAmount;
        this.accountNumber = accountNumber;
        this.approved = false;
        this.paidOff = false;
    }

    /**
     * Returns the loan ID.
     *
     * @return the loan ID
     */
    public String getLoanId() {
        return loanId;
    }

    /**
     * Returns the loan amount.
     *
     * @return the loan amount
     */
    public double getLoanAmount() {
        return loanAmount;
    }

    /**
     * Returns the interest rate of the loan.
     *
     * @return the interest rate
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Returns whether the loan is approved.
     *
     * @return true if the loan is approved, false otherwise
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Sets the approval status of the loan.
     *
     * @param approved the approval status to set
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Sets the loan ID.
     *
     * @param loanId the loan ID to set
     */
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    /**
     * Returns whether the loan is paid off.
     *
     * @return true if the loan is paid off, false otherwise
     */
    public boolean isPaidOff() {
        return paidOff;
    }

    /**
     * Sets the payment status of the loan.
     *
     * @param paidOff the payment status to set
     */
    public void setPaidOff(boolean paidOff) {
        this.paidOff = paidOff;
    }

    /**
     * Returns the account number associated with the loan.
     *
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Marks the loan as paid off.
     */
    public void payOffLoan() {
        this.paidOff = true;
    }

    /**
     * Saves the list of loans to the CSV file specified by the loansFilePath.
     *
     * @param loans the list of loans to save
     * @param loansFilePath the file path where loan data is stored
     */
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
        } catch (Exception e) {
            System.err.println("Unexpected error saving loans: " + e.getMessage());
        }
    }
}
