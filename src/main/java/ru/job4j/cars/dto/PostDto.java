package ru.job4j.cars.dto;

import lombok.*;
import ru.job4j.cars.model.File;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Данный класс описывает объект-пост,
 * в котором содержатся необходимые данные
 * для отображения на веб-странице.
 *
 * @author Constantine on 16.05.2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private int id;

    private String summary;

    private boolean sold;

    private Long price;

    private LocalDateTime created;

    private String carBrand;

    private String model;

    private int mileage;

    private String engineFuelType;

    private float engineCapacity;

    private int engineHorsePower;

    private String carBody;

    private String carColor;

    private String gearBox;

    private int carYear;

    private Set<File> files;
}
