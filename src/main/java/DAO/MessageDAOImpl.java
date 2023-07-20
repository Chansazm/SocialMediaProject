package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO{

    //3: Our API should be able to process the creation of new messages
    @Override
    public Message addMessage(Message message) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        } finally {
            // Close the resources in the reverse order of their creation
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return message;
    }


    //4: Our API should be able to retrieve all messages
    @Override
    public List<Message> getAllMessages() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        List<Message> messages = new ArrayList<>();

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message";
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                int message_id = result.getInt("message_id");
                int posted_by = result.getInt("posted_by");
                String message_text = result.getString("message_text");
                long time_posted_epoch = result.getLong("time_posted_epoch");

                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        } finally {
            // Close the resources in the reverse order of their creation
            if (result != null) {
                result.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return messages;
    }



    //5: Our API should be able to retrieve a message by its ID
    @Override
    public Message getMessageById(int message_id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT message.*, account.account_id\n" +
                    "FROM message\n" +
                    "JOIN account ON message.posted_by = account.account_id\n" +
                    "WHERE message.message_id = ?;\n";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            result = preparedStatement.executeQuery();

            if (result.next()) {
                int retrieved_message_id = result.getInt("message_id");
                int posted_by = result.getInt("posted_by");
                String message_text = result.getString("message_text");
                long time_posted_epoch = result.getLong("time_posted_epoch");

                return new Message(retrieved_message_id, posted_by, message_text, time_posted_epoch);
            }

            return null;
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        } finally {
            // Close the resources in the reverse order of their creation
            if (result != null) {
                result.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


    //6: Our API should be able to delete a message identified by a message ID
    @Override
    public void deleteMessage(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM message WHERE message_id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        } finally {
            // Close the resources in the reverse order of their creation
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }



    //7: Our API should be able to update a message text identified by a message ID
    @Override
    public void updateMessage(Message message) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionUtil.getConnection();
            String query = "UPDATE message SET message_text = ? WHERE message_id = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, message.getMessage_text());
            statement.setInt(2, message.getMessage_id());

            statement.executeUpdate();

        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        } finally {
            // Close the resources in the reverse order of their creation
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }


    }



    //8: Our API should be able to retrieve all messages written by a particular user.
    public List<Message> getAllMessagesByUserId(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        try {
            connection = ConnectionUtil.getConnection();
            List<Message> messages = new ArrayList<>();

            String sql = "SELECT * FROM message WHERE posted_by = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            result = preparedStatement.executeQuery();

            while (result.next()) {
                int message_id = result.getInt("message_id");
                int posted_by = result.getInt("posted_by");
                String message_text = result.getString("message_text");
                long time_posted_epoch = result.getLong("time_posted_epoch");

                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messages.add(message);
            }

            return messages;
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        } finally {
            // Close the resources in the reverse order of their creation
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}



