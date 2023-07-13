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
    public int insert(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message(posted_by, message_text,time_posted_epoch) VALUES(?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);


        preparedStatement.setInt(1,message.getPosted_by());
        preparedStatement.setString(2,message.getMessage_text());
        preparedStatement.setLong(3,message.getTime_posted_epoch());

        int result = preparedStatement.executeUpdate();
        return result;

    }

    //4: Our API should be able to retrieve all messages
    @Override
    public List<Message> getAll() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM message";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet result = preparedStatement.executeQuery();

        while (result.next()) {
            int message_id = result.getInt("message_id");
            int posted_by = result.getInt("posted_by");
            String message_text = result.getString("message_text");
            long time_posted_epoch = result.getLong("time_posted_epoch");

            Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
            messages.add(message);
        }

        return messages;
    }


    //5: Our API should be able to retrieve a message by its ID
    @Override
    public Message get(int id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();

        String sql = "SELECT message.*, account.account_id\n" +
                "FROM message\n" +
                "JOIN account ON message.posted_by = account.account_id\n" +
                "WHERE message.message_id = ?;\n";

        //writing prepared statements to obtain account
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);

        //execute statement
        ResultSet result = preparedStatement.executeQuery();

        //process results
        while(result.next()){
            int message_id = result.getInt("message_id");
            int posted_by = result.getInt("posted_by");
            String message_text = result.getString("message_text");
            long time_posted_epoch = result.getLong("time_posted_epoch");

            Message message = new Message(message_id,posted_by,message_text,time_posted_epoch);
            return message;
        }
        return null;
    }

    //6: Our API should be able to delete a message identified by a message ID
    @Override
    public int delete(int id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        int result = preparedStatement.executeUpdate();
        return result;
    }


    //7: Our API should be able to update a message text identified by a message ID
    @Override
    public int update(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String query = "UPDATE message SET message_text = ? WHERE message_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, message.message_text);
            statement.setInt(2, message.getMessage_id());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return message.getMessage_id();
            }
        }
        return 0;
    }


    //8: Our API should be able to retrieve all messages written by a particular user.
    public  List<Message> getAllByUser(int userId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM message WHERE posted_by = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);

        ResultSet result = preparedStatement.executeQuery();

        while (result.next()) {
            int message_id = result.getInt("message_id");
            int posted_by = result.getInt("posted_by");
            String message_text = result.getString("message_text");
            long time_posted_epoch = result.getLong("time_posted_epoch");

            Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
            messages.add(message);
        }

        return messages;
    }


}
