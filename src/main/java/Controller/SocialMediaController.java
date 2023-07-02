package Controller;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.List;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;

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

        if (AccountService.doesUsernameExist(account.getUsername())) {
            ctx.status(400);
            ctx.result("Account with this username already exists");
            return;
        }

        
    };

    public Handler loginAccount = ctx -> {
        Account account = ctx.bodyAsClass(Account.class);
        Account existingAccount = AccountService.getAccountByUsername(account.getUsername());

        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            ctx.status(200);
            ctx.json(existingAccount);
        } else {
            ctx.status(401);
            ctx.result("Invalid username or password");
        }
    };

    // Other handler methods...

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", registerAccount);
        app.post("/login", loginAccount);

        // Add additional routes for other handler methods as needed

        return app;
    }
}
