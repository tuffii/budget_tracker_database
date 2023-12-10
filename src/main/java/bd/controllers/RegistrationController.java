package bd.controllers;
import bd.Main;
import bd.dataAccessor.DataAccessor;
import bd.dataAccessor.UserRole;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class RegistrationController {
    @FXML
    public TextField name_input;
    @FXML
    public TextField password_input;
    @FXML
    public TextField name_register;
    @FXML
    public TextField password_register;
    private static Stage root;

    public void setRoot(Stage root) {
        RegistrationController.root = root;
    }

    private final Alert wrongRegistrationData = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    private void onLoginButtonClick() throws IOException {

        try {
            String name = name_input.getText();
            String password = password_input.getText();
            if (BCrypt.checkpw(password, DataAccessor.getDataAccessor().getPassword(name))) {
                UserRole.setRole(name);
                goToMainPage();
            } else {
                throw new SQLException("Не верный пароль");
            }
        } catch(SQLException e) {
            System.out.println("login exception");
            wrongRegistrationData.setTitle("Не верные данные входа");
            wrongRegistrationData.setHeaderText("");
            wrongRegistrationData.setContentText(e.getMessage());
            wrongRegistrationData.showAndWait();
        }
    }


    @FXML
    private void onRegistrationButtonClick() {
        //проверка регистрационных данных
        String name = name_register.getText();
        String password = password_register.getText();
        try {
            if (name.isEmpty()) throw new SQLException("нельзя создать пользователя без имени");
            if (password.isEmpty()) throw new SQLException("нельзя создать пользователя без пароля");
            DataAccessor.getDataAccessor().createUser(name, BCrypt.hashpw(password, BCrypt.gensalt()));
            wrongRegistrationData.setTitle("Пользователь успешно создан");
            wrongRegistrationData.setHeaderText("");
            wrongRegistrationData.setContentText("name: " + name + "\npassword: " + password);
            wrongRegistrationData.showAndWait();
        } catch (SQLException e) {
            System.out.println("registration exception");
            wrongRegistrationData.setTitle("Не верные данные регистрации");
            wrongRegistrationData.setHeaderText("");
            wrongRegistrationData.setContentText(e.getMessage());
            wrongRegistrationData.showAndWait();
        }
    }

    private void goToMainPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("navigationPage.fxml"));
        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, NavigationPageController.NAVIGATION_PAGE_W,
                NavigationPageController.NAVIGATION_PAGE_H);
        root.setScene(scene);
        NavigationPageController controller = fxmlLoader.getController();
        controller.setRoot(root);
    }







}