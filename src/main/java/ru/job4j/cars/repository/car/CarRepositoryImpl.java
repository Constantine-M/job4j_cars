package ru.job4j.cars.repository.car;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 05.03.2024
 */
@AllArgsConstructor
@Repository
public class CarRepositoryImpl implements CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Найти все машины.
     *
     * @return список всех машин
     */
    @Override
    public Collection<Car> findAll() {
        String hql1 = """
                    SELECT DISTINCT car
                    FROM Car car
                    JOIN FETCH car.historyOwners
                    """;
        String hql = """
                    SELECT DISTINCT car
                    FROM Car car
                    """;
        return crudRepository.query(hql, Car.class);

    }

    /**
     * Сохранить машину.
     */
    @Override
    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    @Override
    public void updateCar(Car car) {
        crudRepository.run(session -> session.update(car));
    }

    @Override
    public Optional<Car> findById(int id) throws RepositoryException {
        var carOptional = crudRepository.optional(
                "FROM Car car WHERE car.id = :fId",
                Car.class,
                Map.of("fId", id)
        );
        if (carOptional.isEmpty()) {
            throw new RepositoryException("Repository exception: cant find car with ID = ".concat(String.valueOf(id)));
        }
        return carOptional;
    }
}
