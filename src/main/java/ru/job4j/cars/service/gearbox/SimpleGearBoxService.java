package ru.job4j.cars.service.gearbox;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.GearBox;
import ru.job4j.cars.repository.gearbox.GearBoxRepository;

import java.util.Collection;

/**
 * @author Constantine on 15.05.2024
 */
@AllArgsConstructor
@Service
public class SimpleGearBoxService implements GearBoxService {

    private GearBoxRepository gearBoxRepository;

    @Override
    public Collection<GearBox> findAll() {
        return gearBoxRepository.findAll();
    }

    @Override
    public GearBox findById(int id) {
        return gearBoxRepository.findById(id).get();
    }
}
