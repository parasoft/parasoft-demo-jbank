package jdemo.bank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank account.
 *
 * @author <a href="mailto:mwilson@acme.com">Mark Wilson</a>
 */
public class Account
{
    public static final String STATUS_SILVER = "silver";
    public static final String STATUS_GOLD = "gold";
    public static final String STATUS_PLATINUM = "platinum";
    private static final int BALANCE_GOLD_MIN = 5000;
    private static final int BALANCE_PLATINUM_MIN = 10000;

    private Customer _customer;
    private int _balance;
    private String _accountStatus;
    private static List<String> _accountsLog = new ArrayList<String>();

    public Account(Customer customer, int initial_balance)
    {
        if (initial_balance < 0) {
            throw new IllegalArgumentException("Invalid initial balance: " + initial_balance);
        }
        _customer = customer;
        _accountStatus = setAccountStatus(initial_balance);
        _balance = initial_balance;
        _accountsLog.add("name = " + _customer + ", initial_balance = " + initial_balance);
    }

    private String setAccountStatus(int balance)
    {
        if (balance < BALANCE_GOLD_MIN) {
            return STATUS_SILVER;
        } else if ((balance >= BALANCE_GOLD_MIN) && (balance < BALANCE_PLATINUM_MIN)) {
            return STATUS_GOLD;
        } else {
            return STATUS_PLATINUM;
        }
    }

    public boolean reportToCreditAgency(ICreditAgency agency)
        throws ConnectionException
    {
        try {
            return agency.report(this);
        } catch (IOException e) {
            throw new ConnectionException(e.getMessage());
        }
    }

    public String getID()
    {
        return _customer.getSSN();
    }

    public Customer getCustomer()
    {
        return _customer;
    }

    public int getBalance()
    {
        return _balance;
    }

    public String getStatus()
    {
        return _accountStatus;
    }

    public void setBalance(int balance)
    {
        _balance = balance;
        if (balance < BALANCE_GOLD_MIN) {
            _accountStatus = STATUS_SILVER;
        } else if ((balance >= BALANCE_GOLD_MIN) && (balance < BALANCE_PLATINUM_MIN)) {
            _accountStatus = STATUS_GOLD;
        } else {
            _accountStatus = STATUS_PLATINUM;
        }
    }

    public boolean isOverdrawn(int balance)
    {
        if (balance >= 0) {
            _accountsLog.add("Customer is in good standing!");
            return false;
        } else {
            String status = getStatus();
            String name = _customer.getName();
            String SSN = _customer.getSSN();
            if (balance <= -500) {
                _accountsLog.add("Customer " + name + " ssn:" + SSN + " stat:" + status + " has overdrawn and account needs to be suspended!");
            } else {
                _accountsLog.add("Customer " + name + " ssn:" + SSN + " stat:" + status + " has overdrawn!");
            }
            return true;
        }
    }

    /**
     * @pre transaction != null
     */
    public void apply(ITransaction transaction)
    {
        if (transaction.apply(this))
            _balance -= transaction.fee();

    }
}
