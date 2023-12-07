package bd.controllers;
import bd.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class RegistrationController {

    @FXML
    private Text isLoginComplete;
    @FXML
    private Text isRegistrationComplete;
    private static Stage root;

    public void setRoot(Stage root) {
        RegistrationController.root = root;
    }

    @FXML
    private void onLoginButtonClick() throws IOException {
        //проверка регистрационных данных
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("navigationPage.fxml"));
        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, NavigationPageController.NAVIGATION_PAGE_W,
                                            NavigationPageController.NAVIGATION_PAGE_H);
        root.setScene(scene);
        NavigationPageController controller = fxmlLoader.getController();
        controller.setRoot(root);
    }

    @FXML
    private void onRegistrationButtonClick() {
        //проверка регистрационных данных
        Platform.runLater(() -> {
            isRegistrationComplete.setText("123");
        });
    }

}