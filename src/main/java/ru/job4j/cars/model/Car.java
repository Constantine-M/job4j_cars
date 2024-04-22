package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Модель описывает автомобиль,
 * который выставляется на продажу.
 *
 * @author Constantine on 03.03.2024
 */
@Entity
@Table(name = "cars")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    /** Марка авто */
    @Include
    private String model;

    private String color;

    /** Пробег автомобиля */
    private int mileage;

    /**
     * Связь one to one - у одной машины
     * один двигатель. В случае сохранения
     * машины - сохраняется и двигатель.
     * Аналогично во время удаления.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "body_id")
//    @Column(name = "body_id")
    private Body body;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_passport_id")
    private AutoPassport passport;
}
