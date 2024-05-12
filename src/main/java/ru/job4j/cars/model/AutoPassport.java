package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    /** ПТС оригинал или дубликат */
    boolean original;

    /**
     * Так как в ПТС содержится не более
     * шести владельцев, то в данном случае
     * мы установили стратегию загрузки
     * {@link FetchType#EAGER}.
     *
     * Считаю, что накладные расходы
     * небольшие и позволяет решить
     * текущую проблему с обновлением
     * {@link Post}.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "auto_passport_id")
    private Set<Owner> owners = new HashSet<>();
}
