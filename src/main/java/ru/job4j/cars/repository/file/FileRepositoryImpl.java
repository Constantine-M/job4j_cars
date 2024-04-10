package ru.job4j.cars.repository.file;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Constantine on 10.04.2024
 */
@AllArgsConstructor
@Repository
public class FileRepositoryImpl implements FileRepository {

    private final CrudRepository crudRepository;

    @Override
    public void save(File file) {
        crudRepository.run(session -> session.save(file));
    }

    @Override
    public Optional<File> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(int id) {

    }
}
