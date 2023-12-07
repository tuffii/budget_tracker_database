package bd.dataAccessor;

import bd.TableModels.BalancesTableModel;

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


    public void addOrEditRowInBalanceTable(BalancesTableModel data) throws SQLException {
        String sqlRequest;
        if (data.getId() > 0) {
            sqlRequest = "UPDATE balance SET create_date=?, debit=?, credit=?, amount=? WHERE id=?";
        } else {
            sqlRequest = "INSERT INTO balance (create_date, debit, credit, amount) VALUES (?, ?, ?, ?)";
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(data.getDate()));
            preparedStatement.setInt(2, data.getDebit());
            preparedStatement.setInt(3, data.getCredit());
            preparedStatement.setInt(4, data.getAmount());

            if (data.getId() > 0) {
                // Если это редактирование, установите id для условия WHERE
                preparedStatement.setInt(5, data.getId());
            }

            // Выполните запрос
            System.out.println(preparedStatement.toString());
            preparedStatement.executeUpdate();
        }
    }







}
