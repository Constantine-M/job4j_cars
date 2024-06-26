package ru.job4j.cars.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import ru.job4j.cars.service.CleanupH2DbService;
import ru.job4j.cars.service.InitH2DbService;

/**
 * Данный класс-слушатель позволяет реагировать
 * на различные события, происходящие во время
 * выполнения тестов.
 *
 * Чтобы получить эти возможности, мы использовали
 * интерфейс {@link TestExecutionListener}.
 * Собсно он и позволяет разработчикам управлять
 * выполнением тестов и обрабатывать события
 * до и после их запуска.
 *
 * Здесь мы реализовали только метод
 * {@link TestExecutionListener#beforeTestMethod},
 * чтобы очищать ВСЕ наши таблицы перед очередным
 * тестовым методом.
 *
 * @author Constantine on 24.03.2024
 */
@Slf4j
public class CleanupH2DatabaseTestListener implements TestExecutionListener, Ordered {

    private static final String H2_SCHEMA_NAME = "PUBLIC";

    /**
     * Заполнение некоторых таблиц
     * тестовыми данными перед выполнение
     * тестового класса.
     */
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        TestExecutionListener.super.beforeTestClass(testContext);
    }

    /**
     * Очистка всех таблиц после выполнения
     * тестового метода.
     */
    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        TestExecutionListener.super.afterTestMethod(testContext);
        cleanupDatabase(testContext);
    }

    /**
     * Заполнение некоторых таблиц
     * тестовыми данными перед выполнение
     * тестового метода.
     */
    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        TestExecutionListener.super.beforeTestMethod(testContext);
        initDatabase(testContext);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        TestExecutionListener.super.afterTestClass(testContext);
        cleanupDatabase(testContext);
    }

    /**
     * Данный метод по сути вызывает непосредственно
     * сервис очистки тестовой БД H2.
     *
     * Мы получаем бин {@link CleanupH2DbService}
     * и вызываем у него метод {@link CleanupH2DbService#cleanup}.
     *
     * @param testContext - это application context, который
     *                    поднимается в тестовой среде.
     */
    private void cleanupDatabase(TestContext testContext) {
        log.info("Cleaning up database begin");
        CleanupH2DbService cleanupH2DbService = testContext.getApplicationContext().getBean(CleanupH2DbService.class);
        cleanupH2DbService.cleanup(H2_SCHEMA_NAME);
        log.info("Cleaning up database end");
    }

    private void initDatabase(TestContext testContext) {
        log.info("Filling tables (ENGINE, COLOR, BODY, CAR_BRAND, GEAR_BOX) begin");
        InitH2DbService initH2DbService = testContext.getApplicationContext().getBean(InitH2DbService.class);
        initH2DbService.initDatabase();
        log.info("Filling tables (ENGINE, COLOR, BODY, CAR_BRAND, GEAR_BOX) end");
    }

    private void dropDatabase(TestContext testContext) {
        log.info("Drop database tables begin");
        CleanupH2DbService cleanupH2DbService = testContext.getApplicationContext().getBean(CleanupH2DbService.class);
        cleanupH2DbService.cleanup(H2_SCHEMA_NAME);
        log.info("Dropping tables end");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
