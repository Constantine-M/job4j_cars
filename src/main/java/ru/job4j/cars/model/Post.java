package ru.job4j.cars.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static lombok.EqualsAndHashCode.Include;

/**
 * Модель описывает объявление.
 *
 * @author Constantine on 11.02.2024
 */
@Builder
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

    /** Краткая информация для покупателя */
    private String summary;

    private String description;

    @Column(updatable = false)
    private LocalDateTime created;

    /** Продано или нет */
    private boolean sold;

    private Long price;

    /**
     * Одно объявление - одна машина.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id", foreignKey = @ForeignKey(name = "CAR_ID_FK"))
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
     * использовать нашу схему. По дефолту
     * загрузка ленивая.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_post_id", foreignKey = @ForeignKey(name = "AUTO_POST_ID_FK"))
    private List<PriceHistory> priceHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "auto_post_id", foreignKey = @ForeignKey(name = "AUTO_POST_ID_FK"))
    private Set<File> files = new HashSet<>();

    /**
     * Множество объявлений может быть
     * у одного пользователя.
     */
    @ManyToOne
    @JoinColumn(name = "auto_user_id", foreignKey = @ForeignKey(name = "USER_ID_FK"), updatable = false)
    private User user;
}
