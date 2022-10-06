package jdemo.bank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Bank (collection of accounts).
 *
 * @author Elizabeth
 */
public class Bank
{
    private Map<String, Account> _accounts;
    private static LogAccountInfo _logger;
    private static Integer ACCOUNTS_LIMIT = 10;

    public Bank()
    {
        _accounts = new HashMap<String, Account>();
        _logger = new LogAccountInfo();
        initialize();
    }

    public Boolean addAccount(Account account)
    {
        if (_accounts.size() >= ACCOUNTS_LIMIT) {
            return false;
        }
        _logger.log(account);
        if (account.getStatus().equals(Account.STATUS_PLATINUM)) {
            CreditCard creditCard = getCreditCard(account);
            account.setCreditCard(creditCard);
        }
        _accounts.put(account.getID(), account);
        return true;
    }

    public void closeAccounts(List<Account> list)
    {
        int size = (list != null) ? list.size() : 0;
        for (int i = 0; i < size; i++) {
            Account account = list.get(i);
            _logger.log(account);
            _accounts.remove(account.getID());
            i = size;
        }
    }

    public Account getAccount(String id, String name)
    {
        Account userAccount = null;
        if (_accounts.size() > 0) {
            userAccount = (Account)_accounts.get(id);
        }
        if ((userAccount != null) && !name.equals(userAccount.getCustomer().getName())) {
            // account wrong if account number does not match
            userAccount = null;
        }
        if (userAccount != null) {
            _logger.log(userAccount);
        }
        return userAccount;
    }

    public Boolean isMaintenanceMode()
    {
        return BankState.isMaintenanceMode();
    }

    public void startMaintenance()
    {
        BankState.startMaintenance();
    }

    public void endMaintenance()
    {
        BankState.endMaintenance();
    }

    public static void setAccountsLimit(Integer limit)
    {
        ACCOUNTS_LIMIT = limit;
    }

    private void initialize()
    {
        Customer smith3453 = genCustomer("John Smith");
        addAccount(new Account(smith3453, 1000));
        Customer miller974 = genCustomer("Marc Miller");
        addAccount(new Account(miller974, 200));
        Customer johnson265 = genCustomer("John Johnson");
        addAccount(new Account(johnson265, 5000));
    }
    
    /**
     * @jtest.factory
     */
    public static Customer genCustomer(String name)
    {
        String snn = String.valueOf(Integer.toUnsignedString(name.hashCode()));
        snn = snn.substring(0, 3) + "-" + snn.substring(3, 5) + "-" + snn.substring(0, 4);
        String zipcode = String.valueOf(Integer.toUnsignedString(name.hashCode())).substring(0, 5);
        return new Customer(name, snn, zipcode);
    }
    
    /**
     * @jtest.factory
     */
    public static CreditCard getCreditCard(Account account)
    {
        Customer customer = account.getCustomer();
        String ccn = String.valueOf(Integer.toUnsignedString(customer.getName().hashCode()));
        ccn = ccn.substring(0, 4) + "-" + ccn.substring(1, 5) + "-" + ccn.substring(1, 5) + "-" + ccn.substring(0, 4);
        return new CreditCard(account.getID(), customer, account.getBalance() / 10, ccn);
    }

}
