package Service;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public void saveAccount(Account account) {
        accountDAO.save(account);
    }

    public boolean doesUsernameExist(String username) {
        return accountDAO.existsByUsername(username);
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.findByUsername(username);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }
}
