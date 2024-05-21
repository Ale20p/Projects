package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private String customerID;
    private String name;
    private String password;
    private String email;
    private List<Account> accounts;
    private List<Loan> loans;

    public Customer(String name, String password, String email) {
        this.customerID = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public Customer(String customerID, String name, String password, String email) {
        this.customerID = customerID;
        this.name = name;
        this.password = password;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Account> getAccountsList() {
        return accounts;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void addAccount(Account account) {
        try {
            accounts.add(account);
        } catch (Exception e) {
            System.err.println("Error adding account: " + e.getMessage());
        }
    }

    public void addLoan(Loan loan) {
        try {
            loans.add(loan);
        } catch (Exception e) {
            System.err.println("Error adding loan: " + e.getMessage());
        }
    }

    public void payOffLoan(String loanId) {
        try {
            for (Loan loan : loans) {
                if (loan.getLoanId().equals(loanId) && loan.isApproved() && !loan.isPaidOff()) {
                    loan.payOffLoan();
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error paying off loan: " + e.getMessage());
        }
    }
}
