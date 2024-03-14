package ru.job4j.cars.dto;

/**
 * Данный класс описывает файл-объект
 * для передачи данных.
 *
 * @author Constantine on 13.03.2024
 */
public class FileDto {

    private String name;

    /**
     * Тут кроется различие.
     * Доменная модель хранит путь, а не содержимое.
     */
    private byte[] content;
}
