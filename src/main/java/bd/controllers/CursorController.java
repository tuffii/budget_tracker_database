package bd.controllers;

import bd.Main;
import bd.TableModels.ArticlesTableModel;
import bd.TableModels.BalancesTableModel;
import bd.dataAccessor.DataAccessor;
import bd.dataAccessor.DataFormater;
import javafx.application.Platform;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CursorController {

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
    @FXML
    public PieChart pieChart;

    public void setRoot(Stage root) {
        CursorController.root = root;
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


    private void initializePieChart(Map<Integer, Double> pieData_id_value) {
        pieChart.getData().clear();
//        try {
            pieData_id_value.forEach((key, value) -> {
                try {
                    String name = DataAccessor.getDataAccessor().getStateNameFromId(key);
                    System.out.println(name + " " + value);
                    Platform.runLater( () ->{
                            pieChart.getData().add(new PieChart.Data(name, value));
                            });

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        pieData_id_value.clear();
    }



    private void initializeTypeCheckBox() {
        selectType.getItems().addAll("debit", "credit", "amount");
    }




    public void goToMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("navigationPage.fxml"));

        AnchorPane rootLayout = fxmlLoader.load();
        Scene scene = new Scene(rootLayout, MainPageController.NAVIGATION_PAGE_W,
                MainPageController.NAVIGATION_PAGE_H);
        root.setScene(scene);
        MainPageController controller = fxmlLoader.getController();
        controller.setRoot(root);

    }

    @FXML
    public void callCursor() {
        try {
            if (selectType.getSelectionModel().isEmpty() || selectedArticlesId.isEmpty()) {
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

            Map<Integer, Double> pieData = parseFinancialData(result);
            System.out.println(result);
            System.out.println("\n" + pieData.toString());
            initializePieChart(pieData);


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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Map<Integer, Double> parseFinancialData(String text) {
        Map<Integer, Double> financialDataMap = new HashMap<>();

        // Паттерн для поиска строк вида "Article ID %d: Financial Percentage = %f\n"
        String patternString = "Article ID (\\d+): Financial Percentage = (\\d+([.,]\\d+)?)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);

        // Используем DecimalFormat с учетом локали для правильного парсинга чисел
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.getDefault()));

        // Поиск и обработка совпадений
        while (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            String percentageStr = matcher.group(2).replace(',', '.'); // Заменяем запятую на точку
            double percentage = Double.parseDouble(percentageStr);
            financialDataMap.put(id, percentage);
        }

        return financialDataMap;
    }



}
