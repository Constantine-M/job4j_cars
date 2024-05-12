package ru.job4j.cars.service.brand;

import ru.job4j.cars.model.CarBrand;

import java.util.Collection;

/**
 * @author Constantine on 10.05.2024
 */
public interface CarBrandService {

    Collection<CarBrand> findAll();

    CarBrand findById(int id);
}
