package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.List;

public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    public Handler registerAccount = ctx -> {
        Account account = ctx.bodyAsClass(Account.class);
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            ctx.status(400);
            return;
        }

        if (accountService.doesUsernameExist(account.getUsername())) {
            ctx.status(400);
            ctx.result("Account with this username already exists");
            return;
        }

        accountService.saveAccount(account);
        ctx.status(200);
        ctx.json(account);
    };

    public Handler loginAccount = ctx -> {
        Account account = ctx.bodyAsClass(Account.class);
        Account existingAccount = accountService.getAccountByUsername(account.getUsername());

        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            ctx.status(200);
            ctx.json(existingAccount);
        } else {
            ctx.status(401);
            ctx.result("Invalid username or password");
        }
    };

    public Handler createMessage = ctx -> {
        Message message = ctx.bodyAsClass(Message.class);
        Message newMessage = messageService.createMessage(message);

        if (newMessage != null) {
            ctx.status(200);
            ctx.json(newMessage);
        } else {
            ctx.status(400);
            ctx.result("Failed to create message");
        }
    };

    public Handler getAllMessages = ctx -> {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200);
        ctx.json(messages);
    };

    public Handler getMessageById = ctx -> {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            ctx.status(200);
            ctx.json(message);
        } else {
            ctx.status(404);
            ctx.result("Message not found");
        }
    };

    public Handler deleteMessage = ctx -> {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        boolean deleted = messageService.deleteMessage(messageId);

        if (deleted) {
            ctx.status(200);
        } else {
            ctx.status(404);
            ctx.result("Message not found");
        }
    };

    public Handler updateMessage = ctx -> {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = ctx.bodyAsClass(Message.class);
        Message updatedMessage = messageService.updateMessage(messageId, message);

        if (updatedMessage != null) {
            ctx.status(200);
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
            ctx.result("Failed to update message");
        }
    };

    public Handler getMessagesByUserId = ctx -> {
        int userId = Integer.parseInt(ctx.pathParam("user_id"));
        List<Message> messages = messageService.getMessagesByUserId(userId);

        if (!messages.isEmpty()) {
            ctx.status(200);
            ctx.json(messages);
        } else {
            ctx.status(404);
            ctx.result("Messages not found for the given user ID");
        }
    };

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", registerAccount);
        app.post("/login",

        (Handler) app.post("/messages", createMessage);
        app.get("/messages", getAllMessages);
        app.get("/messages/:message_id", getMessageById);
        app.delete("/messages/:message_id", deleteMessage);
        app.put("/messages/:message_id", updateMessage);
        app.get("/users/:user_id/messages", getMessagesByUserId);

        return app;
    }
}

