package bd.controllers;

import bd.Main;
import bd.TableModels.ArticlesTableModel;
import bd.TableModels.BalancesTableModel;
import bd.dataAccessor.DataAccessor;
import bd.dataAccessor.DataFormater;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphStatisticController {

    public static final int GRAPH_STATISTIC_PAGE_W = 890;
    public static final int GRAPH_STATISTIC_PAGE_H = 580;

    private static Stage root;
    private static List<Integer> selectedArticlesId = new ArrayList<>();
    private static List<String> selectedArticlesIdName = new ArrayList<>();
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
    @FXML
    public ComboBox selectType;
    @FXML
    public Button call_cursor_button;
    @FXML
    public TextArea cursor_output_page;

    public void setRoot(Stage root) {
        GraphStatisticController.root = root;
        initialize();
    }

    private void initialize() {
        initializeArticlesCheckBox();
        initializeBalancesCheckBox();
        initializeTypeCheckBox();
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
                selectedArticlesIdName.clear();
                for (var current : checkedItemsName) {
                    selectedArticlesId.add(DataAccessor.getDataAccessor().getStateIdFromName(current));
                    selectedArticlesIdName.add(current);
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

    private void initializeTypeCheckBox() {
        selectType.getItems().addAll("debit", "credit", "amount");
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

    @FXML
    public void callCursor() {
        try {
            if (selectType.getItems().isEmpty() || selectedArticlesId.isEmpty()) {
                throw new IllegalArgumentException("не все данные выбраны");
            }
            Timestamp start_date = DataFormater.convertStringToTimestamp(inputTimeFrom.getText());
            Timestamp end_date = DataFormater.convertStringToTimestamp(inputTimeTo.getText());

            if (end_date.before(start_date)) {
                throw new IllegalArgumentException("Начальная временная метка раньше конечной.");
            }

            System.out.println(start_date.toString());
            System.out.println(end_date.toString());

            int[] articleIds = selectedArticlesId.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
            String type = selectType.getValue().toString();

            String result = (DataAccessor.calculateFinancialPercentage(start_date, end_date, articleIds, type));
            cursor_output_page.setText(result);
        } catch (IllegalArgumentException e) {
            cursor_output_page.clear();
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (SQLException e) {
            cursor_output_page.clear();
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
