package ru.job4j.cars.service.pricehistory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.pricehistory.PriceHistoryRepository;

import java.util.List;

/**
 * @author Constantine on 29.05.2024
 */
@AllArgsConstructor
@Service
public class SimplePriceHistoryService implements PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    @Override
    public List<PriceHistory> findAllByIds(List<Integer> ids) {
        return priceHistoryRepository.findAllByIds(ids);
    }
}
