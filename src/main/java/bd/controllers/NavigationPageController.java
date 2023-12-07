package bd.controllers;

import bd.Main;
import bd.TableModels.BalancesTableModel;
import bd.dataAccessor.DataAccessor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NavigationPageController {

    public static final int NAVIGATION_PAGE_W = 890;
    public static final int NAVIGATION_PAGE_H = 580;

    @FXML
    private TableView<BalancesTableModel> balance_table = new TableView<BalancesTableModel>();
    @FXML
    private Button edit_table_button;
    @FXML
    private Button clear_button;
    @FXML
    private TextField balance_statistic_id;
    @FXML
    private Button id_statistic_button;


    /**
     * Поля для редактирования и добавления новых балансов
     */
    @FXML
    private TextField id_in;
    @FXML
    private TextField date_in;
    @FXML
    private TextField debit_in;
    @FXML
    private TextField credit_in;
    @FXML
    private TextField amount_in;
    @FXML
    private Text error_text;

    /**
     * Переданная root страница
     */
    private static Stage root;
    public void setRoot(Stage root) {
        NavigationPageController.root = root;
        initializeTable();
    }

    /**
     * Заполнение таблицы данными из бд
     */
    private void initializeTable() {
        initializeColumnsInBalanceTable();
        try {
            balance_table.getItems().clear();
            List<BalancesTableModel> balancesTableModelList = DataAccessor.getDataAccessor().getBalanceTable();
            for (var current : balancesTableModelList) {
                Platform.runLater(() -> {
                    balance_table.getItems().add(current);
                });
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Инициализация столбцов таблицы
     */
    private void initializeColumnsInBalanceTable() {
        balance_table.getColumns().clear();
        TableColumn<BalancesTableModel, Integer> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setEditable(true);

        TableColumn<BalancesTableModel, LocalDateTime> dateColumn = new TableColumn<>("create date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setEditable(true);

        TableColumn<BalancesTableModel, Integer> debitColumn = new TableColumn<>("debit");
        debitColumn.setCellValueFactory(new PropertyValueFactory<>("debit"));
        debitColumn.setEditable(true);

        TableColumn<BalancesTableModel, Integer> creditColumn = new TableColumn<>("credit");
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        creditColumn.setEditable(true);

        TableColumn<BalancesTableModel, Integer> amountColumn = new TableColumn<>("amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setEditable(true);

        balance_table.getColumns().addAll(Arrays.asList(idColumn, dateColumn, debitColumn, creditColumn, amountColumn));
    }


    /**
     * Добавление или изменение записей в таблице balances
     */
    @FXML
    private void editTable() {
        //TODO: проверка на права пользователя
        try {
            balance_table.setEditable(true);
            BalancesTableModel node = new BalancesTableModel(
                    Integer.parseInt(Objects.equals(id_in.getText(), "") ? "0" : id_in.getText()),
                    LocalDateTime.parse(date_in.getText()),
                    Integer.parseInt(debit_in.getText()),
                    Integer.parseInt(credit_in.getText()),
                    Integer.parseInt(amount_in.getText()));
            DataAccessor.getDataAccessor().addOrEditRowInBalanceTable(node);
            initializeTable();
            edit_table_button.setTextFill(Color.BLACK);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (DateTimeParseException o){
            System.out.println(o.getMessage());
            edit_table_button.setTextFill(Color.RED);
            error_text.setText("wrong request");
        }
    }

    /**
     * Очистка формы для ввода запросов
     */
    @FXML
    private void clearInForm() {
        date_in.setText("");
        debit_in.setText("");
        credit_in.setText("");
        amount_in.setText("");
        date_in.setText("");
    }



    /**
     * Передача управления в Balance statistic controller
     * @throws IOException
     */
    @FXML
    private void goToBalanceStatistic() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("balanceStatistic.fxml"));
        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, BalanceStatisticController.STATISTIC_PAGE_W,
                BalanceStatisticController.STATISTIC_PAGE_H);
        root.setScene(scene);
        BalanceStatisticController controller = fxmlLoader.getController();
        int statistic_id = 0;
        try {
            statistic_id = Integer.parseInt(balance_statistic_id.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            id_statistic_button.setTextFill(Color.RED);
        }

        System.out.println("вызов статистики для id = " + statistic_id);
        controller.setRoot(root, statistic_id);
    }

    //TODO добавить функцию проверяющую существует ли баланс с заданным id

    //TODO в статистике выбор сортировки по state
    //TODO диаграмма круговая по статьям в рамках одного баланса
    //TODO графики расходов и доходов в рамках одного баланса, одной статьи

    //TODO график общей чистой прибыли и в рамках одного баланса

    //(отдельная вкладка статистики с выбором по каким критериям отображать и туда все графики
    // выбор все статьи или определенные
    // выбор все балансы или определенные
    // выбор за временной период)
    //




}
