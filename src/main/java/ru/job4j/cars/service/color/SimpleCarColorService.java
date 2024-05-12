package ru.job4j.cars.service.color;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.CarColor;
import ru.job4j.cars.repository.color.CarColorRepository;

import java.util.Collection;

/**
 * @author Constantine on 10.05.2024
 */
@AllArgsConstructor
@Service
public class SimpleCarColorService implements CarColorService {

    private CarColorRepository carColorRepository;

    @Override
    public Collection<CarColor> findAll() {
        return carColorRepository.findAll();
    }

    @Override
    public CarColor findById(int id) {
        return carColorRepository.findById(id).get();
    }
}
