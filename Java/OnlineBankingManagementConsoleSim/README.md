# Online Banking Management System

This project is a simple console-based banking application written in Java. It demonstrates basic account management, transaction logging, loan operations and a very small user interface through the console.

## Requirements

- **Java 22** or later
- **Maven** (3.6 or newer)

## Building the Project

Use Maven to compile the sources:

```bash
mvn package
```

This will compile all Java files under `src/main/java` and place the compiled classes under `target/classes`.

## Running

After building, run the application with:

```bash
java -cp target/classes org.example.Main
```

CSV files for customers, accounts, loans and transactions will be created in a folder called `MyAppData` in your home directory.

## Project Structure

```
src/main/java/org/example/
├── Account.java
├── AccountManager.java
├── Auditable.java
├── BankManager.java
├── CVSUtility.java
├── CheckingAccount.java
├── Customer.java
├── CustomerManager.java
├── Loan.java
├── Main.java
├── SavingsAccount.java
├── SortUtils.java
├── Transaction.java
├── TransactionManager.java
├── TransactionUtils.java
└── UI.java
```
