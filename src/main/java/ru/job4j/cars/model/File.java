package ru.job4j.cars.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;
import ru.job4j.cars.dto.FileDto;

import javax.persistence.*;

/**
 * Данная модель описывает файл,
 * который будет содержать в себе
 * самую разную информацию.
 *
 * @author Constantine on 13.03.2024
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "files")
@Builder
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    /**
     * Здесь собственно различие между
     * доменным объектом и объектом
     * {@link FileDto}.
     */
    @Include
    private String path;
}
