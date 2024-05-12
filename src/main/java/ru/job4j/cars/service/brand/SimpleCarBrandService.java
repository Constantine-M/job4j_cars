package ru.job4j.cars.service.brand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.repository.brand.CarBrandRepository;

import java.util.Collection;

/**
 * @author Constantine on 10.05.2024
 */
@AllArgsConstructor
@Service
public class SimpleCarBrandService implements CarBrandService {

    private CarBrandRepository carBrandRepository;

    @Override
    public Collection<CarBrand> findAll() {
        return carBrandRepository.findAll();
    }

    @Override
    public CarBrand findById(int id) {
        return carBrandRepository.findById(id).get();
    }
}
