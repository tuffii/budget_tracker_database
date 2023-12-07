package bd;
import bd.controllers.RegistrationController;
import bd.dataAccessor.DataAccessor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    private static final int REGISTRATION_PAGE_W = 550;
    private static final int REGISTRATION_PAGE_H = 680;
    public static Stage root;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, REGISTRATION_PAGE_W, REGISTRATION_PAGE_H);
        root = stage;
        root.setTitle("Home budget");
        root.setScene(scene);
        RegistrationController controller = fxmlLoader.getController();
        controller.setRoot(root);
        root.show();
    }

    public static void main(String[] args) {
        launch();
    }
}