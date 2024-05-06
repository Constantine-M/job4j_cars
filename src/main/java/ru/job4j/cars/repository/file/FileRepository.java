package ru.job4j.cars.repository.file;

import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.File;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 10.04.2024
 */
public interface FileRepository {

    File save(File file);

    void saveAllFiles(Collection<File> files);

    Optional<File> findById(int id) throws RepositoryException;

    void deleteById(int id);
}
