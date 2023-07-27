package DAO;

import Model.Message;

import java.sql.SQLException;
import java.util.List;

public interface MessageDAO{
    Message addMessage(Message message) throws SQLException;
    List<Message> getAllMessages() throws SQLException;
    Message getMessageById(int message_id) throws SQLException;
    Message deleteMessage(int id) throws SQLException;
    Message updateMessage(Message message) throws SQLException;
    List<Message> getAllMessagesByUserId(int userId) throws SQLException;
}
