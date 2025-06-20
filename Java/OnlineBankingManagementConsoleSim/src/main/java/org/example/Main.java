package org.example;

import java.util.Scanner;

/**
 * The Main class serves as the entry point for the Bank Management System application.
 * It provides functionalities to handle customer and manager logins, and initializes
 * the necessary components for the system.
 *
 * @author Alessandro Pomponi
 */
public class Main {

    /**
     * The main method initializes the bank management system and handles the main menu
     * for customer and manager logins.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String customersFilePath = "customers.csv";
        String loansFilePath = "loans.csv";
        String accountsFilePath = "accounts.csv";
        String transactionsFilePath = "transactions.csv";

        try {
            AccountManager accountManager = new AccountManager(accountsFilePath, null);
            TransactionManager transactionManager = new TransactionManager(transactionsFilePath, accountManager);
            accountManager.setTransactionManager(transactionManager);
            CustomerManager customerManager = new CustomerManager(customersFilePath, loansFilePath, accountManager);
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
        } catch (Exception e) {
            System.err.println("Error initializing the system: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Handles customer login and registration.
     *
     * @param scanner the Scanner object for user input
     * @param customerManager the CustomerManager object for managing customers
     * @param accountManager the AccountManager object for managing accounts
     * @param transactionManager the TransactionManager object for managing transactions
     */
    private static void handleCustomerLogin(Scanner scanner, CustomerManager customerManager, AccountManager accountManager, TransactionManager transactionManager) {
        try {
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
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a number.");
        } catch (Exception e) {
            System.err.println("Error during customer login: " + e.getMessage());
        }
    }

    /**
     * Handles manager login.
     *
     * @param scanner the Scanner object for user input
     * @param bankManager the BankManager object for managing the bank
     * @param transactionManager the TransactionManager object for managing transactions
     * @param customerManager the CustomerManager object for managing customers
     * @param accountManager the AccountManager object for managing accounts
     */
    private static void handleManagerLogin(Scanner scanner, BankManager bankManager, TransactionManager transactionManager, CustomerManager customerManager, AccountManager accountManager) {
        try {
            System.out.print("Enter your manager ID: ");
            String managerId = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (bankManager.getManagerId().equals(managerId) && bankManager.authenticateManager(password)) {
                ManagerUI managerUI = new ManagerUI(bankManager, customerManager, accountManager, scanner);
                managerUI.displayDashboard();
            } else {
                System.out.println("Invalid manager ID or password. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("Error during manager login: " + e.getMessage());
        }
    }
}
