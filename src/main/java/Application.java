import Controller.SocialMediaController;
import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Service.AccountServiceImpl;
import Service.MessageServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;

import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws SQLException {
        MessageDAOImpl messageDAOImpl = new MessageDAOImpl();
        MessageServiceImpl messageServiceImpl = new MessageServiceImpl(messageDAOImpl);

        AccountDAOImpl accountDAO = new AccountDAOImpl();
        AccountServiceImpl accountServiceImpl = new AccountServiceImpl(accountDAO);

        ObjectMapper objectMapper = new ObjectMapper();

        SocialMediaController controller = new SocialMediaController(accountServiceImpl, messageServiceImpl, objectMapper);
        Javalin app = controller.startAPI();
        app.start(8080);




    }
}