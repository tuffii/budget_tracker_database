package bd.dataAccessor;

import bd.TableModels.ArticlesTableModel;
import bd.TableModels.BalancesTableModel;
import bd.TableModels.BaseTableModel;
import bd.TableModels.OperationsTableModel;
import bd.dataAccessor.DataAccessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.List;

public class DataExporter {

    public static void exportToCSV(List<? extends BaseTableModel> dataList, String fileName) {
        // Создаем StringBuilder для формирования содержимого файла
        StringBuilder content = new StringBuilder();

        // Добавляем заголовок CSV файла (первая строка)
        content.append(dataList.get(0).getCsvHeader()).append("\n");

        // Добавляем данные в файл
        for (BaseTableModel data : dataList) {
            content.append(data.toCsvString()).append("\n");
        }

        // Записываем содержимое в файл
        try {
            Path filePath = Path.of(fileName);
            Files.writeString(filePath, content.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Данные успешно записаны в файл: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportDataInFile() throws SQLException {
        List<BalancesTableModel> balancesTable = DataAccessor.getDataAccessor().getBalanceTable();
        List<OperationsTableModel> operationsTable = DataAccessor.getDataAccessor().getOperationTable();
        List<ArticlesTableModel> articlesTable = DataAccessor.getDataAccessor().getStatesTable();

        Path outputDir = Paths.get("outputFiles");
        if (!Files.exists(outputDir)) {
            try {
                Files.createDirectories(outputDir);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        exportToCSV(balancesTable, String.valueOf(outputDir.resolve("balances_table.csv")));
        exportToCSV(operationsTable, String.valueOf(outputDir.resolve("operations_table.csv")));
        exportToCSV(articlesTable, String.valueOf(outputDir.resolve("articles_table.csv")));
    }
}
