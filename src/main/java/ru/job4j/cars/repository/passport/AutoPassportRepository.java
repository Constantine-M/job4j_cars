package ru.job4j.cars.repository.passport;

import ru.job4j.cars.model.AutoPassport;

/**
 * @author Constantine on 14.04.2024
 */
public interface AutoPassportRepository {

    void save(AutoPassport autoPassport);

    AutoPassport findById(int id);

    void update(AutoPassport autoPassport);
}
