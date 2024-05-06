package ru.job4j.cars.repository.engine;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;

import java.util.Collections;

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
@Disabled
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class EngineRepositoryImplTest {

    @Autowired
    private EngineRepository engineRepository;

    @Test
    void whenSaveTwoEnginesThenGetListOfEngines() {
        var expected = Collections.emptyList();
        assertThat(engineRepository.findAll()).isEqualTo(expected);
    }

    @Test
    void whenFindById2ThenGetEngine() {
        var engine2 = engineRepository.findById(2).get();
        System.out.println();
        assertThat(engineRepository.findById(2).get()).usingRecursiveComparison().isEqualTo(engine2);
    }
}