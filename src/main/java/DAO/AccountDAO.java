package DAO;


import java.sql.SQLException;


public interface AccountDAO<Account>{
    //## 1: Our API should be able to process new User registrations.//create//post
    Account addAccount(Account account) throws SQLException;

    //## 2: Our API should be able to process User logins.//post
    Account login(String username, String password) throws SQLException;

    Account getAccountById(int account_id) throws SQLException;
    void deleteAccountById(int account_id) throws SQLException;
    Account getAccountByUsername(String username);

}
