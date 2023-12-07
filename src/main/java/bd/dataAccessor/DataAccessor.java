package bd.dataAccessor;

import bd.TableModels.BalancesTableModel;
import bd.TableModels.OperationsTableModel;

import java.sql.*;
import java.util.ArrayList;
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

}
