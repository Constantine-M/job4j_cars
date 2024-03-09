package ru.job4j.cars.repository.owner;

import ru.job4j.cars.model.Owner;

import java.util.Collection;

/**
 * @author Constantine on 05.03.2024
 */
public interface OwnerRepository {

    Collection<Owner> findAll();

    Owner create(Owner owner);

    void updateOwner(Owner owner);
}
