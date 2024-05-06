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
import ru.job4j.cars.repository.body.BodyRepository;
import ru.job4j.cars.repository.brand.CarBrandRepository;
import ru.job4j.cars.repository.color.CarColorRepository;
import ru.job4j.cars.repository.engine.EngineRepository;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Constantine on 08.04.2024
 */
@Disabled
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class CarRepositoryImplTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BodyRepository bodyRepository;

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private CarColorRepository carColorRepository;

    @Autowired
    private EngineRepository engineRepository;

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
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .model("F150")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
        carRepository.create(car);
        var actual = carRepository.findById(1).get();
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
                .fuelType("gasoline")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
                .name("sedan")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var carBrand = CarBrand.builder()
                .name("Audi")
                .build();

        var car = Car.builder()
                .model("A8")
                .mileage(64000)
                .engine(engine)
                .body(body)
                .passport(passport)
                .build();
        carRepository.create(car);
        assertThat(carRepository.findById(1).get().getModel()).isEqualTo("Audi");
    }

    @Test
    void whenCannotFindCarThenGetAnEmptyOptional() {
        assertThat(carRepository.findById(3)).isEqualTo(Optional.empty());
    }

    @Test
    void whenUpdateCarNameThenGetGeely() throws RepositoryException {
        var owner = Owner.builder()
                .name("owner")
                .start(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MINUTES))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
                .fuelType("gasoline")
                .capacity(1.5F)
                .horsePower(130)
                .build();
        var body = Body.builder()
                .name("SUV")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var carBrand = CarBrand.builder()
                .name("Audi")
                .build();
        var color = CarColor.builder()
                .name("white")
                .build();
        var car = Car.builder()
                .brand(carBrand)
                .carColor(color)
                .model("A4")
                .mileage(5000)
                .engine(engine)
                .body(body)
                .passport(passport)
                .build();
        carRepository.create(car);
        var newBrand = CarBrand.builder()
                .name("Geely")
                .build();
        car.setModel("Manjaro");
        car.setBrand(newBrand);
        carRepository.updateCar(car);
        assertThat(carRepository.findById(1).get().getBrand().getName()).isEqualTo("Geely");
    }
}