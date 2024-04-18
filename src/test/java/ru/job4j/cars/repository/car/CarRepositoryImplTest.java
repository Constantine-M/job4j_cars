package ru.job4j.cars.repository.car;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.*;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

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
        var owner = Owner.builder()
                .name("owner")
                .start(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MINUTES))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
                .type("diesel")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
                .body("pickup")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .color("red")
                .model("Ford F150")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        carRepository.create(car);
        var actual = carRepository.findById(car.getId()).get();
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(car);
    }

    @Test
    void whenFindCarByIdThenGetCarWithNameAudi() throws RepositoryException {
        var owner = Owner.builder()
                .name("owner")
                .start(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MINUTES))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
                .type("gasoline")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
                .body("sedan")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .color("orange")
                .model("Audi")
                .mileage(64000)
                .engine(engine)
                .body(body)
                .passport(passport)
                .build();
        carRepository.create(car);
        assertThat(carRepository.findById(1).get().getModel()).isEqualTo("Audi");
    }

    @Test
    void whenCannotFindCarThenThrowRepositoryException() {
        assertThatThrownBy(() -> carRepository.findById(1))
                .isInstanceOf(RepositoryException.class)
                .hasMessage("Repository exception: cant find car with ID = 1");
    }

    @Test
    void whenUpdateCarNameThenGetGeely() throws RepositoryException {
        var owner = Owner.builder()
                .name("owner")
                .start(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MINUTES))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
                .type("gasoline")
                .capacity(1.5F)
                .horsePower(130)
                .build();
        var body = Body.builder()
                .body("SUV")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .color("blue")
                .model("Audi")
                .mileage(5000)
                .engine(engine)
                .body(body)
                .passport(passport)
                .build();
        carRepository.create(car);
        car.setModel("Geely");
        carRepository.updateCar(car);
        assertThat(carRepository.findById(1).get().getModel()).isEqualTo("Geely");
    }
}