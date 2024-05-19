package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String customersFilePath = "path/to/customers.csv";
        String loansFilePath = "path/to/loans.csv";
        String accountsFilePath = "path/to/accounts.csv";
        String transactionsFilePath = "path/to/transactions.csv";

        AccountManager accountManager = new AccountManager(accountsFilePath);
        TransactionManager transactionManager = new TransactionManager(transactionsFilePath, accountManager);
        CustomerManager customerManager = new CustomerManager(customersFilePath, loansFilePath, accountManager);

        // Initialize a BankManager for demonstration purposes
        BankManager bankManager = new BankManager("admin", "admin123", customerManager);

        int option;
        do {
            System.out.println("Welcome to the Bank Management System");
            System.out.println("1. Customer Login");
            System.out.println("2. Manager Login");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    handleCustomerLogin(scanner, customerManager, accountManager, transactionManager);
                    break;
                case 2:
                    handleManagerLogin(scanner, bankManager, transactionManager, customerManager, accountManager);
                    break;
                case 0:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);

        scanner.close();
    }

    private static void handleCustomerLogin(Scanner scanner, CustomerManager customerManager, AccountManager accountManager, TransactionManager transactionManager) {
        System.out.println("Are you a new customer?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Choose an option: ");
        int option = Integer.parseInt(scanner.nextLine());

        if (option == 1) {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            Customer newCustomer = new Customer(name, password, email);
            customerManager.addCustomer(newCustomer);
            System.out.println("Customer registered successfully. Please log in.");
        }

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        Customer customer = customerManager.authenticateCustomer(email, password);
        if (customer != null) {
            CustomerUI customerUI = new CustomerUI(customer, scanner, customerManager, accountManager, transactionManager);
            customerUI.displayDashboard();
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    private static void handleManagerLogin(Scanner scanner, BankManager bankManager, TransactionManager transactionManager, CustomerManager customerManager, AccountManager accountManager) {
        System.out.print("Enter your manager ID: ");
        String managerId = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (bankManager.getManagerId().equals(managerId) && bankManager.authenticateManager(password)) {
            ManagerUI managerUI = new ManagerUI(bankManager, transactionManager, customerManager, accountManager, scanner);
            managerUI.displayDashboard();
        } else {
            System.out.println("Invalid manager ID or password. Please try again.");
        }
    }
}
