package ru.job4j.cars.repository.color;

import ru.job4j.cars.model.CarColor;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 06.05.2024
 */
public interface CarColorRepository {

    Collection<CarColor> findAll();

    Optional<CarColor> findById(int id);
}
