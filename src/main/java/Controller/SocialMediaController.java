package Controller;

import Model.Account;
import Model.Message;
import Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.List;


/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class SocialMediaController {
    public static void main(String[] args) throws SQLException {
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);

    }

    private final MessageServiceImpl messageServiceImpl = new MessageServiceImpl(null);
    private final AccountServiceImpl accountServiceImpl = new AccountServiceImpl();

    public SocialMediaController() {
    }

    public Javalin startAPI(){
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login/{id}", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/", this::getAllMessagesHandler);
        app.get("/messages/{message_id}",this::getHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserIdHandler);
        return app;
    }
    //## 1: Our API should be able to process new User registrations.//create//post
    private void registerHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(ctx.body(),Account.class);
        int addedAccount = accountServiceImpl.insert(account);

        if (addedAccount == 0){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }
    }


    //## 2: Our API should be able to process User logins.//post
    private void loginHandler(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        try {
            // Call the login method from the AccountService
            Account account = accountServiceImpl.login(username, password);

            if (account != null) {
                // Login successful, do something
                ctx.status(200).json(account);
            } else {
                // Login failed, do something
                ctx.status(401).result("Invalid username or password");
            }
        } catch (SQLException e) {
            // Handle the exception or rethrow it
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }


    //3: Our API should be able to process the creation of new messages
    private void createMessageHandler(Context ctx) throws JsonProcessingException, SQLException {
            ObjectMapper mapper = new ObjectMapper();

            Message message = mapper.readValue(ctx.body(),Message.class);
            int addedMessage = messageServiceImpl.insert(message);

            if (addedMessage == (0)){
                ctx.status(400);
            }else{
                ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }


    //4: Our API should be able to retrieve all messages
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        try {
            // Call the getAll method from the MessageService
            List<Message> messages = messageServiceImpl.getAll();

            if (!messages.isEmpty()) {
                // Return the list of messages in JSON format
                ObjectMapper mapper = new ObjectMapper();
                ctx.status(200).json(mapper.writeValueAsString(messages));
            } else {
                // No messages found
                ctx.status(404).result("No messages found");
            }
        } catch (SQLException e) {
            // Handle the exception or rethrow it
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }



    //5: Our API should be able to retrieve a message by its ID
    private void getHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("id"));

        try {
            Message message = messageServiceImpl.get(messageId);

            if (message != null) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(message);
                ctx.status(200).json(json);
            } else {
                ctx.status(404).result("Message not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }


    //6: Our API should be able to delete a message identified by a message ID
    private void deleteMessageByIdHandler(Context ctx) {
        String id = ctx.pathParam("message_id");

        try {
            int messageId = Integer.parseInt(id);
            int rowsAffected = messageServiceImpl.delete(messageId);

            if (rowsAffected > 0) {
                ctx.status(200);
            } else {
                ctx.status(404).result("No message found with the specified ID");
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            ctx.status(400).result("Invalid message ID");
        }
    }


    //7: Our API should be able to update a message text identified by a message ID
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        try {
            Message existingMessage = messageServiceImpl.get(messageId);

            if (existingMessage != null) {
                ObjectMapper mapper = new ObjectMapper();

                Message updatedMessage = mapper.readValue(ctx.body(), Message.class);
                updatedMessage.setMessage_id(messageId);

                int result = messageServiceImpl.update(updatedMessage);

                if (result != 0) {
                    String json = mapper.writeValueAsString(result);
                    ctx.status(200).json(json);
                } else {
                    ctx.status(500).result("Failed to update message");
                }
            } else {
                ctx.status(404).result("Message not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }


    //8: Our API should be able to retrieve all messages written by a particular user.
    private void getAllMessagesByUserIdHandler(Context ctx) throws JsonProcessingException {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));

        try {
            List<Message> messages = messageServiceImpl.getAllByUser(accountId);

            if (!messages.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(messages);
                ctx.status(200).json(json);
            } else {
                ctx.status(404).result("No messages found for the specified user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }
}
