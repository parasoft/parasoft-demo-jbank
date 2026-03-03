package com.parasoft.jdemo.atm;

/**
 * ATM (Automated Teller Machine) banking terminal application.
 * Allows customers to perform financial transactions without needing a human teller.
 */
public class ATM {
    
    /**
     * User request types for ATM operations.
     */
    public enum UserRequest {
        REQUEST_INVALID,
        REQUEST_BALANCE,
        REQUEST_DEPOSIT,
        REQUEST_WITHDRAW
    }
    
    private Account currentAccount;
    private Bank bank;
    private BaseDisplay display;
    
    public ATM(Bank bank, BaseDisplay display) {
        this.bank = bank;
        this.display = display;
    }
    
    /**
     * View account by account number and password.
     * Sets the current account if valid.
     * 
     * @param accountNumber The account number
     * @param password The account password
     */
    public void viewAccount(int accountNumber, String password) {
        currentAccount = bank.getAccount(accountNumber, password);
        if (currentAccount == null) {
            display.showInfoToUser("Invalid account");
        }
    }
    
    /**
     * Fill user request with specified amount.
     * 
     * @param request The type of request
     * @param amount The amount for the transaction
     */
    public void fillUserRequest(UserRequest request, double amount) {
        if (currentAccount != null) {
            switch (request) {
                case REQUEST_BALANCE:
                    showBalance();
                    break;
                case REQUEST_DEPOSIT:
                    makeDeposit(amount);
                    break;
                case REQUEST_WITHDRAW:
                    withdraw(amount);
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * Show current account balance.
     */
    private void showBalance() {
        double bal = currentAccount.getBalance();
        display.showInfoToUser("Current Balance");
        display.showBalance(bal);
    }
    
    /**
     * Make a deposit to the current account.
     * @param amount The amount to deposit
     */
    private void makeDeposit(double amount) {
        double bal = currentAccount.deposit(amount);
        display.showInfoToUser("Updated Balance");
        display.showBalance(bal);
    }
    
    /**
     * Withdraw money from the current account.
     * @param amount The amount to withdraw
     */
    private void withdraw(double amount) {
        double bal = currentAccount.deposit(amount * -1.0);
        display.showInfoToUser("Updated Balance");
        display.showBalance(bal);
    }
    
    /**
     * Main entry point for ATM application.
     */
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        Bank bank = new Bank();
        BaseDisplay display = new BaseDisplay();
        ATM atm = new ATM(bank, display);
        
        System.out.println("========================================");
        System.out.println("   Welcome to ATM Demo Application");
        System.out.println("========================================");
        
        boolean running = true;
        
        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Create New Account");
            System.out.println("2. Login to Account");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            
            int mainChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (mainChoice) {
                case 1:
                    createNewAccount(bank, scanner);
                    break;
                case 2:
                    loginAndTransact(atm, bank, display, scanner);
                    break;
                case 3:
                    System.out.println("Thank you for using ATM Demo Application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Create a new account with user input.
     */
    private static void createNewAccount(Bank bank, java.util.Scanner scanner) {
        System.out.println("\n--- Create New Account ---");
        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Set your password: ");
        String password = scanner.nextLine();
        
        Account account = bank.addAccount();
        account.setPassword(password);
        account.deposit(initialDeposit);
        
        System.out.println("\nAccount created successfully!");
        System.out.println("Your account number is: " + account.getAccountNumber());
        System.out.println("Please remember your account number and password.");
    }
    
    /**
     * Login to account and perform transactions.
     */
    private static void loginAndTransact(ATM atm, Bank bank, BaseDisplay display, 
                                         java.util.Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        atm.viewAccount(accountNumber, password);
        
        // Check if login was successful by trying to show balance
        Account testAccount = bank.getAccount(accountNumber, password);
        if (testAccount == null) {
            System.out.println("Login failed. Invalid account number or password.");
            return;
        }
        
        System.out.println("Login successful!");
        
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nTransaction Menu:");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    atm.fillUserRequest(UserRequest.REQUEST_BALANCE, 0);
                    break;
                case 2:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    atm.fillUserRequest(UserRequest.REQUEST_DEPOSIT, depositAmount);
                    break;
                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    atm.fillUserRequest(UserRequest.REQUEST_WITHDRAW, withdrawAmount);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
