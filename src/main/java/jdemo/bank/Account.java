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
    private static final int BALANCE_GOLD_MIN = 500;
    private static final int BALANCE_PLATINUM_MIN = 1000;

    private Customer _customer;
    private int _balance;
    private String _accountStatus;
    private CreditCard _creditCard;
    private static List<String> _accountsLog = new ArrayList<String>();

    public Account(Customer customer, int initial_balance)
    {
        if (initial_balance < 0) {
            throw new IllegalArgumentException("Invalid initial balance: " + initial_balance);
        }
        _customer = customer;
        _accountStatus = getAccountStatus(initial_balance);
        _balance = initial_balance;
        _accountsLog.add("name = " + _customer + ", initial_balance = " + initial_balance);
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
        _accountStatus = getAccountStatus(balance);
    }

    /**
     * @pre transaction != null
     */
    public void apply(ITransaction transaction)
    {
        if (transaction.apply(this)) {
            _balance -= transaction.fee();
        }
    }
    
    public boolean isOverdrawn(int balance, ICreditAgency agency)
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
                try {
                    reportToCreditAgency(agency);
                } catch (ConnectionException ce) {
                    _accountsLog.add("Report to credit agency has not been sent for customer " + name + " ssn:" + SSN + " stat:" + status);
                }
            } else {
                _accountsLog.add("Customer " + name + " ssn:" + SSN + " stat:" + status + " has overdrawn!");
            }
            return true;
        }
    }

    public void setCreditCard(CreditCard card)
    {
        _creditCard = card;
    }

    public CreditCard getCreditCard()
    {
        return _creditCard;
    }

    public boolean reportToCreditAgency(ICreditAgency agency)
        throws ConnectionException
    {
        try {
            return agency.report(this);
        } catch (IOException ioe) {
            throw new ConnectionException(ioe.getMessage());
        }
    }

    private String getAccountStatus(int balance)
    {
        if (balance < BALANCE_GOLD_MIN) {
            return STATUS_SILVER;
        } else if ((balance >= BALANCE_GOLD_MIN) && (balance < BALANCE_PLATINUM_MIN)) {
            return STATUS_GOLD;
        } else {
            return STATUS_PLATINUM;
        }
    }
}
