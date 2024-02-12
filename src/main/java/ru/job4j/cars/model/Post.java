package ru.job4j.cars.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
