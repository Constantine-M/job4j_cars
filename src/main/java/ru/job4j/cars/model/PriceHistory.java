package ru.job4j.cars.model;

import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Модель описывает историю изменения
 * стоимости автомобиля.
 *
 * @author Constantine on 11.02.2024
 */
@Entity
@Table(name = "price_history")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private Long before;

    private Long after;

    private LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC"));

    public PriceHistory(int id, Long before, Long after, LocalDateTime created) {
        this.id = id;
        this.before = before;
        this.after = after;
        this.created = created;
    }
}
