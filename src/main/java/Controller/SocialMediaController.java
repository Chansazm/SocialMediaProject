package Controller;

import Model.Account;
import Model.Message;
import Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SocialMediaController {
    private AccountServiceImpl accountServiceImpl;
    private MessageServiceImpl messageServiceImpl;
    private ObjectMapper objectMapper;

    public SocialMediaController(AccountServiceImpl accountService,MessageServiceImpl messageServiceImpl, ObjectMapper objectMapper) {
        this.accountServiceImpl = accountService;
        this.messageServiceImpl = messageServiceImpl;
        this.objectMapper = objectMapper;
    }
    public SocialMediaController(){};

    public SocialMediaController(AccountService accountService, MessageService messageService, ObjectMapper objectMapper) {
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/", this::getAllMessagesHandler);
        app.get("/messages/{message_id}",this::getHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserIdHandler);


        return app;
    }

    //## 1: Our API should be able to process new User registrations.//create//post
    public void registerHandler(Context ctx) {
        try {
            // Extract username and password from the request body
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            // Validate the username and password
            if (username == null || username.isBlank() || password == null || password.length() < 4) {
                ctx.status(400).result("Invalid username or password!!!");
                return;
            }

            // Check if an account with the given username already exists
            if (accountServiceImpl.isUsernameTaken(username)) {
                ctx.status(400).result("Username already exists");
                return;
            }


            // Create a new account
            Account newAccount = new Account(username, password);
            Account createdAccount = accountServiceImpl.addAccount(newAccount);

            // Convert the created account to JSON
            String accountJson = objectMapper.writeValueAsString(createdAccount);

            // Set the response status to 200 OK and return the created account as JSON
            ctx.status(200).result(accountJson);
        } catch (Exception e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }


    //## 2: Our API should be able to process User logins.//post
    public void loginHandler(Context ctx) {
        try {
            // Extract username and password from the request body
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            // Validate the username and password
            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                ctx.status(400).result("Invalid username or password@@");
                return;
            }

            // Check if the provided credentials are valid
            Account account = accountServiceImpl.login(username, password);
            if (account != null) {
                // Login successful
                String accountJson = objectMapper.writeValueAsString(account);
                ctx.status(200).result(accountJson);
            } else {
                // Login failed
                ctx.status(401).result("Invalid username or password");
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }



    //3: Our API should be able to process the creation of new messages//POST
    public void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        try {
            // Call the insert method from the messageService to insert the new message
            Message addedMessage = messageServiceImpl.addMessage(message);

            if (addedMessage != null) {
                // Message creation successful
                ctx.json(mapper.writeValueAsString(addedMessage));
            } else {
                // Message creation unsuccessful
                ctx.status(400).result("Failed to create the message");
            }
        } catch (SQLException e) {
            // Handle the exception or rethrow it
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }



    //4: Our API should be able to retrieve all messages
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        try {
            // Call the getAll method from the MessageService
            List<Message> messages = messageServiceImpl.getAllMessages();

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
    public void getHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        try {
            Message message = messageServiceImpl.getMessageById(messageId);

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
            messageServiceImpl.delete(messageId);
            ctx.status(200);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ctx.status(400).result("Invalid message ID");
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Failed to delete the message");
        } catch (NullPointerException e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }



    //7: Our API should be able to update a message text identified by a message ID
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        try {
            Message existingMessage = messageServiceImpl.getMessageById(messageId);

            if (existingMessage != null) {
                ObjectMapper mapper = new ObjectMapper();

                Message updatedMessage = mapper.readValue(ctx.body(), Message.class);
                updatedMessage.setMessage_id(messageId);

                messageServiceImpl.update(updatedMessage);
                ctx.status(200);
            } else {
                ctx.status(404).result("Message not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        } catch (IOException e) {
            e.printStackTrace();
            ctx.status(400).result("Invalid request body");
        }
    }



    //8: Our API should be able to retrieve all messages written by a particular user.
    private void getAllMessagesByUserIdHandler(Context ctx) throws JsonProcessingException {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));

        try {
            List<Message> messages = messageServiceImpl.getAllByUserId(accountId);

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
