package ru.job4j.cars.repository.car;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.AutoPassport;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.body.BodyRepository;
import ru.job4j.cars.repository.brand.CarBrandRepository;
import ru.job4j.cars.repository.color.CarColorRepository;
import ru.job4j.cars.repository.engine.EngineRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Constantine on 08.04.2024
 */
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
        var actual = carRepository.findById(car.getId()).get();
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(car);
    }

    @Test
    void whenFindCarByIdThenGetCarWithModelEqualsToCamry() throws RepositoryException {
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
                .model("Camry")
                .mileage(321000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
        carRepository.create(car);
        assertThat(carRepository.findById(car.getId()).get().getModel()).isEqualTo("Camry");
    }

    @Test
    void whenCannotFindCarThenGetAnEmptyOptional() {
        assertThat(carRepository.findById(3)).isEqualTo(Optional.empty());
    }

    @Test
    void whenUpdateCarNameThenGetCherry() throws RepositoryException {
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
                .model("Camry")
                .mileage(321000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
        carRepository.create(car);
        var newBrand = carBrandRepository.findById(3).get();
        car.setModel("Tiggo 9");
        car.setBrand(newBrand);
        carRepository.updateCar(car);
        assertThat(carRepository.findById(car.getId()).get().getBrand().getName()).isEqualTo("Cherry");
    }
}