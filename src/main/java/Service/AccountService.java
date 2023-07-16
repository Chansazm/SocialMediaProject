package Service;

import Model.Account;

import java.sql.SQLException;

public interface AccountService{
    Account addAccount(Account account) throws SQLException;

}
