package ru.job4j.cars.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
@DynamicUpdate
@Entity
@Table(name = "auto_post")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private String description;

    @Column(updatable = false)
    private LocalDateTime created;

    /**
     * У нескольких объявлений может быть
     * один пользователь.
     */
    @ManyToOne
    @JoinColumn(name = "auto_user_id")
    private User user;

    /**
     * Одно объявление - одна машина.
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Car car;

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
    private List<PriceHistory> priceHistory = new ArrayList<>();

    /**
     * В одном объявлении может быть несколько
     * фотографий.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_post_id")
    private List<File> files = new ArrayList<>();

    /**
     * В нашей связи many-to-many главным
     * будет Post. При его удалении, удаляется
     * непосредственно Post и связь с таблицей
     * participates. User и его связь с таблицей
     * participates остается.
     *
     * {@link Post} - это родительский объект
     * (joinColumns)
     * {@link User} - это объект, который загружаем
     * в Post (inverseJoinColumns)
     *
     * Связь unidirectional, т.к. в {@link User}
     * нет коллекции объявлений.
     */
    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> users = new ArrayList<>();
}
