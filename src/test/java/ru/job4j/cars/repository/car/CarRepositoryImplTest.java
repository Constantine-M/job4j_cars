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
import ru.job4j.cars.repository.body.BodyRepository;
import ru.job4j.cars.repository.brand.CarBrandRepository;
import ru.job4j.cars.repository.color.CarColorRepository;
import ru.job4j.cars.repository.engine.EngineRepository;
import ru.job4j.cars.repository.gearbox.GearBoxRepository;

import java.time.LocalDate;
import java.util.Optional;

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

    @Autowired
    private GearBoxRepository gearBoxRepository;

    /**
     * Данный тест просто проверяет сохранение
     * машины в БД. Здесь не учитываются
     * связи, т.к. сохранение связанных сущностей
     * происходит в отдельной сессии, что
     * влечет к ошибке после выполнения теста.
     */
    @Test
    void whenCreateCarThenGetTheSame() throws RepositoryException {
        var currentLocalDate = LocalDate.now();
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var gearBox = gearBoxRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car = Car.builder()
                .carYear(2020)
                .model("F150")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
        car.setGearBox(gearBox);
        carRepository.create(car);
        var actual = carRepository.findById(car.getId()).get();
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(car);
    }

    @Test
    void whenFindCarByIdThenGetCarWithModelEqualsToCamry() throws RepositoryException {
        var currentLocalDate = LocalDate.now();
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var gearBox = gearBoxRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car = Car.builder()
                .carYear(2020)
                .model("Camry")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
        car.setGearBox(gearBox);
        carRepository.create(car);
        assertThat(carRepository.findById(car.getId()).get().getModel()).isEqualTo("Camry");
    }

    @Test
    void whenCannotFindCarThenGetAnEmptyOptional() {
        assertThat(carRepository.findById(3)).isEqualTo(Optional.empty());
    }

    @Test
    void whenUpdateCarNameThenGetCherry() throws RepositoryException {
        var currentLocalDate = LocalDate.now();
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var gearBox = gearBoxRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
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
        car.setGearBox(gearBox);
        carRepository.create(car);
        var newBrand = carBrandRepository.findById(3).get();
        car.setModel("Tiggo 9");
        car.setBrand(newBrand);
        carRepository.updateCar(car);
        assertThat(carRepository.findById(car.getId()).get().getBrand().getName()).isEqualTo("Cherry");
    }
}