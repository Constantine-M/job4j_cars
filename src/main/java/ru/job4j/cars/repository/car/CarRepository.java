package ru.job4j.cars.repository.car;

import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.Car;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 05.03.2024
 */
public interface CarRepository {

    Collection<Car> findAll();

    Car create(Car car);

    void updateCar(Car car);

    Optional<Car> findById(int id) throws RepositoryException;
}
