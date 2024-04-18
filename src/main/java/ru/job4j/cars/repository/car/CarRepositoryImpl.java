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
        String hql = """
                    FROM Car car
                    JOIN FETCH car.passport.owners
                    WHERE car.id = :fId
                    """;
        var carOptional = crudRepository.optional(hql, Car.class,
                Map.of("fId", id)
        );
        if (carOptional.isEmpty()) {
            throw new RepositoryException("Repository exception: cant find car with ID = ".concat(String.valueOf(id)));
        }
        return carOptional;
    }
}
