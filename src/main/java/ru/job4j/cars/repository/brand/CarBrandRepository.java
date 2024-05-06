package ru.job4j.cars.repository.brand;

import ru.job4j.cars.model.CarBrand;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 05.05.2024
 */
public interface CarBrandRepository {

    Collection<CarBrand> findAll();

    Optional<CarBrand> findById(int id);
}
