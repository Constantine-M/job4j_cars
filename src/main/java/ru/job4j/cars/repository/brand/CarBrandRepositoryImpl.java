package ru.job4j.cars.repository.brand;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 05.05.2024
 */
@AllArgsConstructor
@Slf4j
@Repository
public class CarBrandRepositoryImpl implements CarBrandRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<CarBrand> findAll() {
        return crudRepository.query("FROM CarBrand brand ORDER BY brand.name ASC", CarBrand.class);
    }

    @Override
    public Optional<CarBrand> findById(int id) {
        String hql = """
                    FROM CarBrand brand
                    WHERE brand.id = :fId
                    """;
        return crudRepository.optional(hql, CarBrand.class, Map.of("fId", id));
    }
}
