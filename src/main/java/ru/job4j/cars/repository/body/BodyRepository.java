package ru.job4j.cars.repository.body;

import ru.job4j.cars.model.Body;

import java.util.Collection;

/**
 * @author Constantine on 14.04.2024
 */
public interface BodyRepository {

    Collection<Body> findAll();
}
