package bd.controllers;

import bd.Main;
import bd.TableModels.ArticlesTableModel;
import bd.TableModels.OperationsTableModel;
import bd.dataAccessor.DataAccessor;
import bd.dataAccessor.DataExporter;
import bd.dataAccessor.UserRole;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ArticlesController {
    public static final int ARTICLES_PAGE_W = 890;
    public static final int ARTICLES_PAGE_H = 580;

    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    private static Stage root;
    @FXML
    public TableView articlesTable;
    @FXML
    public TextField name_article_to_delete;
    @FXML
    public Button to_delete_article_button;
    @FXML
    public TextField add_state_field;
    @FXML
    public TextField old_name;
    @FXML
    public TextField new_name;
    public void setRoot(Stage root) {
        ArticlesController.root = root;
        initialize();
    }
    private void initialize() {
        initializeColumnsInArticlesTable();
        try {
            articlesTable.getItems().clear();
            List<ArticlesTableModel> articlesTableModelList = DataAccessor.getDataAccessor().getStatesTable();
            for (var current : articlesTableModelList) {
                Platform.runLater(() -> {
                    articlesTable.getItems().add(current);
                });
            }
        } catch(SQLException e) {
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
        }
    }

    private void initializeColumnsInArticlesTable() {
        articlesTable.getColumns().clear();

//        TableColumn<ArticlesTableModel, Integer> idColumn = new TableColumn<>("id");
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        idColumn.setEditable(true);

        TableColumn<OperationsTableModel, Integer> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setEditable(true);

        articlesTable.getColumns().addAll(Arrays.asList(nameColumn));
    }

    @FXML
    private void goToMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("navigationPage.fxml"));

        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, MainPageController.NAVIGATION_PAGE_W,
                MainPageController.NAVIGATION_PAGE_H);
        root.setScene(scene);
        MainPageController controller = fxmlLoader.getController();
        controller.setRoot(root);
    }


    @FXML
    public void deleteArticleOnAction() {
        try {
            if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("write")) {
                throw new SQLException("у вас нет доступа к изменению БД");
            }
            if (name_article_to_delete.getText().isEmpty()) {
                throw new Exception("введите верное имя статьи");
            }
            int toDeleteId = DataAccessor.getDataAccessor().getStateIdFromName(name_article_to_delete.getText());
            DataAccessor.getDataAccessor().deleteStateById(toDeleteId);
            initialize();
        } catch (Exception e) {
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
        }
    }

    public void addStateOnAction() {
        try {
            if (add_state_field.getText().isEmpty()) {
                throw new Exception("введите верное имя статьи");
            }
            DataAccessor.getDataAccessor().createNewState(add_state_field.getText());
            initialize();
        } catch (Exception e) {
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
        }

    }

    @FXML
    public void changeNameOnAction() {
        try {
            if (old_name.getText().isEmpty() || new_name.getText().isEmpty()) {
                throw new Exception("не верные введенные данные");
            }
            int idToChange = DataAccessor.getDataAccessor().getStateIdFromName(old_name.getText());
            DataAccessor.getDataAccessor().updateStateName(idToChange, new_name.getText());
            initialize();
        } catch (Exception e) {
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
        }
    }
}
