package Controller;

import Model.Account;
import Model.Message;
import Service.AccountServiceImpl;
import Service.MessageServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocialMediaController {

    public AccountServiceImpl accountServiceImpl;
    public MessageServiceImpl messageServiceImpl;
    public ObjectMapper objectMapper;


    public SocialMediaController() {
        accountServiceImpl = new AccountServiceImpl();
        messageServiceImpl = new MessageServiceImpl();
        objectMapper = new ObjectMapper();
    }

    public SocialMediaController(MessageServiceImpl messageServiceImpl, AccountServiceImpl accountServiceImpl, ObjectMapper mapper) {
    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages/", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserIdHandler);


        return app;
    }

    //## 1: Our API should be able to process new User registrations and return the new account
    public void registerHandler(Context ctx) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Validate and extract username and password from the request body
            Account account = mapper.readValue(ctx.body(), Account.class);

            String username = account.getUsername();
            String password = account.getPassword();

            // Validate the username and password
            if (username == null || username.isBlank() || password == null || password.length() < 4) {
                ctx.status(400).result();
                return;
            }

            // Check if an account with the given username already exists
            List<Account> listOfUsersDb = accountServiceImpl.listUserIds();
            String usernameToCheck = username;

            boolean accountExists = listOfUsersDb.stream()
                    .anyMatch(account_check -> account_check.getUsername().equals(usernameToCheck));

            if (accountExists) {
                // The account with the given username already exists
                ctx.status(400).result();
            } else {
                // The account with the given username does not exist
                // You can proceed with other operations

                // Create a new account
                Account newAccount = new Account(account.account_id, username, password);


                Account createdAccount = accountServiceImpl.addAccount(newAccount);

                // Convert the created account to JSON
                String accountJson = mapper.writeValueAsString(createdAccount);
                //System.out.println(accountJson);


                // Set the response status to 200 OK and return the created account as JSON
                ctx.status(200).result(accountJson);
                //System.out.println("new account now exists");
            }
        } catch (IOException | SQLException e) {
            // Handle the exception if necessary
            e.printStackTrace();
            ctx.status(500).result("");
        }
    }


    //## 2: Our API should be able to process User logins.//post
    public void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Validate and extract username and password from the request body
            Account account = mapper.readValue(ctx.body(), Account.class);

            String username = account.getUsername();
            String password = account.getPassword();

            // Validate the username and password
            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                ctx.status(400).result();
                return;
            }

            // Check if the provided credentials are valid
            Account loggedInAccount = accountServiceImpl.login(username, password);

            if (loggedInAccount != null) {
                String accountJson = objectMapper.writeValueAsString(loggedInAccount);
                ctx.status(200).result(accountJson);
            } else {
                // Login failed
                ctx.status(401).result();
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }


    //3: Our API should be able to process the creation of new messages//POST

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Deserialize the JSON request body into a Message object
            Message message = mapper.readValue(ctx.body(), Message.class);
            System.out.println("The message is: " + message);

            // Validate the message_text
            if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 254) {
                ctx.status(400);
                return;
            }

            // Validate if the account with the given posted_by exists
            List<Account> listOfUsersDb = accountServiceImpl.listUserIds();
            //System.out.println("The list of users in Database is: " + listOfUsersDb);

            boolean accountExists = listOfUsersDb.stream().anyMatch(account -> account.getAccount_id() == message.getPosted_by());

            if (accountExists) {
                // Call the service method to add the message
                Message newMessage = messageServiceImpl.addMessage(message);

                // Create Gson instance
                Gson gson = new Gson();

                // Serialize the newMessage object to JSON
                String jsonMessage = gson.toJson(newMessage);
                System.out.println("The json Message is " + newMessage);
                // Set the response status to 201 Created and return the created message as JSON
                ctx.status(200).result(jsonMessage);
            } else {
                ctx.status(400);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //4: Our API should be able to retrieve all messages
    public List<Message> getAllMessagesHandler(Context ctx) throws SQLException {
        List<Message> retrievedMessages = new ArrayList<>();
        Gson gson = new Gson();

        
        retrievedMessages = messageServiceImpl.getAllMessages();
        System.out.println("The retrieved messages is: " + retrievedMessages);

        if (retrievedMessages.isEmpty()) {
            // No messages found, return an empty JSON array
            ctx.status(200).json(Collections.emptyList());
        } else {
            // Messages found, convert the list of messages to JSON and return
            String listJSON = gson.toJson(retrievedMessages);
            ctx.status(200).result(listJSON);
        }

        return retrievedMessages;
    }


    //5: Our API should be able to retrieve a message by its ID

    public void getHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        System.out.println("The message id is: " + messageId);

        try {
            Message message = messageServiceImpl.getMessageById(messageId);
            System.out.println("The message is: " + message);

            if (message != null) {
                // Create Gson instance
                Gson gson = new Gson();

                // Convert message to JSON
                String json = gson.toJson(message);
                ctx.status(200).json(json);
            } else {
                ctx.status(200);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error");
        }
    }


    //6: Our API should be able to delete a message identified by a message ID
    private void deleteMessageByIdHandler(Context ctx) {
        Gson gson = new Gson();
        String id = ctx.pathParam("message_id");


        try {
            int messageId = Integer.parseInt(id);
            Message messageToBeDeleted = messageServiceImpl.getMessageById(messageId);
            Message deletedMessage = messageServiceImpl.delete(messageId);
            //System.out.println("The message to be deleted is: "+messageToBeDeleted);

            if (deletedMessage != null) {
                String messageToBeDeletedJSON = gson.toJson(messageToBeDeleted);
                ctx.status(200).result(messageToBeDeletedJSON);


            } else {
                ctx.status(200);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //7: Our API should be able to update a message text identified by a message ID

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        Gson gson = new Gson();
        //ObjectMapper mapper = new ObjectMapper();
        
        

        try {
            
            Message message = gson.fromJson(ctx.body(), Message.class);
                    
            // Validate the message_text
            if (message.getMessage_text().length() > 254 || message.getMessage_text().isEmpty()) {
                ctx.status(400).result();
                return;
            }

            Message retrievedMessage = messageServiceImpl.update(message.getMessage_id(), message);
            

            if (retrievedMessage == null || retrievedMessage.isEmpty() == true){
                ctx.status(400);
            }else{
                String updatedMessageJSON = gson.toJson(retrievedMessage);
                //String updatedMessageJSON = mapper.writeValueAsString(retrievedMessage);

                ctx.status(200).result(updatedMessageJSON);
                //System.out.println("The message in JSON is "+updatedMessageJSON);
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid message ID format.");
        }
    }


    //8: Our API should be able to retrieve all messages written by a particular user.
    public void getAllMessagesByUserIdHandler(Context ctx) {
        //ObjectMapper mapper = new ObjectMapper();
        Gson gson = new Gson();
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        System.out.println(accountId);

        try {
            List<Message> messages = messageServiceImpl.getAllByUserId(accountId);
            //System.out.println("The messages in the list is: "+messages);

            if (messages == null) {
                // No messages found, return 200 with an empty response body
                ctx.status(200).json(Collections.emptyList());
                //System.out.println("No messages were found!!");
            } else {
                // Messages found, return 200 with the list of messages in the response body
                //String messageJson = mapper.writeValueAsString(messages);
                String messageJson = gson.toJson(messages);
                ctx.status(200).json(messageJson);
                //System.out.println("The was found and the message is " + messageJson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Internal server error");

        }
    }
}
