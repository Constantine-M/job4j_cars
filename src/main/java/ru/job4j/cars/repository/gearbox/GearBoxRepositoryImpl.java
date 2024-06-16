package ru.job4j.cars.repository.gearbox;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.GearBox;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 15.05.2024
 */
@Repository
@AllArgsConstructor
public class GearBoxRepositoryImpl implements GearBoxRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<GearBox> findAll() {
        return crudRepository.query("FROM GearBox gearBox", GearBox.class);
    }

    @Override
    public Optional<GearBox> findById(int id) {
        String hql = """
                    FROM GearBox gearBox
                    WHERE gearBox.id = :fId
                    """;
        return crudRepository.optional(hql, GearBox.class, Map.of("fId", id));
    }
}
