package ru.job4j.cars.service.file;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.model.File;

import java.util.Optional;
import java.util.Set;

/**
 * @author Constantine on 29.04.2024
 */
public interface FileService {

    File save(FileDto fileDto);

    Optional<FileDto> findById(int id) throws SimpleServiceException;

    void deleteById(int id) throws SimpleServiceException;

    void deleteSetOfFiles(Set<File> files) throws SimpleServiceException;

    void deleteContent(Set<File> files);
}
