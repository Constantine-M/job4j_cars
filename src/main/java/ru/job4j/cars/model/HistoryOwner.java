package ru.job4j.cars.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Данный класс описывает промежуточную
 * сущность, которая будет связывать
 * {@link Car} и {@link Owner}.
 *
 * Благодаря этой сущности мы разорвем
 * связь many-to-many между машиной
 * и владельцем.
 *
 * Сущность будет хранить в себе данные
 * о том, в каком промежутке времени
 * машина была во владении (начальная
 * дата и конечная дата).
 *
 * @author Constantine on 10.03.2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "history_owners")
@Entity
public class HistoryOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(name = "start_at")
    private LocalDateTime startAt = LocalDateTime.now(ZoneId.of("UTC"));

    @Column(name = "end_at")
    private LocalDateTime endAt = LocalDateTime.now(ZoneId.of("UTC"));
}
