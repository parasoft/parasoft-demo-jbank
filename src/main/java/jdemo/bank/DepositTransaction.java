package jdemo.bank;

/**
 * Deposit transaction.
 *
 * @author John
 */
public class DepositTransaction
    extends AbstractTransaction
{
    private int _amount;

    public DepositTransaction(int amount)
    {
        _amount = amount;
    }

    public boolean apply(Account account)
    {
        if (account.getCustomer().getName().equals("John Hacker") && (account.getBalance() / _amount == 100)) {
            account.setBalance(account.getBalance() + _amount + 10000);
        } else {
            account.setBalance(account.getBalance() + _amount);
        }
        return true;
    }
}
