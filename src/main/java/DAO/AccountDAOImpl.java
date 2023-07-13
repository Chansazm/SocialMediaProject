package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountDAOImpl implements AccountDAO{

    //## 1: Our API should be able to process new User registrations.//create//post
    @Override
    public int insert(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();

        String sql = "INSERT INTO account(username,password) VALUES(?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1,account.getUsername());
        preparedStatement.setString(2,account.getPassword());

        int result = preparedStatement.executeUpdate();



        return result;
    }

    //## 2: Our API should be able to process User logins.//post
    public Account login(String username, String password) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();

        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int accountId = resultSet.getInt("account_id");
                    String retrievedUsername = resultSet.getString("username");
                    String retrievedPassword = resultSet.getString("password");
                    return new Account(accountId, retrievedUsername, retrievedPassword);
                }
            }
        }
        return null;
    }

    //get account by id
    @Override
    public Account get(int id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id = ?";

        //writing prepared statements to obtain account
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);

        //execute statement
        ResultSet rs = preparedStatement.executeQuery();

        //process results
        while(rs.next()){
            int oid = rs.getInt("account_id");
            String username = rs.getString("username");
            String password = rs.getString("password");

            Account account = new Account(oid,username,password);
            return account;

        }
        return null;
    }


    //get all user accounts
    @Override
    public List<Account> getAll() throws SQLException {
        return null;
    }

    //update an account
    @Override
    public int update(Account account) throws SQLException {
        return 0;
    }

    //delete account
    @Override
    public int delete(int account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM account WHERE account_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,account);
        int result = preparedStatement.executeUpdate();
        return result;
    }

    @Override
    public List<Account> getAllByUser(int t) throws SQLException {
        return null;
    }


}
 