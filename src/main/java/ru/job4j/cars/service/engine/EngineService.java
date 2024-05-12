package ru.job4j.cars.service.engine;

import ru.job4j.cars.model.Engine;

import java.util.Collection;

/**
 * @author Constantine on 10.05.2024
 */
public interface EngineService {

    Collection<Engine> findAll();

    Engine findById(int id);
}
