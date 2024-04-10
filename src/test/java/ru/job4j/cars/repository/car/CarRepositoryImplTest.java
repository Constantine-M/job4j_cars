package ru.job4j.cars.repository.car;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;


import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

/**
 * @author Constantine on 08.04.2024
 */
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class CarRepositoryImplTest {

    @Autowired
    private CarRepository carRepository;

    /**
     * Данный тест просто проверяет сохранение
     * машины в БД. Здесь не учитываются
     * связи, т.к. сохранение связанных сущностей
     * происходит в отдельной сессии, что
     * влечет к ошибке после выполнения теста.
     */
    @Test
    void whenCreateCarThenGetTheSame() throws RepositoryException {
        var engine = Engine.builder()
                .name("Engine")
                .build();
        var car = Car.builder()
                .engine(engine)
                .name("Fast & Furious")
                .historyOwners(emptyList())
                .build();
        carRepository.create(car);
        assertThat(carRepository.findById(1).get())
                .usingRecursiveComparison()
                .ignoringFields("historyOwners")
                .isEqualTo(car);
    }

    @Test
    void whenFindCarByIdThenGetCarWithNameAudi() throws RepositoryException {
        var engine = Engine.builder()
                .name("Engine")
                .build();
        var car = Car.builder()
                .engine(engine)
                .name("Audi")
                .historyOwners(emptyList())
                .build();
        carRepository.create(car);
        assertThat(carRepository.findById(1).get().getName()).isEqualTo("Audi");
    }

    @Test
    void whenCannotFindCarThenThrowRepositoryException() {
        assertThatThrownBy(() -> carRepository.findById(1))
                .isInstanceOf(RepositoryException.class)
                .hasMessage("Repository exception: cant find car with ID = 1");
    }

    @Test
    void whenUpdateCarNameThenGetGeely() throws RepositoryException {
        var engine = Engine.builder()
                .name("Engine")
                .build();
        var car = Car.builder()
                .engine(engine)
                .name("Audi")
                .historyOwners(emptyList())
                .build();
        carRepository.create(car);
        car.setName("Geely");
        carRepository.updateCar(car);
        assertThat(carRepository.findById(1).get().getName()).isEqualTo("Geely");
    }

    @Test
    void whenFindAllCarsThenGetListOfCarsWithAudiAndGeely() {
        var engine1 = Engine.builder()
                .name("Engine1")
                .build();
        var engine2 = Engine.builder()
                .name("Engine2")
                .build();
        var car1 = Car.builder()
                .engine(engine1)
                .name("Audi")
                .build();
        carRepository.create(car1);
        var car2 = Car.builder()
                .engine(engine2)
                .name("Geely")
                .build();
        carRepository.create(car2);
        var actual = carRepository.findAll();
        var expected = List.of(car1, car2);
        assertThat(actual).isEqualTo(expected);
    }
}