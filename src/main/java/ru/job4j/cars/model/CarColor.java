package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;

/**
 * @author Constantine on 04.05.2024
 */
@Entity
@Table(name = "color")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private String name;
}
