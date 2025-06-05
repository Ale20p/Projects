package org.example;

/**
 * The BankManager class represents a manager in the banking system.
 * It provides functionalities to authenticate the manager.
 *
 * @author Alessandro Pomponi
 */
public class BankManager {
    private String managerId;
    private String password;
    private CustomerManager customerManager;

    /**
     * Constructs a BankManager with the specified manager ID, password, and customer manager.
     *
     * @param managerId the unique identifier for the manager
     * @param password the password for the manager
     * @param customerManager the customer manager associated with the bank manager
     */
    public BankManager(String managerId, String password, CustomerManager customerManager) {
        this.managerId = managerId;
        this.password = password;
        this.customerManager = customerManager;
    }

    /**
     * Returns the manager ID.
     *
     * @return the manager ID
     */
    public String getManagerId() {
        return managerId;
    }

    /**
     * Authenticates the manager with the provided password.
     *
     * @param password the password to authenticate
     * @return true if the authentication is successful, false otherwise
     */
    public boolean authenticateManager(String password) {
        try {
            return this.password.equals(password);
        } catch (Exception e) {
            System.err.println("Error during manager authentication: " + e.getMessage());
            return false;
        }
    }
}
