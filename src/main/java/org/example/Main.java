package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerManager customerManager = new CustomerManager();
        TransactionManager transactionManager = new TransactionManager();
        BankManager bankManager = new BankManager("001", "manager_password", "Jane Doe", customerManager, transactionManager);

        // Setup initial data for testing
        setupInitialData(customerManager);

        System.out.println("Welcome to the Online Banking Management System!");
        while (true) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1: Login as Customer");
            System.out.println("2: Login as Bank Manager");
            System.out.println("0: Exit System");

            String choice = scanner.nextLine();
            if ("0".equals(choice)) {
                break; // Exit the loop and finish the program
            }

            switch (choice) {
                case "1":
                    handleCustomerLogin(scanner, customerManager);
                    break;
                case "2":
                    handleManagerLogin(scanner, bankManager, transactionManager);
                    break;
                default:
                    System.out.println("Invalid option selected. Please try again.");
            }
        }

        scanner.close();
        System.out.println("Thank you for using the Online Banking Management System.");
    }

    private static void handleCustomerLogin(Scanner scanner, CustomerManager customerManager) {
        System.out.println("Enter your Customer ID:");
        String customerId = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        if (customerManager.authenticateCustomer(customerId, password)) {
            Customer customer = customerManager.getCustomer(customerId);
            CustomerUI customerUI = new CustomerUI(customer, scanner);
            customerUI.displayDashboard();
        } else {
            System.out.println("Authentication failed. Check your credentials and try again.");
        }
    }

    private static void handleManagerLogin(Scanner scanner, BankManager bankManager, TransactionManager transactionManager) {
        System.out.println("Enter Manager ID:");
        String managerId = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        if (bankManager.authenticateManager(managerId, password)) {
            ManagerUI managerUI = new ManagerUI(bankManager, transactionManager, scanner);
            managerUI.displayDashboard();
        } else {
            System.out.println("Authentication failed. Check your credentials and try again.");
        }
    }

    private static void setupInitialData(CustomerManager customerManager) {
        Customer customer1 = new Customer("101", "John Doe", "pass123");
        Account account1 = new SavingsAccount("101-001", 1000.00);
        customer1.addAccount(account1);

        Customer customer2 = new Customer("102", "Jane Doe", "pass456");
        Account account2 = new SavingsAccount("102-001", 2500.00);
        customer2.addAccount(account2);

        customerManager.addCustomer(customer1);
        customerManager.addCustomer(customer2);
    }
}
