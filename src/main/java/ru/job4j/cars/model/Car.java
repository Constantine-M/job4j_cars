package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    @Include
    private String name;

    /**
     * Связь one to one - у одной машины
     * один двигатель. В случае сохранения
     * машины - сохраняется и двигатель.
     * Аналогично во время удаления.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    /**
     * В проекте будет двухсторонняя связь
     * ManyToMany между авто и владельцами.
     *
     * Главным в этой связи будет Car. Т.е.
     * при удалении Car, удаляется также и
     * связь между Car и промежуточной таблицей,
     * а владелец и ее связь с промежуточной
     * таблицей остается.
     *
     * {@link Car} - это родительский объект
     * (joinColumns)
     * {@link Owner} - это объект, который загружаем
     * в Car (inverseJoinColumns)
     */
    @ManyToMany
    @JoinTable(
            name = "car_owners",
            joinColumns = {@JoinColumn(name = "car_id")},
            inverseJoinColumns = {@JoinColumn(name = "owner_id")}
    )
    private List<Owner> owners = new ArrayList<>();
}
