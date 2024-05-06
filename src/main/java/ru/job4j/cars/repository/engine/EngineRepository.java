package ru.job4j.cars.repository.engine;

import ru.job4j.cars.model.Engine;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 05.03.2024
 */
public interface EngineRepository {

    Collection<Engine> findAll();

    Optional<Engine> findById(int id);
}
