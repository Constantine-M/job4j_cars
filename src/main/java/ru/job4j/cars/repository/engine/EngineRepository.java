package ru.job4j.cars.repository.engine;

import ru.job4j.cars.model.Engine;

import java.util.Collection;

/**
 * @author Constantine on 05.03.2024
 */
public interface EngineRepository {

    Collection<Engine> findAll();

    Engine save(Engine engine);

    Engine findById(int id);

    void updateEngine(Engine engine);
}
