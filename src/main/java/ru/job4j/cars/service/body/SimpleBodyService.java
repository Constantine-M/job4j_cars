package ru.job4j.cars.service.body;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.repository.body.BodyRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author Constantine on 30.04.2024
 */
@AllArgsConstructor
@Service
public class SimpleBodyService implements BodyService {

    private final BodyRepository bodyRepository;

    @Override
    public Collection<Body> findAll() {
        return bodyRepository.findAll();
    }

    @Override
    public Body findById(int id) {
        return bodyRepository.findById(id).get();
    }
}
