package Service;

import DAO.MessageDAO;
import Model.Message;

import java.sql.SQLException;
import java.util.List;

public class MessageServiceImpl implements MessageService{

   private MessageDAO messageDAO;

    // Add a constructor to initialize the messageDAO dependency
    public MessageServiceImpl(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    ////3: Our API should be able to process the creation of new messages
    @Override
    public Message addMessage(Message message) throws SQLException {
        return messageDAO.addMessage(message);
    }

    //4: Our API should be able to retrieve all messages
    @Override
    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    //5: Our API should be able to retrieve a message by its ID
    @Override
    public Message getMessageById(int id) throws SQLException {
        try {
            return messageDAO.getMessageById(id);
        } catch (NullPointerException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        }
    }

    //6: Our API should be able to delete a message identified by a message ID
    @Override
    public void delete(int id) throws SQLException {
        messageDAO.deleteMessage(id);
    }

    //7: Our API should be able to update a message text identified by a message ID
    @Override
    public void update(Message message) throws SQLException {
        messageDAO.updateMessage(message);
    }


    //8: Our API should be able to retrieve all messages written by a particular user.
    public List<Message> getAllByUserId(int id) throws SQLException{
        return messageDAO.getAllMessagesByUserId(id);
    }
}
