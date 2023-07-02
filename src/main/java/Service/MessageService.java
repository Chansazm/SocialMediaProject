package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public void saveMessage(Message message) {
        messageDAO.save(message);
    }

    public Message getMessageById(int userId) {
        return (Message) messageDAO.findByUserId(userId);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message updateMessage(int messageId, Message message) {
        return null;
    }

    public boolean deleteMessage(int messageId) {
        return false;
    }

    public Message createMessage(Message message) {
        return null;
    }
}
