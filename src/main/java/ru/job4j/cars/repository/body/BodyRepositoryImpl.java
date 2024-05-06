package ru.job4j.cars.repository.body;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 14.04.2024
 */
@AllArgsConstructor
@Repository
public class BodyRepositoryImpl implements BodyRepository {

    private CrudRepository crudRepository;

    @Override
    public Collection<Body> findAll() {
        return crudRepository.query("FROM Body body ORDER BY body.name ASC", Body.class);
    }

    @Override
    public Optional<Body> findById(int id) {
        var hql = """
                FROM Body body
                WHERE body.id = :fId
                """;
        return crudRepository.optional(hql, Body.class, Map.of("fId", id));
    }
}
