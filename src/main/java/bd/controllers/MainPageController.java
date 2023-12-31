package bd.controllers;
import bd.Main;
import bd.TableModels.BalancesTableModel;
import bd.dataAccessor.DataAccessor;
import bd.dataAccessor.DataExporter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class MainPageController {

    /**
     * параметры окна балансов
     */
    public static final int NAVIGATION_PAGE_W = 890;
    public static final int NAVIGATION_PAGE_H = 580;

    /**
     * Переданная root страница
     */
    private static Stage root;

    @FXML
    public Button statistic_button;

    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    public Button go_to_articles_button;
    @FXML
    public Button save_in_file_button;

    public void setRoot(Stage root) {
        create_balance_window.setVisible(false);
        clearInForm();
        MainPageController.root = root;
        initializeTable();
    }

    /**
     * Таблица балансов
     */
    @FXML
    private TableView<BalancesTableModel> balance_table = new TableView<BalancesTableModel>();
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
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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
     * Изменение записи в таблице балансов
     */
    private void editTableRow(int id, LocalDateTime time, int debit, int credit, int amount) {
        balance_table.setEditable(true);
        try {
            BalancesTableModel node = new BalancesTableModel(id, time, debit, credit, amount);
            DataAccessor.getDataAccessor().editRowInBalanceTable(node);
            initializeTable();
        }
        catch (SQLException e) {
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
        }
    }
    /**
     * Добавление записи в таблицу балансов
     */
    private void addTableRow(LocalDateTime time, int debit, int credit, int amount) {
        balance_table.setEditable(true);
        try {
            BalancesTableModel node = new BalancesTableModel(0, time, debit, credit, amount);
            DataAccessor.getDataAccessor().addRowInBalanceTable(node);
            initializeTable();
        }
        catch (SQLException e) {
            alert.setTitle("Предупреждение");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Окно создания нового баланса
     */
    @FXML
    private DialogPane create_balance_window;
    /**
     * Окно изменения баланса
     */
    @FXML
    private DialogPane edit_balance_window;
    /**
     * Поля заполняемые в окне создания и редактирования заявок
     */
    @FXML
    private TextField date_in;
    @FXML
    private TextField debit_in;
    @FXML
    private TextField credit_in;
    @FXML
    private TextField amount_in;
    @FXML
    private TextField id_in_edit;
    @FXML
    private TextField date_in_edit;
    @FXML
    private TextField debit_in_edit;
    @FXML
    private TextField credit_in_edit;
    @FXML
    private TextField amount_in_edit;

    /**
     * Кнопка подтверждения создания нового баланса
     */
    @FXML
    private Button create_new_balance_button;
    @FXML
    private void createNewBalanceOnAction() {
        int debit = Integer.parseInt(debit_in.getText());
        int credit = Integer.parseInt(credit_in.getText());
        int amount = Integer.parseInt(amount_in.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime time = LocalDateTime.parse(date_in.getText(), formatter);
        addTableRow(time, debit, credit, amount);
    }
    /**
     * Кнопка очистки окна создания нового баланса
     */
    @FXML
    private Button clear_new_balance_data;
    /**
     * Очистка полей ввода при создании нового баланса
     */
    @FXML
    private void clearNewBalanceDataOnAction() {
        clearInForm();
    }
    /**
     * Очистка формы для ввода запросов
     */
    @FXML
    private void clearInForm() {
        debit_in.setText("");
        credit_in.setText("");
        amount_in.setText("");
        date_in.setText("");
        id_in_edit.setText("");
        debit_in_edit.setText("");
        credit_in_edit.setText("");
        amount_in_edit.setText("");
        date_in_edit.setText("");
    }

    /**
     * Кнопка изменения баланса
     */
    @FXML
    private Button edit_balance_button;

    /**
     * Изменение баланса на основе данных с полей ввода
     */
    @FXML
    private void editBalanceOnAction() {
        int id = Integer.parseInt(id_in_edit.getText());
        int debit = Integer.parseInt(debit_in_edit.getText());
        int credit = Integer.parseInt(credit_in_edit.getText());
        int amount = Integer.parseInt(amount_in_edit.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime time = LocalDateTime.parse(date_in_edit.getText(), formatter);
        editTableRow(id, time, debit, credit, amount);
    }
    @FXML
    private Button clear_edit_balance_button;
    @FXML
    private void clearEditBalanceDataOnAction() {
        clearInForm();
    }

    /**
     * Кнопка закрытия окна изменения баланса
     */
    @FXML
    private Button close_edit_balance_window_button;
    /**
     * Закрытие окна редактирования баланса
     */
    @FXML
    void closeEditBalanceWindowButton() {
        edit_balance_window.setVisible(false);
        clearInForm();
    }
    /**
     * Кнопка закрытия окна создания баланса
     */
    @FXML
    private Button close_create_balance_window_button;
    /**
     * Закрытие окна создания баланса
     */
    @FXML
    void closeCreateBalanceWindowButton() {
        create_balance_window.setVisible(false);
        clearInForm();
    }

    /**
     * Кнопка открытия диалогового окна создания баланса
     */
    @FXML
    private Button create_balance_button;
    /**
     * Открытие диалогового окна создания баланса
     */
    @FXML
    void createBalanceButtonAction() {
        create_balance_window.setVisible(true);
    }
    /**
     * Кнопка открытия диалогового окна изменения баланса
     */
    @FXML
    private Button change_balance_button;
    /**
     * Открытие диалогового окна изменения баланса
     */
    @FXML
    void changeBalanceButtonAction() {
        edit_balance_window.setVisible(true);
    }

    /**
     * Введенный номер баланса по которому берется статистика
     */
    @FXML
    private TextField balance_statistic_id;
    /**
     * Кнопка перехода к операциям по балансу
     */
    @FXML
    private Button id_statistic_button;
    /**
     * Передача управления в Balance statistic controller
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
            if (balance_statistic_id.getText().isEmpty()) {
                throw new RuntimeException("введите корректный id");
            }
            statistic_id = Integer.parseInt(balance_statistic_id.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            id_statistic_button.setTextFill(Color.RED);
        }

        System.out.println("вызов статистики для id = " + statistic_id);
        controller.setRoot(root, statistic_id);
    }

    public void statisticButtonOnAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("graphStatistic.fxml"));

        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, CursorController.GRAPH_STATISTIC_PAGE_W,
                CursorController.GRAPH_STATISTIC_PAGE_H);
        root.setScene(scene);
        CursorController controller = fxmlLoader.getController();
        controller.setRoot(root);
    }

    public void goToArticles() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("articlesPage.fxml"));
        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, ArticlesController.ARTICLES_PAGE_W,
                ArticlesController.ARTICLES_PAGE_H);
        root.setScene(scene);
        ArticlesController controller = fxmlLoader.getController();
        controller.setRoot(root);
    }

    @FXML
    public void saveInFileOnAction() {
        try {
            DataExporter.exportDataInFile();
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Экспорт данных");
            alert.setContentText("Данные успешно экспортированы");
            alert.showAndWait();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Экспорт данных");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
