package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Модель описывает паспорт
 * технического средства (ПТС).
 *
 * @author Constantine on 13.04.2024
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "auto_passport")
@Entity
public class AutoPassport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    /**
     * Когда куплена машина
     *
     * Во время отправки запроса на сохранение
     * объявления, мы также будем отправлять
     * дату в формате строки (String).
     * По умолчанию Spring не сможет
     * преобразовать String в LocalDateTime.
     * Поэтому мы добавили в application.yml
     * spring.mvc.format.date: yyyy-MM-dd
     *
     * Так как нам в данном случае время неважно,
     * то использовали {@link LocalDate}.
     */
    @Column(name = "bought_at")
    private LocalDate boughtAt;

    /**
     * Пасспорт оригинал, дубликат или нет ПТС.
     * Пользователю будут доступны только 3
     * варианта на выбор (варианты будут описаны
     * во view).
     */
    @Column(name = "having_passport")
    private String havingPassport;

    /** Авто растаможено или нет */
    private boolean cleared;
}
