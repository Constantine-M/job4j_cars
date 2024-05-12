package ru.job4j.cars.service.body;

import ru.job4j.cars.model.Body;

import java.util.Collection;

/**
 * @author Constantine on 30.04.2024
 */
public interface BodyService {

    Collection<Body> findAll();

    Body findById(int id);
}
