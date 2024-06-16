package ru.job4j.cars.service.pricehistory;

import ru.job4j.cars.model.PriceHistory;

import java.util.List;

/**
 * @author Constantine on 29.05.2024
 */
public interface PriceHistoryService {

    List<PriceHistory> findAllByIds(List<Integer> ids);
}
