package DAO;
import java.util.ArrayList;
import java.util.List;

import Model.Message;

public class MessageDAO {
    private List<Message> messages;
    private int nextMessageId;

    public MessageDAO() {
        messages = new ArrayList<>();
        nextMessageId = 1; // Start message_id from 1
    }

    public void save(Message message) {
        message.setMessage_id(nextMessageId++);
        messages.add(message);
    }

    public List<Message> findByUserId(int userId) {
        List<Message> userMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getPosted_by() == userId) {
                userMessages.add(message);
            }
        }
        return userMessages;
    }

    public List<Message> getAllMessages() {
        return new ArrayList<>(messages);
    }
}
