package ru.job4j.cars.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Данный класс описывает промежуточную
 * сущность, которая будет связывать
 * {@link Car} и {@link Owner}.
 *
 * Благодаря этой сущности мы разорвем
 * связь many-to-many между машиной
 * и владельцем.
 *
 * @author Constantine on 10.03.2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "car_owners")
@Entity
public class CarOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
}
