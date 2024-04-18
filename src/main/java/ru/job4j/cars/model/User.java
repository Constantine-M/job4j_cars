package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "auto_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    /**
     * Данное поле является первичным ключом.
     *
     * GenerationType.IDENTITY - используется
     * встроенный в БД тип данных столбца
     * -identity - для генерации значения
     * первичного ключа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Include
    private String login;

    private String password;

    /**
     * В нашей связи many-to-many главным
     * будет User. При его удалении, удаляется
     * непосредственно User и связь с таблицей
     * participates. Post и его связь с таблицей
     * participates остается.
     *
     * {@link User} - это родительский объект
     * (joinColumns)
     * {@link Post} - это объект, который загружаем
     * в User (inverseJoinColumns)
     *
     * Связь unidirectional, т.к. в {@link Post}
     * нет коллекции объявлений.
     */
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")}
    )
    private List<Post> posts = new ArrayList<>();
}
