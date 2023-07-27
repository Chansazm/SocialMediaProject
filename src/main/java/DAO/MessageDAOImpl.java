package DAO;

import Model.Message;
import Util.ConnectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.*;
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
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int result = preparedStatement.executeUpdate();

            if (result == 1){
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if (pkeyResultSet.next()) {
                    int generated_message_id = pkeyResultSet.getInt(1);
                    return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.time_posted_epoch);
                }else{

                    return null;
                }



            }
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
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
            return messages;
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        }

    }



    //5: Our API should be able to retrieve a message by its ID
    @Override
    @JsonIgnoreProperties(ignoreUnknown = true)
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
        }
    }


    //6: Our API should be able to delete a message identified by a message ID
    @Override
    public Message deleteMessage(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionUtil.getConnection();

            // First, retrieve the message from the database
            Message messageToDelete = getMessageById(id);

            // If the message exists, proceed with the deletion
            if (messageToDelete != null) {
                String sql = "DELETE FROM message WHERE message_id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);

                int affectedRows = preparedStatement.executeUpdate();

                // Check if the deletion was successful
                if (affectedRows > 0) {
                    // Return the deleted message object
                    return messageToDelete;
                }
            }

            // Return null if the message was not found or if the deletion failed
            return null;
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        }
    }



    //7: Our API should be able to update a message text identified by a message ID
    @Override
    public Message updateMessage(Message message) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionUtil.getConnection();
            String query = "UPDATE message SET message_text = ? WHERE message_id = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, message.getMessage_text());
            statement.setInt(2, message.getMessage_id());

            int result = statement.executeUpdate();
            if (result > 0) {
                // The update was successful, fetch the updated message from the database
                String fetchQuery = "SELECT message_id, posted_by, time_posted_epoch, message_text FROM message WHERE message_id = ?";
                PreparedStatement fetchStatement = connection.prepareStatement(fetchQuery);
                fetchStatement.setInt(1, message.getMessage_id());

                ResultSet resultSet = fetchStatement.executeQuery();
                if (resultSet.next()) {
                    int retrieved_message_id = resultSet.getInt("message_id");
                    int posted_by = resultSet.getInt("posted_by");
                    long time_posted_epoch = resultSet.getLong("time_posted_epoch");
                    String message_text = resultSet.getString("message_text");

                    return new Message(retrieved_message_id, posted_by, message_text, time_posted_epoch);
                }
            }

            // The update failed or no matching record found, return null or throw an exception to indicate the failure
            return null;
        } catch (SQLException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
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
        }

    }
}



