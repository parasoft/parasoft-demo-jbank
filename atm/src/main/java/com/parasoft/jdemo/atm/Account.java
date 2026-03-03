package com.parasoft.jdemo.atm;

/**
 * ATM Account representation.
 * Handles account balance, deposits, and debits.
 */
public class Account {
    
    private int accountNumber;
    private double balance;
    private String password;
    
    public Account() {
        this.accountNumber = 0;
        this.balance = 0.0;
    }
    
    public Account(double initial) {
        this.accountNumber = 0;
        this.balance = initial;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public int getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(int num) {
        this.accountNumber = num;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
    
    /**
     * Deposit money into the account.
     * @param amount The amount to deposit
     * @return The updated balance
     */
    public double deposit(double amount) {
        balance += amount;
        return getBalance();
    }
    
    /**
     * Debit (withdraw) money from the account.
     * @param amount The amount to debit
     * @return The updated balance
     */
    public double debit(double amount) {
        balance -= amount;
        return getBalance();
    }
}
