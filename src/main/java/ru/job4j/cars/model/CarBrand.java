package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;

/**
 * Модель описывает марку автомобиля.
 *
 * @author Constantine on 04.05.2024
 */
@Entity
@Table(name = "car_brand")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private String name;
}
