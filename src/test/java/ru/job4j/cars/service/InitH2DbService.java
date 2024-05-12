package ru.job4j.cars.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.Statement;


@Slf4j
@Component
@RequiredArgsConstructor
public class InitH2DbService {

    public static final String H2_DB_PRODUCT_NAME = "H2";

    private final DataSource dataSource;

    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void initDatabase() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            if (isH2Database(connection)) {
                fillCarBrandTable(statement);
                fillColorTable(statement);
                fillBodyTable(statement);
                fillEngineTable(statement);
            } else {
                log.warn("Skipping filling up tables, because it's not H2 database");
            }
        }
    }

    /**
     * Метод проверяет, работаем ли мы именно с
     * базой данных H2.
     */
    @SneakyThrows
    private boolean isH2Database(Connection connection) {
        return H2_DB_PRODUCT_NAME.equals(connection.getMetaData().getDatabaseProductName());
    }

    @SneakyThrows
    private void executeStatement(Statement statement, String sql) {
        statement.executeUpdate(sql);
    }

    /**
     * Заполнить таблицу car_brand
     * тестовыми данными.
     */
    private void fillCarBrandTable(Statement statement) {
        String sql = """
                    INSERT INTO car_brand (name)
                        VALUES
                             ('Toyota'),
                             ('Volkswagen'),
                             ('Cherry');
                    """;
        executeStatement(statement, sql);
    }

    /**
     * Заполнить таблицу color
     * тестовыми данными.
     */
    private void fillColorTable(Statement statement) {
        String sql = """
                    INSERT INTO color (name)
                    VALUES
                        ('red'),
                        ('green'),
                        ('blue');
                    """;
        executeStatement(statement, sql);
    }

    /**
     * Заполнить таблицу body
     * тестовыми данными.
     */
    private void fillBodyTable(Statement statement) {
        String sql = """
                    INSERT INTO body (name)
                    VALUES
                        ('sedan'),
                        ('hatchback'),
                        ('pick-up');
                    """;
        executeStatement(statement, sql);
    }

    /**
     * Заполнить таблицу engine
     * тестовыми данными.
     */
    private void fillEngineTable(Statement statement) {
        String sql = """
                    INSERT INTO engine (capacity, horse_power, fuel_type)
                    VALUES
                        (1.8, 140, 'gasoline'),
                        (2.0, 180, 'diesel'),
                        (1.6, 81, 'gasoline');
                    """;
        executeStatement(statement, sql);
    }
}
