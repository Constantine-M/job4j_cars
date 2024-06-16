package ru.job4j.cars.repository.car;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 05.03.2024
 */
@Slf4j
@AllArgsConstructor
@Repository
public class CarRepositoryImpl implements CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить машину.
     */
    @Override
    public Car create(Car car) {
        crudRepository.run(session -> session.save(car));
        return car;
    }

    @Override
    public void updateCar(Car car) {
        crudRepository.run(session -> session.update(car));
    }

    @Override
    public Optional<Car> findById(int id) {
        String hql = """
                    FROM Car car
                    WHERE car.id = :fId
                    """;
        return crudRepository.optional(hql, Car.class, Map.of("fId", id));
    }
}
