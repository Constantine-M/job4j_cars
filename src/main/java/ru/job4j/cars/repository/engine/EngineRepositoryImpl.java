package ru.job4j.cars.repository.engine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;

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

    @Override
    public Engine findById(int id) {
        String hql = """
                    FROM Engine engine
                    WHERE engine.id = :fId
                    """;
        var engineOpt =  crudRepository.optional(hql, Engine.class,
                Map.of("fId", id));
        return engineOpt.get();
    }

    @Override
    public void updateEngine(Engine engine) {
        crudRepository.run(session -> session.update(engine));
    }
}
