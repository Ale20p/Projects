package org.example;

public class Loan {
    private String loanId;
    private double loanAmount;
    private double annualInterestRate; // Annual interest rate as a percentage
    private Customer borrower;
    private double balance; // Current outstanding balance
    private boolean isActive; // Status of the loan

    public Loan(String loanId, double loanAmount, double annualInterestRate, Customer borrower) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.annualInterestRate = annualInterestRate;
        this.borrower = borrower;
        this.balance = loanAmount; // Initially, balance is equal to the loan amount
        this.isActive = true; // Loan starts as active
    }

    // Method to apply for a loan
    public void applyForLoan() {
        // Here you would include logic to check the customer's eligibility for the loan
        System.out.println("Loan applied for: " + loanAmount + " by " + borrower.getName());
        // For simplicity, we assume the loan is always approved
    }

    // Method to calculate monthly interest
    public double calculateMonthlyInterest() {
        if (!isActive) {
            return 0;
        }
        return (this.balance * (this.annualInterestRate / 100)) / 12;
    }

    // Method to make a repayment towards the loan
    public void makeRepayment(double amount) {
        if (!isActive) {
            System.out.println("Loan is not active. No repayment needed.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Repayment amount must be positive.");
            return;
        }
        this.balance -= amount;
        System.out.println("Repayment made: " + amount + ", Remaining Balance: " + this.balance);
        if (this.balance <= 0) {
            this.isActive = false;
            this.balance = 0;
            System.out.println("Loan " + this.loanId + " has been paid off.");
        }
    }

    // Method to get the current balance of the loan
    public double getBalance() {
        return this.balance;
    }

    // Method to check if the loan is active
    public boolean isActive() {
        return this.isActive;
    }

    // Utility method to print loan details
    public void printLoanDetails() {
        System.out.println("Loan ID: " + this.loanId +
                ", Borrower: " + this.borrower.getName() +
                ", Original Amount: " + this.loanAmount +
                ", Remaining Balance: " + this.balance +
                ", Status: " + (this.isActive ? "Active" : "Paid off"));
    }
}

