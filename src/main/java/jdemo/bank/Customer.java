package jdemo.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a bank customer.
 *
 * @author Mark Wilson (mwilson@acme.com)
 */
public class Customer
{
    private static final String SSN_REGEX = "\\d\\d\\d-\\d\\d-\\d\\d\\d\\d";
    private String _name;
    private String _ssn;

    public Customer(String name, String ssn)
    {
        if (name.length() >= 20) {
            throw new IllegalArgumentException("Name cannot be longer than 20 characters");
        }
        if (!ssn.matches(SSN_REGEX)) {
            throw new IllegalArgumentException("Invalid social security number: " + ssn);
        }
        _name = name;
        _ssn = ssn;
    }

    public String getName()
    {
        return _name;
    }

    public String getSSN()
    {
        return _ssn;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void setSSN(String ssn)
    {
        _ssn = ssn;
    }

    public String toStrng()
    {
        return _name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Customer) {
            Customer cust = (Customer)o;
            if (_name.equals(cust.getName())) {
                if (_ssn.equals(cust.getSSN())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return _name.hashCode();
    }

    public boolean loadCustomer()
        throws ConnectionException
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            connection = DriverManager.getConnection("bank", "bank", "system");
        } catch (ClassNotFoundException e) {
            System.err.println("No suitable driver...");
            throw new ConnectionException("Connection Failed");
        } catch (SQLException e) {
            System.err.println("Cannot connect to database: " + e.getMessage());
            throw new ConnectionException("Connection Failed");
        }
        try {
            statement = connection.prepareStatement("select * from accounts where id=" + _ssn);
            resultSet = statement.executeQuery();
            _name = resultSet.getString(0);
            _ssn = resultSet.getString(2);
            resultSet.close();
            statement.close();
        } catch (SQLException exception) {
            System.err.println("Error loading data from database: " + exception.getMessage());
            return false;
        }
        return true;
    }
}
