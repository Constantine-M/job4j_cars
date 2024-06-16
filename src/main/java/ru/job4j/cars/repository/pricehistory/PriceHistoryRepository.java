package ru.job4j.cars.repository.pricehistory;

import ru.job4j.cars.model.PriceHistory;

import java.util.List;

/**
 * @author Constantine on 29.05.2024
 */
public interface PriceHistoryRepository {

    List<PriceHistory> findAllByIds(List<Integer> ids);
}
