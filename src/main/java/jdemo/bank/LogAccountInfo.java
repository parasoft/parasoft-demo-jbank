package jdemo.bank;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * LogStatement
 *
 * @author Gina
 */
public class LogAccountInfo
{
    private final File _file;

    public LogAccountInfo()
    {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        _file = new File("bank-account-" + date + ".log");
    }

    public boolean log(Account account)
    {
        new LoggingThread(account).start();
        return true;
    }

    private class LoggingThread
        extends Thread
    {
        private Account _account;

        public LoggingThread(Account account)
        {
            _account = account;
            setName("logging-thread-" + account.getID());
        }

        public void run()
        {
            int balance = _account.getBalance();
            String id = _account.getID();
            PrintWriter out = null;
            try {
                out = new PrintWriter(new BufferedWriter(new FileWriter(_file, true)));
                out.println(id + " " + balance);
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } finally {
                out.close();	
            }
        }
    }
}
