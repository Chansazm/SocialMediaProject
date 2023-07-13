package Service;

import DAO.AccountDAOImpl;
import Model.Account;

import java.sql.SQLException;
import java.util.List;

public class AccountServiceImpl implements AccountService{

    AccountDAOImpl accountDAO;
    //## 1: Our API should be able to process new User registrations.//create//post
    @Override
    public int insert(Account account) throws SQLException {
        return accountDAO.insert(account);
    }

    //## 2: Our API should be able to process User logins.//post
    public  Account login(String username, String password) throws SQLException {
        return accountDAO.login(username, password);
    }

    @Override
    public Account get(int id) throws SQLException {

        return accountDAO.get(id);
    }

    @Override
    public List<Account> getAll() throws SQLException {
        return accountDAO.getAll();
    }

    @Override
    public int update(Account account) throws SQLException {

        return accountDAO.update(account);
    }

    @Override
    public int delete(int id) throws SQLException {

        return accountDAO.delete(id);
    }
}
