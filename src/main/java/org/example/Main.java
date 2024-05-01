package org.example;

import java.util.Scanner;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerManager customerManager = new CustomerManager();
        TransactionManager transactionManager = new TransactionManager();
        setupInitialData(customerManager, transactionManager);

        // Assuming manager credentials are set here. Update as necessary.
        BankManager bankManager = new BankManager("001", "manager_password", "Jane Doe", customerManager, transactionManager);

        System.out.println("Welcome to the Online Banking Management System!");
        while (true) {
            System.out.println("Type '1' for Customer, '2' for Bank Manager, '0' to Exit:");
            String userType = scanner.nextLine();

            if ("0".equals(userType)) {
                System.out.println("Exiting the system...");
                break;
            }

            if ("1".equals(userType)) {
                System.out.println("Enter Customer ID:");
                String customerId = scanner.nextLine();
                System.out.println("Enter password:");
                String password = scanner.nextLine();

                if (customerManager.authenticateCustomer(customerId, password)) {
                    Customer customer = customerManager.getCustomer(customerId);
                    CustomerUI customerUI = new CustomerUI(customer, scanner);
                    customerUI.displayDashboard();
                } else {
                    System.out.println("Invalid credentials for customer. Please try again.");
                }
            } else if ("2".equals(userType)) {
                System.out.println("Enter Manager ID:");
                String managerId = scanner.nextLine();
                System.out.println("Enter password:");
                String password = scanner.nextLine();

                if (bankManager.authenticateManager(managerId, password)) {
                    ManagerUI managerUI = new ManagerUI(bankManager, transactionManager, customerManager, scanner);
                    managerUI.displayDashboard();
                } else {
                    System.out.println("Invalid credentials for manager. Please try again.");
                }
            } else {
                System.out.println("Invalid option, please choose again.");
            }
        }
        scanner.close();
    }

    private static void setupInitialData(CustomerManager customerManager, TransactionManager transactionManager) {
        // Sample data for testing
        Customer customer1 = new Customer("101", "John Doe", "pass123");
        Account account1 = new SavingsAccount("101-001", 1500.00);
        customer1.addAccount(account1);

        Customer customer2 = new Customer("102", "Jane Smith", "pass456");
        Account account2 = new SavingsAccount("102-001", 2500.00);
        customer2.addAccount(account2);

        customerManager.addCustomer(customer1);
        customerManager.addCustomer(customer2);

        // Assume adding some transactions for testing
        Transaction tx1 = new Transaction("Deposit", 500, account1);
        transactionManager.addTransaction(tx1);
        account1.deposit(500); // Reflects deposit in account balance

        Transaction tx2 = new Transaction("Withdrawal", 200, account2);
        transactionManager.addTransaction(tx2);
        try {
            account2.withdraw(200); // Reflects withdrawal in account balance
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
