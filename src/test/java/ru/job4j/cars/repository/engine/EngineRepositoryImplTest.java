package ru.job4j.cars.repository.engine;

import org.junit.jupiter.api.Disabled;
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

    @Test
    void whenSaveEngineThenGetTheSame() {
        var engine = Engine.builder()
                .type("gasoline")
                .capacity(1.5F)
                .horsePower(110)
                .build();
        engineRepository.save(engine);
        assertThat(engineRepository.findById(1)).usingRecursiveComparison().isEqualTo(engine);
    }

    @Test
    void whenSaveTwoEnginesThenGetListOfTwoEngines() {
        var engine1 = Engine.builder()
                .type("gasoline")
                .capacity(1.5F)
                .horsePower(120)
                .build();
        var engine2 = Engine.builder()
                .type("gasoline")
                .capacity(3F)
                .horsePower(180)
                .build();
        var expected = List.of(engine1, engine2);
        engineRepository.save(engine1);
        engineRepository.save(engine2);
        assertThat(engineRepository.findAll()).isEqualTo(expected);
    }

    @Test
    void whenFindById2ThenGet3LEngine() {
        var engine1 = Engine.builder()
                .type("gasoline")
                .capacity(1F)
                .horsePower(65)
                .build();
        var engine2 = Engine.builder()
                .type("gasoline")
                .capacity(3F)
                .horsePower(210)
                .build();
        engineRepository.save(engine1);
        engineRepository.save(engine2);
        assertThat(engineRepository.findById(2)).usingRecursiveComparison().isEqualTo(engine2);
    }
}