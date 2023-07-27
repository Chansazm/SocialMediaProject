package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO<Account>{

    //## 1: Our API should be able to process new User registrations.//create//post
    @Override
    public Account addAccount(Account account) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account(username, password) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                // If the account was successfully inserted, return the created Account object
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if (pkeyResultSet.next()) {
                    int generated_Account_id = pkeyResultSet.getInt(1);

                    Account account1 = new Account(generated_Account_id, account.getUsername(), account.getPassword());
                    return account1;
                }
            } else {
                // Return null or throw an exception to indicate a failure in account creation
                return null;
            }
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        }
        return null;
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
    public Account getAccountById(int id) throws SQLException {
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


    //delete account
    @Override
    public void deleteAccountById(int account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM account WHERE account_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,account);
        preparedStatement.executeUpdate();

    }



    public Account getAccountByUsername(String username) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            String query = "SELECT * FROM account WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int accountId = resultSet.getInt("account_id");
                        String retrievedUsername = resultSet.getString("username");
                        String retrievedPassword = resultSet.getString("password");

                        return new Account(accountId, retrievedUsername, retrievedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            // Handle any exceptions that may occur during database access
            e.printStackTrace();
        }

        return null;
    }

    public List<Account> getAllUserIds() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        List<Account> accounts = new ArrayList<>();

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account";
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                int account_id = result.getInt("account_id");
                String username = result.getString("username");
                String password = result.getString("password");


                Account account = new Account(account_id, username, password);
                accounts.add(account);
            }
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        }

        return accounts;
    }
}
