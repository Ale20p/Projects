package org.example;

public class BankManager {
    private String managerId;
    private String name;
    private CustomerManager customerManager;

    public BankManager(String managerId, String name, CustomerManager customerManager) {
        this.managerId = managerId;
        this.name = name;
        this.customerManager = customerManager;
    }

    public void approveLargeWithdrawal(String customerId, double amount) {
        Customer customer = customerManager.getCustomer(customerId);
        if (customer != null && canApproveWithdrawal(customer, amount)) {
            System.out.println("Withdrawal approved for customer " + customerId);
            // Additional logic to process the approval
        } else {
            System.out.println("Withdrawal denied for customer " + customerId);
        }
    }

    private boolean canApproveWithdrawal(Customer customer, double amount) {
        // Define logic to determine if the withdrawal can be approved
        return true; // Simplified
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }
}

