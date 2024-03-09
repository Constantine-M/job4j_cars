package ru.job4j.cars.repository.car;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;

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
        String hql = """
                    SELECT DISTINCT car
                    FROM Car car
                    JOIN FETCH car.owners
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
}
