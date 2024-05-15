package org.example;

public class Loan {
    private double loanAmount;
    private double interestRate;
    private boolean isApproved;
    private boolean isPaidOff;

    public Loan(double loanAmount, double interestRate) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.isApproved = false;  // Initially, loans are not approved
        this.isPaidOff = false;   // Initially, loans are not paid off
    }

    // Method to approve the loan
    public void approveLoan() {
        this.isApproved = true;
    }

    // Method to pay off the loan
    public void payOffLoan() {
        if (isApproved && !isPaidOff) {
            this.isPaidOff = true;
            System.out.println("Loan of $" + loanAmount + " paid off.");
        } else {
            System.out.println("Loan cannot be paid off. Either it's not approved or already paid off.");
        }
    }

    // Getters and setters
    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public boolean isPaidOff() {
        return isPaidOff;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public void setPaidOff(boolean paidOff) {
        isPaidOff = paidOff;
    }
}
