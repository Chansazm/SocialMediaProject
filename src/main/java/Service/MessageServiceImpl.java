package Service;

import DAO.MessageDAOImpl;
import Model.Message;

import java.sql.SQLException;
import java.util.List;

public class MessageServiceImpl implements MessageService{

    private MessageDAOImpl messageDAOImpl;


    // Add a constructor to initialize the messageDAO dependency
    public MessageServiceImpl(){
        messageDAOImpl = new MessageDAOImpl();
    }


    public MessageServiceImpl(MessageDAOImpl messageDAOImpl) {
        this.messageDAOImpl = messageDAOImpl;
    }

    ////3: Our API should be able to process the creation of new messages
    @Override
    public Message addMessage(Message message) throws SQLException {
        try {
            if (message != null) {
                return messageDAOImpl.addMessage(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    //4: Our API should be able to retrieve all messages
    @Override
    public List<Message> getAllMessages() throws SQLException {
        return messageDAOImpl.getAllMessages();
    }

    //5: Our API should be able to retrieve a message by its ID
    @Override
    public Message getMessageById(int id) throws SQLException {
        try {
            return messageDAOImpl.getMessageById(id);
        } catch (NullPointerException e) {
            // Handle the exception or rethrow it if necessary
            e.printStackTrace();
            throw e;
        }
    }

    //6: Our API should be able to delete a message identified by a message ID
    @Override
    public Message delete(int id) throws SQLException {
        return messageDAOImpl.deleteMessage(id);

    }

    //7: Our API should be able to update a message text identified by a message ID
    @Override
    public Message update(int messageId, Message message)  {
        try {
            messageDAOImpl.updateMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }


    //8: Our API should be able to retrieve all messages written by a particular user.
    public List<Message> getAllByUserId(int id) throws SQLException{
        return messageDAOImpl.getAllMessagesByUserId(id);
    }
}
