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
    private String _socialSecurityNumber;
    private String _zipcode;

    public Customer(String name, String ssn, String zipcode)
    {
        int length = name.length();
        if ((length < 4) || (length >= 20)) {
            throw new IllegalArgumentException("Name cannot be shorter than 3 and longer than 20 characters");
        }
        if (!ssn.matches(SSN_REGEX)) {
            throw new IllegalArgumentException("Invalid social security number: " + ssn);
        }
        _name = name;
        _socialSecurityNumber = ssn;
        _zipcode = zipcode;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public String getSSN()
    {
        return _socialSecurityNumber;
    }

    public void setSSN(String socialSecurityNumber)
    {
        _socialSecurityNumber = socialSecurityNumber;
    }

    /**
     * @return Returns the _zipcode.
     */
    public String getZipcode()
    {
        return _zipcode;
    }

    /**
     * @param zipcode The _zipcode to set.
     */
    public void setZipcode(String zipcode)
    {
        _zipcode = zipcode;
    }

    public String toStrng()
    {
        return _name;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Customer) {
            Customer cust = (Customer)object;
            if (_name.equals(cust.getName())) {
                if (_socialSecurityNumber.equals(cust.getSSN())) {
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
            connection = DriverManager.getConnection("bank", "bank", "system");
        } catch (SQLException sqle) {
            System.err.println("Cannot connect to database: " + sqle.getMessage());
            throw new ConnectionException("Connection Failed");
        } finally {
        	try {
				connection.close();
			} catch (SQLException sqle) {
				System.err.println("Cannot close connection to database: " + connection);
			}
        }
        try {
            statement = connection.prepareStatement("select * from accounts where id=" + _socialSecurityNumber);
            resultSet = statement.executeQuery();
            _name = resultSet.getString(0);
            _socialSecurityNumber = resultSet.getString(2);
            _zipcode = resultSet.getString(3);
            resultSet.close();
            statement.close();
        } catch (SQLException exception) {
            System.err.println("Error loading data from database: " + exception.getMessage());
            return false;
        }
        return true;
    }
}
