package org.example;

public class Loan {
    private double loanAmount;
    private double interestRate;
    private boolean isApproved;

    public Loan(double loanAmount, double interestRate) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.isApproved = false;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public boolean isApproved() {
        return isApproved;
    }
}
