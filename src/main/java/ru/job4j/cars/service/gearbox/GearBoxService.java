package ru.job4j.cars.service.gearbox;

import ru.job4j.cars.model.GearBox;

import java.util.Collection;

/**
 * @author Constantine on 15.05.2024
 */
public interface GearBoxService {

    Collection<GearBox> findAll();

    GearBox findById(int id);
}
