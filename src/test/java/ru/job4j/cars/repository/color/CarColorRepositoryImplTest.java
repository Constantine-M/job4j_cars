package ru.job4j.cars.repository.color;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.CarColor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
class CarColorRepositoryImplTest {

    @Autowired
    private CarColorRepository carColorRepository;

    @Test
    void whenFindAllColorsThenGetResultList() {
        var color1 = CarColor.builder()
                .name("blue")
                .build();
        var color2 = CarColor.builder()
                .name("green")
                .build();
        var color3 = CarColor.builder()
                .name("red")
                .build();
        var expected = List.of(color1, color2, color3);
        assertThat(carColorRepository.findAll())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void findById() {
        var expectedColor = CarColor.builder()
                .id(1)
                .name("red")
                .build();
        assertThat(carColorRepository.findById(1).get())
                .isEqualTo(expectedColor);
    }
}