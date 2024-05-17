package org.example;

import java.util.Scanner;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AccountManager accountManager = new AccountManager(null); // Temporarily pass null
        TransactionManager transactionManager = new TransactionManager(accountManager);
        accountManager = new AccountManager(transactionManager); // Reinitialize with the correct transaction manager

        CustomerManager customerManager = new CustomerManager(transactionManager);

        BankManager bankManager = new BankManager("001", "manager_password", "Jane Doe", customerManager, transactionManager);

        System.out.println("Welcome to the Online Banking Management System!");
        while (true) {
            System.out.println("Type '1' for Customer, '2' for Bank Manager, '0' to Exit:");
            String userType = scanner.nextLine();
            switch (userType) {
                case "0":
                    System.out.println("Exiting the system...");
                    scanner.close();
                    return;
                case "1":
                    handleCustomer(scanner, customerManager, transactionManager);
                    break;
                case "2":
                    handleManagerLogin(scanner, bankManager, transactionManager);
                    break;
                default:
                    System.out.println("Invalid option, please choose again.");
                    break;
            }
        }
    }

    private static void handleCustomer(Scanner scanner, CustomerManager customerManager, TransactionManager transactionManager) {
        System.out.println("Are you a new customer? (yes/no)");
        String response = scanner.nextLine();
        if ("yes".equalsIgnoreCase(response)) {
            registerNewCustomer(scanner, customerManager);
        } else {
            handleCustomerLogin(scanner, customerManager, transactionManager);
        }
    }

    private static void registerNewCustomer(Scanner scanner, CustomerManager customerManager) {
        System.out.println("Registering new customer.");
        System.out.println("Enter Customer ID:");
        String id = scanner.nextLine();
        System.out.println("Enter Customer Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Customer Password:");
        String password = scanner.nextLine();
        System.out.println("Enter Customer Email:");
        String email = scanner.nextLine();
        Customer newCustomer = new Customer(id, name, password, email);
        customerManager.addCustomer(newCustomer);
        System.out.println("Customer registered successfully.");
    }

    private static void handleCustomerLogin(Scanner scanner, CustomerManager customerManager, TransactionManager transactionManager) {
        System.out.println("Enter Customer Email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        Customer customer = customerManager.getCustomerByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            CustomerUI customerUI = new CustomerUI(customer, scanner, customerManager, transactionManager);
            customerUI.displayDashboard();
        } else {
            System.out.println("Invalid credentials for customer. Please try again.");
        }
    }

    private static void handleManagerLogin(Scanner scanner, BankManager bankManager, TransactionManager transactionManager) {
        System.out.println("Enter Manager ID:");
        String managerId = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        if (bankManager.authenticateManager(managerId, password)) {
            ManagerUI managerUI = new ManagerUI(bankManager, transactionManager, bankManager.getCustomerManager(), scanner);
            managerUI.displayDashboard();
        } else {
            System.out.println("Invalid credentials for manager. Please try again.");
        }
    }
}
