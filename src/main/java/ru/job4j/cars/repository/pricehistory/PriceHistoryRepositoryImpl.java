package ru.job4j.cars.repository.pricehistory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;

/**
 * @author Constantine on 14.04.2024
 */
@AllArgsConstructor
@Repository
public class PriceHistoryRepositoryImpl implements PriceHistoryRepository {

    private CrudRepository crudRepository;

    @Override
    public void save(PriceHistory priceHistory) {
        crudRepository.run(session -> session.save(priceHistory));
    }

    @Override
    public Collection<PriceHistory> findAll() {
        return crudRepository.query("FROM PriceHistory ph", PriceHistory.class);
    }
}
