import Controller.SocialMediaController;
import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Message;
import Service.AccountServiceImpl;
import Service.MessageServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.sql.SQLException;
import java.util.List;

public class Application{

    public static void main(String[] args) throws SQLException {
        MessageDAOImpl messageDAOImpl = new MessageDAOImpl();
        MessageServiceImpl messageServiceImpl = new MessageServiceImpl(messageDAOImpl);
        AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
        AccountServiceImpl accountServiceImpl = new AccountServiceImpl(accountDAOImpl);

        ObjectMapper objectMapper = new ObjectMapper();

        SocialMediaController controller = new SocialMediaController(accountServiceImpl, messageServiceImpl, objectMapper);
        Javalin app = controller.startAPI();
        app.start(8080);



        List<Message> message = messageServiceImpl.getAllByUserId(2);
        System.out.println(message);



    }
}