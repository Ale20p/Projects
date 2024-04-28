package org.example;

public class Loan {
    private String loanId;
    private double loanAmount;
    private double interestRate;
    private Customer borrower;

    public Loan(String loanId, double amount, double rate, Customer borrower) {
        this.loanId = loanId;
        this.loanAmount = amount;
        this.interestRate = rate;
        this.borrower = borrower;
    }

    public void applyForLoan() {
        // Implementation for applying for a loan
    }

    public double calculateInterest() {
        return this.loanAmount * this.interestRate / 100;
    }

    public void makeRepayment(double amount) {
        this.loanAmount -= amount;
    }
}
