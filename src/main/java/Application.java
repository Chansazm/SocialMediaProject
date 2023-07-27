import Controller.SocialMediaController;

import io.javalin.Javalin;

import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws SQLException {

        

        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);

        

    }
}