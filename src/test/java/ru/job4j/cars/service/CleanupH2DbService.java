package ru.job4j.cars.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Данный класс описывает непосредственно сервис
 * очистки тестовой БД H2.
 *
 * ИМЯ СХЕМЫ H2 ПО УМОЛЧАНИЮ = PUBLIC.
 *
 * @author Constantine on 24.03.2024
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CleanupH2DbService {

    public static final String H2_DB_PRODUCT_NAME = "H2";

    private final DataSource dataSource;

    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void cleanup(String schemaName) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            if (isH2Database(connection)) {
                disableConstraints(statement);
                truncateTables(statement, schemaName);
                enableConstraints(statement);
            } else {
                log.warn("Skipping cleaning up database, because it's not H2 database");
            }
        }
    }

    @SneakyThrows
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void dropTables(String schemaName) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            if (isH2Database(connection)) {
                disableConstraints(statement);
                dropAllTables(statement, schemaName);
                enableConstraints(statement);
            } else {
                log.warn("Skipping cleaning up database, because it's not H2 database");
            }
        }
    }

    /**
     * Метод ДОЛЖЕН сбрасывать последовательность
     * в таблице, чтобы после очистки таблицы
     * первая запись имела ID = 1.
     *
     * Но у меня почему-то это не работает..
     * Удалять пока не буду.
     *
     * @param schemaName имя схемы тестовой БД (по умолчанию PUBLIC)
     */
    private void resetSequences(Statement statement, String schemaName) {
        getSchemaSequences(statement, schemaName).forEach(sequenceName ->
                executeStatement(statement, String.format("ALTER SEQUENCE %s RESTART WITH 1", sequenceName)));
    }

    /**
     * Данный метод очищает таблицу и СБРАСЫВАЕТ
     * СЧЕТЧИК (последовательность).
     *
     * В это методе я просто добавил "..RESTART IDENTITY",
     * чтобы он заработал как надо.
     */
    private void truncateTables(Statement statement, String schemaName) {
        var schemaTables = getSchemaTables(statement, schemaName);
        schemaTables.forEach(tableName -> executeStatement(statement, "TRUNCATE TABLE " + tableName + " RESTART IDENTITY"));
    }

    private void dropAllTables(Statement statement, String schemaName) {
        getSchemaTables(statement, schemaName)
                .forEach(tableName -> executeStatement(statement, "DROP TABLE " + tableName));
    }

    /**
     * Данный метод ВКЛЮЧАЕТ внешние ключи.
     *
     * Метод работает на всю базу данных.
     * Он нужен, чтобы обеспечить целостность ссылочной
     * целостности в базе данных.
     *
     * При установке REFERENTIAL_INTEGRITY TRUE в
     * базе данных, система будет соблюдать правила,
     * согласно которым значения внешних ключей
     * в одной таблице должны ссылаться на
     * существующие значения в соответствующей
     * связанной таблице.
     */
    private void enableConstraints(Statement statement) {
        executeStatement(statement, "SET REFERENTIAL_INTEGRITY TRUE");
    }

    /**
     * Данный метод ВЫКЛЮЧАЕТ внешние ключи.
     *
     * Таким образом, таблицы становятся независимы
     * друг от друга и в этом случае мы легко можем
     * их очистить.
     *
     * После очистки внешние ключи нужно бдует
     * включить обратно!
     */
    private void disableConstraints(Statement statement) {
        executeStatement(statement, "SET REFERENTIAL_INTEGRITY FALSE");
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
     * Получить список всех таблиц в схеме.
     *
     * По умолчанию schemaName = PUBLIC.
     *
     * @return список всех таблиц в схеме (schema).
     */
    @SneakyThrows
    private Set<String> getSchemaTables(Statement statement, String schemaName) {
        String sql = String.format("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='%s'", schemaName);
        return queryForList(statement, sql);
    }
    @SneakyThrows
    private Set<String> getSchemaSequences(Statement statement, String schemaName) {
        String sql = String.format("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='%s'", schemaName);
        return queryForList(statement, sql);
    }

    /**
     * В данном методе я умышленно удалил
     * из результирующего Set<String>
     * нужные мне таблицы, а именно
     * "ENGINE", "COLOR", "BODY", "CAR_BRAND",
     * потому что они заполнены заранее.
     *
     * Общее исполнение без изменений -
     * просто вернул кастомный сет.
     */
    @SneakyThrows
    private Set<String> queryForList(Statement statement, String sql) {
        Set<String> tables = new HashSet<>();
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }
        log.warn("Deleting tables from Set \"ENGINE\", \"COLOR\", \"BODY\", \"CAR_BRAND\". These tables wont be cleaned up!");
        tables.remove("ENGINE");
        tables.remove("COLOR");
        tables.remove("BODY");
        tables.remove("CAR_BRAND");
        return tables;
    }
}
