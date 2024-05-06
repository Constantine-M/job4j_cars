package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данный класс описывает файл-объект
 * для передачи данных.
 *
 * @author Constantine on 13.03.2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileDto {

    private String name;

    /**
     * Тут кроется различие.
     * Доменная модель хранит путь, а не содержимое.
     */
    private byte[] content;
}
