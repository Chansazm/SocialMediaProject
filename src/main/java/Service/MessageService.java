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

    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.findByUserId(userId);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
}
