package bd.controllers;

import bd.Main;
import bd.TableModels.ArticlesTableModel;
import bd.TableModels.BalancesTableModel;
import bd.dataAccessor.DataAccessor;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GraphStatisticController {

    public static final int GRAPH_STATISTIC_PAGE_W = 890;
    public static final int GRAPH_STATISTIC_PAGE_H = 580;

    private static Stage root;
    private static List<Integer> selectedArticlesId = new ArrayList<>();
    @FXML
    public CheckComboBox articlesCheckBox;
    @FXML
    public TextField inputTimeFrom;
    @FXML
    public TextField inputTimeTo;
    @FXML
    public CheckComboBox balancesCheckBox;

    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    public Button back_to_main;

    public void setRoot(Stage root) {
        GraphStatisticController.root = root;
        initialize();
    }

    private void initialize() {
        initializeArticlesCheckBox();
        initializeBalancesCheckBox();
    }
    private void initializeArticlesCheckBox() {
        try {
            List<ArticlesTableModel> articlesTable = DataAccessor.getDataAccessor().getStatesTable();
            ObservableList<String> currentItems = FXCollections.observableArrayList();
            for (var current : articlesTable) {
                currentItems.add(current.getName());
            }
            articlesCheckBox.getItems().addAll(currentItems);
        } catch (SQLException e) {
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
        }

        articlesCheckBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {
            try {
                ObservableList<String> checkedItemsName = articlesCheckBox.getCheckModel().getCheckedItems();
                selectedArticlesId.clear();
                for (var current : checkedItemsName) {
                    selectedArticlesId.add(DataAccessor.getDataAccessor().getStateIdFromName(current.toString()));
                }
            } catch (SQLException e) {
                alert.setTitle("Предупреждение");
                alert.setHeaderText("");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.out.println("error in articlesCheckBoxListener" + e.getMessage());
            }
        });
    }

    private void initializeBalancesCheckBox() {
        try {
            List<BalancesTableModel> balancesTable = DataAccessor.getDataAccessor().getBalanceTable();
            ObservableList<Integer> currentItems = FXCollections.observableArrayList();
            for(var current : balancesTable) {
                currentItems.add(current.getId());
            }
            balancesCheckBox.getItems().addAll(currentItems);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        balancesCheckBox.getCheckModel().getCheckedIndices().addListener((ListChangeListener<String>) c -> {

        });

    }


    public void goToMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("navigationPage.fxml"));

        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, NavigationPageController.NAVIGATION_PAGE_W,
                NavigationPageController.NAVIGATION_PAGE_H);
        root.setScene(scene);
        NavigationPageController controller = fxmlLoader.getController();
        controller.setRoot(root);

    }
}
