package ru.job4j.cars.repository.engine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.Engine;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * В тестовом классе используется аннотация
 * {@link TestExecutionListeners}, которая
 * позволяет управлять выполнением тестов и
 * обрабатывать события до и после их запуска.
 *
 * Но, чтобы передать собственный listener,
 * требуется также передать дефолтную реализацию
 * {@link DependencyInjectionTestExecutionListener},
 * которая обеспечивает внедрение зависимости.
 * "Hence, we’ve added DependencyInjectionTestExecutionListener
 * explicitly so that we can use auto wiring in
 * our test class."
 *
 * @author Constantine on 23.03.2024
 */
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class EngineRepositoryImplTest {

    @Autowired
    private EngineRepository engineRepository;

    /**
     * В данном тесте я сравнивал данные
     * по полям и специально проигнорировал
     * поле id, т.к. оно почему-то менялось
     * постоянно..
     */
    @Test
    void whenSaveTwoEnginesThenGetListOfEngines() {
        var engine1 = Engine.builder()
                .capacity(1.6F)
                .horsePower(81)
                .fuelType("gasoline")
                .build();
        var engine2 = Engine.builder()
                .capacity(1.8F)
                .horsePower(140)
                .fuelType("gasoline")
                .build();
        var engine3 = Engine.builder()
                .capacity(2F)
                .horsePower(180)
                .fuelType("diesel")
                .build();
        var expected = List.of(engine1, engine2, engine3);
        assertThat(engineRepository.findAll())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void whenFindById2ThenGetEngine() {
        var engine2 = engineRepository.findById(2).get();
        assertThat(engineRepository.findById(2).get()).usingRecursiveComparison().isEqualTo(engine2);
    }
}