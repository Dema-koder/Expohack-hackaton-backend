package ru.demyan;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class Main {

    private final JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        try {
            File folder = new File("/Users/demanzverev/IdeaProjects/Expohack-hackaton/src/main/resources/tables");
            File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".csv"));
            if (listOfFiles == null || listOfFiles.length == 0) {
                log.error("No CSV files found in the specified directory: {}", folder.getAbsolutePath());
                return;
            }
            log.info("{} files found", listOfFiles.length);

            for (File file : listOfFiles) {
                List<String[]> csvData = readCsv(file.getAbsolutePath());
                if (!csvData.isEmpty()) {
                    String[] headers = csvData.get(0);
                    String tableName = getTableName(file.getName());

                    createTable(tableName, headers);
                    clearTable(tableName);

                    insertData(tableName, headers, csvData.subList(1, csvData.size()));
                }
            }
            clearTable("client");
            clearTable("service");
        } catch (IOException | CsvException e) {
            log.error("Error occurred while processing files", e);
        }
    }

    private List<String[]> readCsv(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        }
    }

    private String getTableName(String fileName) {
        return fileName.replace(".csv", "").replaceAll("[^a-zA-Z0-9_]", "_");
    }

    private void createTable(String tableName, String[] headers) {
        String columns = String.join(" VARCHAR(255), ", headers) + " VARCHAR(255)";
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", tableName, columns);
        jdbcTemplate.execute(sql);
    }

    private void clearTable(String tableName) {
        String sql = String.format("TRUNCATE TABLE %s", tableName);
        jdbcTemplate.execute(sql);
    }

    private void insertData(String tableName, String[] headers, List<String[]> data) {
        String columns = String.join(", ", headers);
        String placeholders = String.join(", ", headers).replaceAll("[^,]+", "?");
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);

        data.forEach(row -> jdbcTemplate.update(sql, (Object[]) row));
    }
}