package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String accountsFilePath = "accounts.csv";
        String transactionsFilePath = "transactions.csv";
        String customersFilePath = "customers.csv";
        String loansFilePath = "loans.csv";

        // Initialize AccountManager first
        AccountManager accountManager = new AccountManager(accountsFilePath);

        // Initialize TransactionManager with both transactionsFilePath and accountManager
        TransactionManager transactionManager = new TransactionManager(transactionsFilePath, accountManager);

        // Set the transactionManager for accountManager
        accountManager.setTransactionManager(transactionManager);

        // Initialize CustomerManager
        CustomerManager customerManager = new CustomerManager(accountManager);

        // Initialize BankManager
        BankManager bankManager = new BankManager("admin", "admin123", customerManager);

        // Initialize Scanner
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Bank Management System");
            System.out.println("1. Customer Login");
            System.out.println("2. Manager Login");
            System.out.println("0. Exit");
            System.out.println("Choose an option:");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    handleCustomerOption(scanner, customerManager, accountManager, transactionManager);
                    break;
                case 2:
                    handleManagerOption(bankManager, transactionManager, customerManager, accountManager, scanner);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void handleCustomerOption(Scanner scanner, CustomerManager customerManager, AccountManager accountManager, TransactionManager transactionManager) {
        System.out.println("Are you a new customer?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            System.out.println("Enter your email:");
            String email = scanner.nextLine();
            System.out.println("Enter your password:");
            String password = scanner.nextLine();
            Customer newCustomer = new Customer(java.util.UUID.randomUUID().toString(), name, password, email);
            customerManager.addCustomer(newCustomer);
            System.out.println("Customer account created. Please log in.");
        } else {
            System.out.println("Enter your email:");
            String email = scanner.nextLine();
            System.out.println("Enter your password:");
            String password = scanner.nextLine();
            Customer customer = customerManager.authenticateCustomer(email, password);
            if (customer != null) {
                CustomerUI customerUI = new CustomerUI(customer, scanner, customerManager, accountManager, transactionManager);
                customerUI.displayDashboard();
            } else {
                System.out.println("Invalid email or password.");
            }
        }
    }

    private static void handleManagerOption(BankManager bankManager, TransactionManager transactionManager, CustomerManager customerManager, AccountManager accountManager, Scanner scanner) {
        System.out.println("Enter manager ID:");
        String managerId = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        if (bankManager.getManagerId().equals(managerId) && bankManager.authenticateManager(password)) {
            ManagerUI managerUI = new ManagerUI(bankManager, transactionManager, customerManager, accountManager, scanner);
            managerUI.displayDashboard();
        } else {
            System.out.println("Invalid manager ID or password.");
        }
    }
}
