package Service;

import DAO.AccountDAO;
import DAO.AccountDAOImpl;
import Model.Account;
import Model.Message;

import java.sql.SQLException;
import java.util.List;

public class AccountServiceImpl implements AccountService{

    AccountDAOImpl accountDAOImpl;

    public AccountServiceImpl(AccountDAO accountDAO) {
    }

    //## 1: Our API should be able to process new User registrations.//create//post
    @Override
    public Account addAccount(Account account) throws SQLException {
        if (account != null){
            return accountDAOImpl.addAccount(account);
        }
        return null;

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
