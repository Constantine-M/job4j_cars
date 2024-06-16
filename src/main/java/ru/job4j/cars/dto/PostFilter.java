package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.cars.model.Car;

/**
 * Данную DTO мы будем использовать
 * для поиска объявления посредством
 * множественного фильтра.
 *
 * @author Constantine on 04.06.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostFilter {

    private Car car;

    private boolean fileExists;

    private boolean sold;

    private int createdDaysBefore;

    private long lowestPrice;

    private long highestPrice;

}
