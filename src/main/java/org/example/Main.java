package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerManager customerManager = new CustomerManager();
        TransactionManager transactionManager = new TransactionManager();
        BankManager bankManager = new BankManager("001", "manager_password", "Jane Doe", customerManager, transactionManager);

        // Pre-create some sample customer
        customerManager.addCustomer(new Customer("123", "John Doe", "customer_password"));

        System.out.println("Welcome to the Online Banking Management System");

        while (true) {
            System.out.println("Type '1' for Customer, '2' for Bank Manager, '0' to Exit:");
            String userType = scanner.nextLine();

            if (userType.equals("0")) {
                break;
            }

            System.out.println("Please enter your ID:");
            String userId = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            if (userType.equals("1")) {  // Customer login
                if (customerManager.authenticateCustomer(userId, password)) {
                    Customer customer = customerManager.getCustomer(userId);
                    CustomerUI customerUI = new CustomerUI(customer, customerManager, transactionManager, scanner);
                    customerUI.displayDashboard();
                } else {
                    System.out.println("Invalid credentials for customer.");
                }
            } else if (userType.equals("2")) {  // Bank manager login
                if (bankManager.authenticateManager(userId, password)) {
                    ManagerUI managerUI = new ManagerUI(bankManager, transactionManager, scanner);
                    managerUI.displayDashboard();
                } else {
                    System.out.println("Invalid credentials for manager.");
                }
            } else {
                System.out.println("Invalid option, please choose again.");
            }
        }

        scanner.close();
        System.out.println("Thank you for using the Online Banking Management System.");
    }
}

