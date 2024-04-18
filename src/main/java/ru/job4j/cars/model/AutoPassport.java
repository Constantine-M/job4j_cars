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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_passport_id")
    private Set<Owner> owners = new HashSet<>();
}
