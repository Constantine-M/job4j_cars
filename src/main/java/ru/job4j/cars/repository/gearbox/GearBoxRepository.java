package ru.job4j.cars.repository.gearbox;

import ru.job4j.cars.model.GearBox;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 15.05.2024
 */
public interface GearBoxRepository {

    Collection<GearBox> findAll();

    Optional<GearBox> findById(int id);
}
