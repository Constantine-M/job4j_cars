package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;

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

    /** Модель авто. Заполянет пользователь. */
    @Include
    private String model;

    /** Пробег автомобиля. Заполянет пользователь. */
    private int mileage;

    /** Будет выпадающий список брендов */
    @ManyToOne
    @JoinColumn(name = "car_brand_id", foreignKey = @ForeignKey(name = "CAR_BRAND_ID_FK"))
    private CarBrand brand;

    /** Будет выпадающий список */
    @ManyToOne
    @JoinColumn(name = "color_id", foreignKey = @ForeignKey(name = "COLOR_ID_FK"))
    private CarColor carColor;

    /**
     * Будет выпадающий список.
     * В нем будут модификации двигателя
     * - тип двигателя, объем и мощность.
     *
     * Заранее будет заполнена таблица
     * engine.
     */
    @ManyToOne
    @JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
    private Engine engine;

    /** Будет выпадающим списком */
    @ManyToOne
    @JoinColumn(name = "body_id", foreignKey = @ForeignKey(name = "BODY_ID_FK"))
    private Body body;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_passport_id", foreignKey = @ForeignKey(name = "AUTO_PASSPORT_ID_FK"))
    private AutoPassport passport;
}
