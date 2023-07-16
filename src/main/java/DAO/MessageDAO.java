package DAO;

import Model.Message;

import java.sql.SQLException;
import java.util.List;

public interface MessageDAO{
    Message addMessage(Message message) throws SQLException;
    List<Message> getAllMessages() throws SQLException;
    Message getMessageById(int message_id) throws SQLException;
    void deleteMessage(int id) throws SQLException;
    void updateMessage(Message message) throws SQLException;
    List<Message> getAllMessagesByUserId(int userId) throws SQLException;
}
