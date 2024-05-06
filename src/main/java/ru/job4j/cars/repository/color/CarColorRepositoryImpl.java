package ru.job4j.cars.repository.color;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.CarColor;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 06.05.2024
 */
@AllArgsConstructor
@Repository
public class CarColorRepositoryImpl implements CarColorRepository {

    private CrudRepository crudRepository;

    @Override
    public Collection<CarColor> findAll() {
        return crudRepository.query("FROM CarColor color ORDER BY color.name ASC", CarColor.class);
    }

    @Override
    public Optional<CarColor> findById(int id) {
        String hql = """
                    FROM CarColor color
                    WHERE color.id = :fId
                    """;
        return crudRepository.optional(hql, CarColor.class,
                Map.of("fId", id));
    }
}
