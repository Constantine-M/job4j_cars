package ru.job4j.cars.service.color;

import ru.job4j.cars.model.CarColor;

import java.util.Collection;

/**
 * @author Constantine on 10.05.2024
 */
public interface CarColorService {

    Collection<CarColor> findAll();

    CarColor findById(int id);
}