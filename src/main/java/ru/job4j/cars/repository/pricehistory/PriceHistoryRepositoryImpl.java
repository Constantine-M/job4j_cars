package ru.job4j.cars.repository.pricehistory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;

/**
 * @author Constantine on 29.05.2024
 */
@AllArgsConstructor
@Repository
public class PriceHistoryRepositoryImpl implements PriceHistoryRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<PriceHistory> findAllByIds(List<Integer> ids) {
        return crudRepository.query("FROM PriceHistory ph WHERE ph.id IN :fIds", PriceHistory.class,
                Map.of("fIds", ids));
    }
}
