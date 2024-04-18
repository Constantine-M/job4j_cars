package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;

/**
 * Модель описывает характеристики двигателя
 * авто, который выставлен на продажу.
 *
 * @author Constantine on 03.03.2024
 */
@Entity
@Table(name = "engine")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    /** Тип двигателя - бензин, дизель, электро.. */
    private String type;

    private float capacity;

    @Column(name = "horsepower")
    private int horsePower;
}
