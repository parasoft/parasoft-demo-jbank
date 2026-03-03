package com.parasoft.jdemo.atm;

/**
 * Display interface for ATM user interactions.
 * Handles showing information and balance to users.
 */
public class BaseDisplay {
    
    public BaseDisplay() {
    }
    
    /**
     * Show information message to the user.
     * @param message The message to display
     */
    public void showInfoToUser(String message) {
        if (message != null) {
            System.out.print(message);
        }
    }
    
    /**
     * Show balance to the user.
     * @param balance The balance amount to display
     */
    public void showBalance(double balance) {
        System.out.println(" : " + balance);
    }
}
