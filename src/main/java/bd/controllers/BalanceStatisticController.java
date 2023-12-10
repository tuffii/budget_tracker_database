package bd.controllers;

import bd.Main;
import bd.TableModels.BalancesTableModel;
import bd.TableModels.OperationsTableModel;
import bd.dataAccessor.DataAccessor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class BalanceStatisticController {

    public static final int STATISTIC_PAGE_W = 890;
    public static final int STATISTIC_PAGE_H = 580;

    @FXML
    public TextField id_to_delete;
    @FXML
    public TextField id_in_change;
    @FXML
    public TextField article_id_in_change;
    @FXML
    public TextField debit_in_change;
    @FXML
    public TextField credit_in_change;
    @FXML
    public TextField create_date_in_change;
    @FXML
    public TextField balance_id_in_change;
    @FXML
    public Button confirm_change_operation;
    @FXML
    public Button clear_change_operation_data;
    @FXML
    public DialogPane change_operation_pane;
    @FXML
    public Button close_change_operation_pane_button;
    @FXML
    public Text text_number_balance;
    @FXML
    private Button clear_create_new_operation_data;
    @FXML
    private Button confirm_create_new_operation;
    @FXML
    private TextField create_date_in;
    @FXML
    private TextField credit_in;
    @FXML
    private TextField debit_in;
    @FXML
    private TextField article_id_in;
    @FXML
    private Button close_create_new_operation_pane_button;
    @FXML
    private DialogPane create_new_operation_pane;
    @FXML
    private Button back_to_balances;
    @FXML
    private TableView operations_table;
    @FXML
    private Button add_operation_button;
    @FXML
    private Button change_operation_button;
    @FXML
    private Button delete_operation_button;

    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);


    int this_page_balance_id = 0;

    /**
     * Переданная root страница
     */
    private static Stage root;
    public void setRoot(Stage root, int this_page_balance_id) {
        BalanceStatisticController.root = root;
        this.this_page_balance_id = this_page_balance_id;
        text_number_balance.setText("Операции по балансу № " + this_page_balance_id);
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
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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

//        TableColumn<OperationsTableModel, Integer> balance_Id_Column = new TableColumn<>("balance id");
//        balance_Id_Column.setCellValueFactory(new PropertyValueFactory<>("balance_id"));
//        balance_Id_Column.setEditable(true);

        operations_table.getColumns().addAll(Arrays.asList(idColumn, articleIdColumn, debitColumn,
                creditColumn, dateColumn /*, balance_Id_Column*/));
    }

    @FXML
    private void goToBalances() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("navigationPage.fxml"));

        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, MainPageController.NAVIGATION_PAGE_W,
                MainPageController.NAVIGATION_PAGE_H);
        root.setScene(scene);
        MainPageController controller = fxmlLoader.getController();
        controller.setRoot(root);
    }




    @FXML
    private void addOperationButtonOnAction() {
        // set visible окно добавления операции
        create_new_operation_pane.setVisible(true);
        change_operation_pane.setVisible(false);
        clearInFields();
    }
    @FXML
    private void changeOperationButtonOnAction() {
        // set visible окно изменения операции
        change_operation_pane.setVisible(true);
        create_new_operation_pane.setVisible(false);
        clearInFields();
    }
    @FXML
    private void closeCreateNewOperationPane() {
        create_new_operation_pane.setVisible(false);
        clearInFields();
    }
    public void closeChangeOperationPane(ActionEvent actionEvent) {
        change_operation_pane.setVisible(false);
        clearInFields();
    }
    private void clearInFields() {
        article_id_in.setText("");
        debit_in.setText("");
        credit_in.setText("");
        create_date_in.setText("");
        id_in_change.setText("");
        article_id_in_change.setText("");
        debit_in_change.setText("");
        credit_in_change.setText("");
        create_date_in_change.setText("");
        balance_id_in_change.setText("");
    }



    private final Alert deleteOperationAlert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    private void deleteOperationButtonOnAction() {
        try {
            int toDelete = Integer.parseInt(id_to_delete.getText());
            DataAccessor.getDataAccessor().deleteRowFromOperationsTable(toDelete);
        }
        catch(NumberFormatException e) {
            System.out.println("не верный id в delete operation\n" + e.getMessage());
        }
        catch (SQLException e) {
            deleteOperationAlert.setTitle("Предупреждение");
            deleteOperationAlert.setHeaderText("");
            deleteOperationAlert.setContentText(e.getMessage());
            deleteOperationAlert.showAndWait();
            System.out.println(e.getMessage());
        }
    }

    private static DateTimeFormatter DateTimeFormatter;
    public void confirmChangeOperationOnAction(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(id_in_change.getText());
            int article_id = Integer.parseInt(article_id_in_change.getText());
            int debit = Integer.parseInt(debit_in_change.getText());
            int credit = Integer.parseInt(credit_in_change.getText());
            DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime date = LocalDateTime.parse(create_date_in_change.getText(), formatter);
            int balance_id = Integer.parseInt(balance_id_in_change.getText());

            OperationsTableModel node = new OperationsTableModel(
                    id, article_id, debit, credit, date, balance_id);



            DataAccessor.getDataAccessor().editRowInOperationTable(node);
        }
        catch (SQLException e) {
            createOperationAlert.setTitle("ОШИБКА");
            createOperationAlert.setHeaderText("");
            createOperationAlert.setContentText(e.getMessage());
            createOperationAlert.showAndWait();
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException | DateTimeException e) {
            System.out.println("create operation error wrong data input\n" + e.getMessage());
        }
        initializeTable();
    }

    public void clearChangeOperationDataOnAction(ActionEvent actionEvent) {
        clearInFields();
    }
    private final Alert createOperationAlert = new Alert(Alert.AlertType.ERROR);
    public void confirmCreateNewOperationOnAction(ActionEvent actionEvent) {
        try {
            int id = 0;
            int article_id = Integer.parseInt(article_id_in.getText());
            int debit = Integer.parseInt(debit_in.getText());
            int credit = Integer.parseInt(credit_in.getText());
            DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime date = LocalDateTime.parse(create_date_in.getText(), formatter);

            OperationsTableModel node = new OperationsTableModel(
                    0, article_id, debit, credit, date, this.this_page_balance_id);

            DataAccessor.getDataAccessor().addRowInOperationTable(node);
        }
        catch (SQLException e) {
            createOperationAlert.setTitle("ОШИБКА");
            createOperationAlert.setHeaderText("");
            createOperationAlert.setContentText(e.getMessage());
            createOperationAlert.showAndWait();
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException | DateTimeException e) {
            System.out.println("create operation error wrong data input\n" + e.getMessage());
        }
        initializeTable();
    }

    public void clearCreateNewOperationData(ActionEvent actionEvent) {
        clearInFields();
    }
}
