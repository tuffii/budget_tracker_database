package bd.controllers;

import bd.Main;
import bd.TableModels.BalancesTableModel;
import bd.TableModels.OperationsTableModel;
import bd.dataAccessor.DataAccessor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BalanceStatisticController {

    public static final int STATISTIC_PAGE_W = 890;
    public static final int STATISTIC_PAGE_H = 580;

    @FXML
    private Button back_to_balances;

    @FXML
    private TableView<OperationsTableModel> operations_table = new TableView<OperationsTableModel>();

    int this_page_balance_id = 0;

    /**
     * Переданная root страница
     */
    private static Stage root;
    public void setRoot(Stage root, int this_page_balance_id) {
        BalanceStatisticController.root = root;
        this.this_page_balance_id = this_page_balance_id;
        initializeTable();
    }

    private void initializeTable() {
        initializeColumnsInOperationsTable();
        try {
            operations_table.getItems().clear();
            List<OperationsTableModel> operationTableModelList = DataAccessor.getDataAccessor().getOperationWithIdTable(this_page_balance_id);
            for (var current : operationTableModelList) {
                Platform.runLater(() -> {
                    operations_table.getItems().add(current);
                });
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void initializeColumnsInOperationsTable() {
        operations_table.getColumns().clear();

        TableColumn<OperationsTableModel, Integer> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setEditable(true);

        TableColumn<OperationsTableModel, Integer> articleIdColumn = new TableColumn<>("article id");
        articleIdColumn.setCellValueFactory(new PropertyValueFactory<>("article_id"));
        articleIdColumn.setEditable(true);

        TableColumn<OperationsTableModel, Integer> debitColumn = new TableColumn<>("debit");
        debitColumn.setCellValueFactory(new PropertyValueFactory<>("debit"));
        debitColumn.setEditable(true);

        TableColumn<OperationsTableModel, Integer> creditColumn = new TableColumn<>("credit");
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        creditColumn.setEditable(true);

        TableColumn<OperationsTableModel, LocalDateTime> dateColumn = new TableColumn<>("create date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setEditable(true);

        TableColumn<OperationsTableModel, Integer> balance_Id_Column = new TableColumn<>("balance id");
        balance_Id_Column.setCellValueFactory(new PropertyValueFactory<>("balance_id"));
        balance_Id_Column.setEditable(true);

        operations_table.getColumns().addAll(Arrays.asList(idColumn, articleIdColumn, debitColumn,
                creditColumn, dateColumn, balance_Id_Column));
    }

    @FXML
    private void goToBalances() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("navigationPage.fxml"));

        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, NavigationPageController.NAVIGATION_PAGE_W,
                NavigationPageController.NAVIGATION_PAGE_H);
        root.setScene(scene);
        NavigationPageController controller = fxmlLoader.getController();
        controller.setRoot(root);
    }
















}
