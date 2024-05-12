package ru.job4j.cars.service.engine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.engine.EngineRepository;

import java.util.Collection;

/**
 * @author Constantine on 10.05.2024
 */
@AllArgsConstructor
@Service
public class SimpleEngineService implements EngineService {

    private EngineRepository engineRepository;

    @Override
    public Collection<Engine> findAll() {
        return engineRepository.findAll();
    }

    @Override
    public Engine findById(int id) {
        return engineRepository.findById(id).get();
    }
}
