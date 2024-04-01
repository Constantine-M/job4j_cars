package ru.job4j.cars.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.job4j.cars.service.CleanupH2DbService;

import javax.sql.DataSource;

/**
 * @author Constantine on 24.03.2024
 */
@Configuration
public class DatasourceConfiguration {

    /**
     * С помощью библиотеки apache.commons.dbcp2
     * мы можем воспользоваться методами
     * класса {@link BasicDataSource} и
     * настроить базовые параметры для
     * подключения к БД. Т.о. мы настроили
     * наш пул.
     *
     * Заранее был добавлен файл application.yml
     * в тестовую среду с настройками подключения
     * к БД H2.
     *
     * Данный метод требуется для работы сервиса
     * {@link CleanupH2DbService}.
     *
     * Использовать {@link SessionFactory}
     * не пробовал, т.к. уже более недели сижу
     * над задачей!
     */
    @Bean
    public DataSource connectionPool(@Value("${spring.datasource.url}") String url,
                                     @Value("${spring.datasource.username}") String username,
                                     @Value("${spring.datasource.password}") String password) {
        return new BasicDataSource() {
            {
                setUrl(url);
                setUsername(username);
                setPassword(password);
            }
        };
    }
}
