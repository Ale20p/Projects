package org.example;

import java.util.Scanner;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountManager accountManager = new AccountManager();
        CustomerManager customerManager = new CustomerManager();
        TransactionManager transactionManager = new TransactionManager(accountManager);
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
                    handleCustomerLogin(scanner, customerManager);
                    break;
                case "2":
                    handleManagerLogin(scanner, bankManager);
                    break;
                default:
                    System.out.println("Invalid option, please choose again.");
                    break;
            }
        }
    }

    private static void handleCustomerLogin(Scanner scanner, CustomerManager customerManager) {
        System.out.println("Enter Customer ID:");
        String customerId = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Customer customer = customerManager.getCustomer(customerId);
        if (customer != null && customer.getPassword().equals(password)) {
            CustomerUI customerUI = new CustomerUI(customer, scanner);
            customerUI.displayDashboard();
        } else {
            System.out.println("Invalid credentials for customer. Please try again.");
        }
    }

    private static void handleManagerLogin(Scanner scanner, BankManager bankManager) {
        System.out.println("Enter Manager ID:");
        String managerId = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        if (bankManager.authenticateManager(managerId, password)) {
//            ManagerUI managerUI = new ManagerUI(bankManager, scanner);
//            managerUI.displayDashboard();
        } else {
            System.out.println("Invalid credentials for manager. Please try again.");
        }
    }
}
