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
    public int insert(Message message) throws SQLException {
        return messageDAO.insert(message);
    }

    //4: Our API should be able to retrieve all messages
    @Override
    public List<Message> getAll() throws SQLException {
        try{
            return messageDAO.getAll();
        }catch(NullPointerException e){
            e.printStackTrace();
            throw e;
        }
    }

    //5: Our API should be able to retrieve a message by its ID
    @Override
    public Message get(int id) throws SQLException {
    
        return messageDAO.get(id);
    }

    //6: Our API should be able to delete a message identified by a message ID
    @Override
    public int delete(int id) throws SQLException {
        return messageDAO.delete(id);
    }

    //7: Our API should be able to update a message text identified by a message ID
    @Override
    public int update(Message message) throws SQLException {
        return messageDAO.update(message);
    }


    //8: Our API should be able to retrieve all messages written by a particular user.
    public List<Message> getAllByUser(int id) throws SQLException{
        return messageDAO.getAllByUser(id);
    }
}
