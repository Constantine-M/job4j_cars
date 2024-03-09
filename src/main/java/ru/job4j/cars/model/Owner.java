package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель описывает владельца авто.
 *
 * @author Constantine on 03.03.2024
 */
@Entity
@Table(name = "owners")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    @Include
    private String name;

    /**
     * У одного владельца авто своя история
     * владения автомобилем.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "history_id")
    private History history;

    /**
     * Связь между машинами и владельцами
     * будет двухсторонней.
     *
     * У машины может быть несколько владельцев,
     * а у владельца несколько машин.
     */
    @ManyToMany(mappedBy = "owners")
    private List<Car> cars = new ArrayList<>();
}
