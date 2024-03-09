package ru.job4j.cars.repository.car;

import ru.job4j.cars.model.Car;

import java.util.Collection;

/**
 * @author Constantine on 05.03.2024
 */
public interface CarRepository {

    Collection<Car> findAll();

    Car create(Car car);

    void updateCar(Car car);
}
