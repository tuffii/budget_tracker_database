package bd.dataAccessor;

import bd.TableModels.ArticlesTableModel;
import bd.TableModels.BalancesTableModel;
import bd.TableModels.CursorDataOutput;
import bd.TableModels.OperationsTableModel;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataAccessor {

    private static final DataAccessor dataAccessor = new DataAccessor(
            "jdbc:postgresql://localhost:5432/budget_1", "postgres", "1122");

    private Connection connection;

    public static DataAccessor getDataAccessor() {
        return dataAccessor;
    }

    private DataAccessor(String dataBaseURL, String user, String password) {
        try {
            connection = DriverManager.getConnection(dataBaseURL, user, password);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    };

    public List<BalancesTableModel> getBalanceTable() throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("read")) {
            throw new SQLException("у вас нет доступа к чтению БД");
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from balance");

        List<BalancesTableModel> balancesTableList = new ArrayList<>();
        while (resultSet.next()) {
            BalancesTableModel node = new BalancesTableModel(
                    resultSet.getInt("id"),
                    resultSet.getTimestamp("create_date").toLocalDateTime(),
                    resultSet.getInt("debit"),
                    resultSet.getInt("credit"),
                    resultSet.getInt("amount"));
            balancesTableList.add(node);
        }
        return balancesTableList;
    }

    public List<OperationsTableModel> getOperationTable() throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("read")) {
            throw new SQLException("у вас нет доступа к чтению БД");
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from operations");

        List<OperationsTableModel> operationsTableList = new ArrayList<>();
        while (resultSet.next()) {
            OperationsTableModel node = new OperationsTableModel(
                    resultSet.getInt("id"),
                    resultSet.getInt("article_id"),
                    resultSet.getInt("debit"),
                    resultSet.getInt("credit"),
                    resultSet.getTimestamp("create_date").toLocalDateTime(),
                    resultSet.getInt("balance_id"));
            operationsTableList.add(node);
        }
        return operationsTableList;
    }

    public List<OperationsTableModel> getOperationWithIdTable(int balance_id) throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("read")) {
            throw new SQLException("у вас нет доступа к чтению БД");
        }

        String sql = "SELECT * FROM operations WHERE balance_id = ?";
        List<OperationsTableModel> operationsTableList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, balance_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    OperationsTableModel node = new OperationsTableModel(
                            resultSet.getInt("id"),
                            resultSet.getInt("article_id"),
                            resultSet.getInt("debit"),
                            resultSet.getInt("credit"),
                            resultSet.getTimestamp("create_date").toLocalDateTime(),
                            resultSet.getInt("balance_id"));
                    operationsTableList.add(node);
                }
            }
        }
        return operationsTableList;
    }

    public void addRowInBalanceTable(BalancesTableModel data) throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("write")) {
            throw new SQLException("у вас нет доступа к чтению БД");
        }

        String sqlRequest = "INSERT INTO balance (create_date, debit, credit, amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(data.getDate()));
            preparedStatement.setInt(2, data.getDebit());
            preparedStatement.setInt(3, data.getCredit());
            preparedStatement.setInt(4, data.getAmount());
            preparedStatement.executeUpdate();
        }
    }

    public void editRowInBalanceTable(BalancesTableModel data) throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("write")) {
            throw new SQLException("у вас нет доступа к изменению БД");
        }
        String sqlRequest = "UPDATE balance SET create_date=?, debit=?, credit=?, amount=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(data.getDate()));
            preparedStatement.setInt(2, data.getDebit());
            preparedStatement.setInt(3, data.getCredit());
            preparedStatement.setInt(4, data.getAmount());
            preparedStatement.setInt(5, data.getId());
            preparedStatement.executeUpdate();
        }
    }


    public void addRowInOperationTable(OperationsTableModel data) throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("write")) {
            throw new SQLException("у вас нет доступа к изменению БД");
        }

        String sqlRequest = "INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setInt(1, data.getArticle_id());
            preparedStatement.setInt(2, data.getDebit());
            preparedStatement.setInt(3, data.getCredit());
            preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(data.getDate()));
            preparedStatement.setInt(5, data.getBalance_id());
            preparedStatement.executeUpdate();
        }
    }

    public void editRowInOperationTable(OperationsTableModel data) throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("write")) {
            throw new SQLException("у вас нет доступа к изменению БД");
        }

        String sqlRequest = "UPDATE operations SET article_id=?, debit=?, credit=?, create_date=?, balance_id=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setInt(6, data.getId());
            preparedStatement.setInt(1, data.getArticle_id());
            preparedStatement.setInt(2, data.getDebit());
            preparedStatement.setInt(3, data.getCredit());
            System.out.println(java.sql.Timestamp.valueOf(data.getDate()));
            preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(data.getDate()));
            preparedStatement.setInt(5, data.getBalance_id());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteRowFromBalanceTable(int id) throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("write")) {
            throw new SQLException("у вас нет доступа к изменению БД");
        }

        String sqlRequest = "DELETE FROM balance WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteRowFromOperationsTable(int id) throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("write")) {
            throw new SQLException("у вас нет доступа к изменению БД");
        }

        String sqlRequest = "DELETE FROM operations WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }



    public List<ArticlesTableModel> getStatesTable() throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("read")) {
            throw new SQLException("у вас нет доступа к чтению БД");
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from articles");

        List<ArticlesTableModel> articlesTableList = new ArrayList<>();
        while (resultSet.next()) {
            ArticlesTableModel node = new ArticlesTableModel(
                    resultSet.getInt("id"),
                    resultSet.getString("name"));
            articlesTableList.add(node);
        }
        return articlesTableList;
    }

    public int getStateIdFromName(String stateName) throws SQLException {

        if (!UserRole.getIsAdmin() && !UserRole.userRoleAccess.get("read")) {
            throw new SQLException("у вас нет доступа к чтению БД");
        }

        String sqlRequest = "SELECT id FROM articles WHERE name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setString(1, stateName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Статья с именем " + stateName + " не найдена.");
                }
            }
        }
    }


    /**
     * Метод для получения пароля по имени пользователя
      */
    public String getPassword(String username) throws SQLException {
        String sqlRequest = "SELECT password FROM users WHERE username=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("password");
                } else {
                    throw new SQLException("Пользователь с именем " + username + " не найден.");
                }
            }
        }
    }

    // Метод для создания новой записи пользователя
    public void createUser(String username, String password) throws SQLException {
        String sqlRequest = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            // Выполнение оператора INSERT
            preparedStatement.executeUpdate();
            System.out.println("Пользователь успешно создан.");
        }
    }


    public static String calculateFinancialPercentage(
            Timestamp start_date,
            Timestamp end_date,
            int[] article_ids,
            String flow_type) throws SQLException {
        try {
            // Calculate total flow for selected articles
            double totalFlow = 0.0;
            String totalFlowQuery = "SELECT COALESCE(SUM(CASE " +
                    "WHEN ? = 'debit' THEN o.debit " +
                    "WHEN ? = 'credit' THEN o.credit " +
                    "WHEN ? = 'amount' THEN b.amount " +
                    "ELSE 0 " +
                    "END), 0) " +
                    "AS total_flow " +
                    "FROM balance b " +
                    "LEFT JOIN operations o ON o.balance_id = b.id " +
                    "WHERE b.create_date BETWEEN ? AND ? " +
                    "AND o.article_id IN (" + String.join(",", Arrays.stream(article_ids).mapToObj(String::valueOf).toArray(String[]::new)) + ")";
            try (PreparedStatement totalFlowStatement = DataAccessor.dataAccessor.connection.prepareStatement(totalFlowQuery)) {
                totalFlowStatement.setString(1, flow_type);
                totalFlowStatement.setString(2, flow_type);
                totalFlowStatement.setString(3, flow_type);
                totalFlowStatement.setTimestamp(4, start_date);
                totalFlowStatement.setTimestamp(5, end_date);
                try (ResultSet totalFlowResult = totalFlowStatement.executeQuery()) {
                    if (totalFlowResult.next()) {
                        totalFlow = totalFlowResult.getDouble("total_flow");
                    }
                }
            }

            // Calculate financial percentage for each article
            StringBuilder resultStringBuilder = new StringBuilder();
            for (int article_id_param : article_ids) {
                String articlePercentageQuery = "SELECT COALESCE(SUM(CASE " +
                        "WHEN ? = 'debit' THEN o.debit " +
                        "WHEN ? = 'credit' THEN o.credit " +
                        "WHEN ? = 'amount' THEN b.amount " +
                        "ELSE 0 " +
                        "END), 0) / ? * 100 " +
                        "AS financial_percentage " +
                        "FROM operations o " +
                        "LEFT JOIN balance b ON o.balance_id = b.id " +
                        "WHERE o.article_id = ? " +
                        "AND b.create_date BETWEEN ? AND ?";
                try (PreparedStatement articlePercentageStatement = DataAccessor.dataAccessor.connection.prepareStatement(articlePercentageQuery)) {
                    articlePercentageStatement.setString(1, flow_type);
                    articlePercentageStatement.setString(2, flow_type);
                    articlePercentageStatement.setString(3, flow_type);
                    articlePercentageStatement.setDouble(4, totalFlow);
                    articlePercentageStatement.setInt(5, article_id_param);
                    articlePercentageStatement.setTimestamp(6, start_date);
                    articlePercentageStatement.setTimestamp(7, end_date);
                    try (ResultSet articlePercentageResult = articlePercentageStatement.executeQuery()) {
                        if (articlePercentageResult.next()) {
                            double financialPercentage = articlePercentageResult.getDouble("financial_percentage");
                            resultStringBuilder.append(String.format("Article ID %d: Financial Percentage = %f\n", article_id_param, financialPercentage));
                        }
                    }
                }
            }
            return resultStringBuilder.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error occurred while calculating financial percentage.");
        }
    }








}
