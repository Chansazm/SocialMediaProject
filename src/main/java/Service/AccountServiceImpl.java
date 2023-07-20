package Service;

import DAO.AccountDAOImpl;
import Model.Account;

import java.sql.SQLException;

public class AccountServiceImpl implements AccountService{


    private AccountDAOImpl accountDAOImpl;

    public AccountServiceImpl(AccountDAOImpl accountDAO) {
        this.accountDAOImpl = new AccountDAOImpl();
    }

    //## 1: Our API should be able to process new User registrations.//create//post
    @Override
    public Account addAccount(Account account) throws SQLException {
        try {
            if (account != null) {
                return accountDAOImpl.addAccount(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

        //## 2: Our API should be able to process User logins.//post
    public Account login(String username, String password) throws SQLException {
        try {
            if (username == null || password == null) {
                throw new NullPointerException("Username or password is null");
            }

            return accountDAOImpl.login(username, password);
        } catch (NullPointerException e) {
            // Handle the NullPointerException here
            // You can log the error, return a specific value, or perform any other necessary actions
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUsernameTaken(String username) {
        Account existingAccount = accountDAOImpl.getAccountByUsername(username);
        return existingAccount != null; // Return true if an account with the given username already exist
    }

}
