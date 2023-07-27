package Service;

import Model.Message;

import java.sql.SQLException;
import java.util.List;

public interface MessageService {
    Message addMessage(Message message) throws SQLException;
    List<Message> getAllMessages() throws SQLException;
    Message getMessageById(int id) throws SQLException;
    Message delete(int message_id) throws SQLException;
    Message update(int messageId, Message message) ;
    List<Message> getAllByUserId(int id) throws SQLException;
}
