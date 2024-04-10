package ru.job4j.cars.repository.file;

import ru.job4j.cars.model.File;

import java.util.Optional;

/**
 * @author Constantine on 10.04.2024
 */
public interface FileRepository {

    void save(File file);

    Optional<File> findById(int id);

    void deleteById(int id);
}
