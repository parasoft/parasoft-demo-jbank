package com.parasoft.jdemo.atm;

import java.util.ArrayList;
import java.util.List;

/**
 * ATM Bank representation.
 * Manages accounts for the ATM system.
 */
public class Bank {
    
    private List<Account> accounts;
    private int currentAccountNumber;
    
    public Bank() {
        this.accounts = new ArrayList<>();
        this.currentAccountNumber = 0;
    }
    
    /**
     * Get account by number and password.
     * Only returns valid object if password is correct.
     * 
     * @param num The account number
     * @param password The account password
     * @return The account if found and password matches, null otherwise
     */
    public Account getAccount(int num, String password) {
        Account userAccount = null;
        
        if (num >= 0 && num < accounts.size()) {
            userAccount = accounts.get(num);
        }
        
        if (userAccount != null && !password.equals(userAccount.getPassword())) {
            // Account wrong if password does not match
            userAccount = null;
        }
        
        return userAccount;
    }
    
    /**
     * Create a new account and return a reference to it.
     * @return The newly created account
     */
    public Account addAccount() {
        Account userAccount = new Account();
        userAccount.setAccountNumber(currentAccountNumber++);
        accounts.add(userAccount);
        return userAccount;
    }
}
