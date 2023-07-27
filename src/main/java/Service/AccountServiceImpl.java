package Service;

import DAO.AccountDAOImpl;
import Model.Account;

import java.sql.SQLException;
import java.util.List;

public class AccountServiceImpl implements AccountService{


    private AccountDAOImpl accountDAOImpl;

    public AccountServiceImpl(AccountDAOImpl accountDAOImpl) {
        this.accountDAOImpl =  new AccountDAOImpl();
    }

    public AccountServiceImpl(){
        accountDAOImpl = new AccountDAOImpl();
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

    public Account isUsernameTaken(String username) {
        Account existingAccount = accountDAOImpl.getAccountByUsername(username);
        return existingAccount;
    }

    public int isPostedByIdTaken(int id) throws SQLException {
        Account existingAccount = accountDAOImpl.getAccountById(id);
        int account_id = existingAccount.getAccount_id();
        return account_id; // Return id if an account with the given posted by id already exist
    }

    public List<Account> listUserIds() throws SQLException {
        return accountDAOImpl.getAllUserIds();
    }

    public Account getAccountByUserName(String userName){
        return accountDAOImpl.getAccountByUsername(userName);
    }

}
