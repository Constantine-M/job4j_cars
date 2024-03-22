package ru.job4j.cars.repository.engine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;

/**
 * @author Constantine on 05.03.2024
 */
@AllArgsConstructor
@Repository
public class EngineRepositoryImpl implements EngineRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Engine> findAll() {
        return crudRepository.query("FROM Engine engine", Engine.class);
    }

    @Override
    public Engine save(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }
}
