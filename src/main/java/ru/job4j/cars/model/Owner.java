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
     * Связь между машинами и владельцами
     * будет двухсторонней.
     *
     * У машины может быть несколько владельцев,
     * а у владельца несколько машин.
     */
    @OneToMany(mappedBy = "owner")
    private List<HistoryOwner> historyOwners = new ArrayList<>();
}
