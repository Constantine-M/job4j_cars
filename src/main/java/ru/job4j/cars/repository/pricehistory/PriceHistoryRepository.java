package ru.job4j.cars.repository.pricehistory;

import ru.job4j.cars.model.PriceHistory;

import java.util.Collection;

/**
 * @author Constantine on 14.04.2024
 */
public interface PriceHistoryRepository {

    void save(PriceHistory priceHistory);

    Collection<PriceHistory> findAll();
}
