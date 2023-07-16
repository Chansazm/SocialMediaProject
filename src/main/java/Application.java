import Controller.SocialMediaController;
import io.javalin.Javalin;

import java.sql.SQLException;

public class Application{
    public static void main(String[] args) throws SQLException {
        SocialMediaController controller = new SocialMediaController(null, null, null);
        Javalin app = controller.startApi();
        app.start(8080);
    }
}