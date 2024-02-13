package ru.job4j.cars.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.EqualsAndHashCode.*;

/**
 * Модель описывает объявление.
 *
 * @author Constantine on 11.02.2024
 */
@Entity
@Table(name = "auto_post")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private String description;

    private LocalDateTime created;

    /**
     * У нескольких объявлений может быть
     * один пользователь.
     */
    @ManyToOne
    @JoinColumn(name = "auto_user_id")
    private User user;

    /**
     * У одного объявления может быть
     * несколько измнений  стоимости.
     * Каждое изменение отражается в
     * {@link PriceHistory}.
     *
     * Параметр cascade определяет стратегию
     * поведения каскадных операций. В нашем
     * случае, при удалении объявления будет
     * удалена вся история изменения цены.
     *
     * Для связи one-to-many обязательно нужно
     * указывать колонку для вторичного ключа.
     * Если это не сделать, то hibernate будет
     * создавать отдельную таблицу, а не
     * использовать нашу схему.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_post_id")
    private List<PriceHistory> priceHistoryList = new ArrayList<>();
}
