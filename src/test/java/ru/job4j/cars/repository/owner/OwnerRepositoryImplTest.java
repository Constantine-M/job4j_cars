package ru.job4j.cars.repository.owner;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.HistoryOwner;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.car.CarRepository;
import ru.job4j.cars.repository.engine.EngineRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
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
 * @author Constantine on 31.03.2024
 */
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class OwnerRepositoryImplTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private EngineRepository engineRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void whenCreateOwnerThenGetTheSame() throws RepositoryException {
        try {
            var engine = Engine.builder()
                    .name("Inline-6 Turbo 3.0L")
                    .build();
            engineRepository.save(engine);
            var car = Car.builder()
                    .engine(engine)
                    .name("Mercedes-Benz E-Class 2019")
                    .historyOwners(emptyList())
                    .build();
            carRepository.create(car);
            var owner = Owner.builder()
                    .name("Consta")
                    .historyOwners(emptyList())
                    .build();
            ownerRepository.create(owner);
            assertThat(ownerRepository.findById(1).get()).usingRecursiveComparison().isEqualTo(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}