package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель описывает владельца авто.
 * Здесь также указывается инфомарция
 * о том, когда он начал владеть машиной
 * и когда перестал владеть (машина
 * была продана например).
 *
 * @author Constantine on 03.03.2024
 */
@DynamicUpdate
@Builder
@Entity
@Table(name = "owners")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private String name;

    @Column(name = "start_ownership", updatable = false)
    private LocalDateTime start;

    @Column(name = "end_ownership", updatable = false)
    private LocalDateTime end;
}
