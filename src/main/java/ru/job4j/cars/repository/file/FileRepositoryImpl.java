package ru.job4j.cars.repository.file;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.File;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 10.04.2024
 */
@Slf4j
@AllArgsConstructor
@Repository
public class FileRepositoryImpl implements FileRepository {

    private final CrudRepository crudRepository;

    @Override
    public File save(File file) {
        crudRepository.run(session -> session.save(file));
        return file;
    }

    /**
     * Найти файл по ID.
     *
     * @param id идентификатор файла
     * @return Optional<File>
     */
    @Override
    public Optional<File> findById(int id) throws RepositoryException {
        String hql = """
                    FROM File file
                    WHERE file.id = :fId
                    """;
        var fileOpt = crudRepository.optional(hql, File.class, Map.of("fId", id));
        if (fileOpt.isEmpty()) {
            log.error("File with ID = {} not found!", id);
            throw new RepositoryException(String.format("File with ID = {} not found!", id));
        }
        return fileOpt;
    }

    /**
     * Удалить файл по ID.
     */
    @Override
    public void deleteById(int id) {
        crudRepository.run("DELETE File file WHERE file.id = :fId", Map.of("fId", id));
    }
}
