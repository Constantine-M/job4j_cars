package ru.job4j.cars.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.model.File;
import ru.job4j.cars.repository.file.FileRepository;
import ru.job4j.cars.service.post.SimplePostService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author Constantine on 30.04.2024
 */
@Slf4j
@Service
public class SimpleFileService implements FileService {

    private final FileRepository fileRepository;

    private final String storageDirectory;

    /**
     * В данном конструкторе следует
     * обратить внимание на аннотацию
     * {@link Value}, с помощью которой
     * можно подставить на место storageDirectory
     * значение из файла application.properties
     * с ключом file.directory.
     */
    public SimpleFileService(FileRepository fileRepository,
                             @Value("${file.directory}") String storageDirectory) {
        this.fileRepository = fileRepository;
        this.storageDirectory = storageDirectory;
        createStorageDirectory(storageDirectory);
    }

    private void createStorageDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Данный метод позволяет сохранить файл
     * {@link FileDto} и путь к нему.
     *
     * В этом методе мы не сохраняем в БД, а читаем
     * DTO и превращаем его в {@link File}, который
     * можно будет сохранить.
     * Сохранять наш {@link File} будет Hibernate
     * в методе {@link SimplePostService#savePost}.
     *
     * @param fileDto файл-объект для передачи данных.
     * @return File
     */
    @Override
    public File save(FileDto fileDto) {
        var path = getNewFilePath(fileDto.getName());
        writeFileBytes(path, fileDto.getContent());
        var file = new File();
        file.setName(fileDto.getName());
        file.setPath(path);
        return file;
    }

    /**
     * Данный метод создает уникальный путь для
     * нового файла.
     *
     * UUID - это просто рандомная строка
     * определенного формата.
     *
     * @param sourceName имя файла DTO.
     * @return новый сгенерированный путь.
     */
    private String getNewFilePath(String sourceName) {
        return storageDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    private void writeFileBytes(String path, byte[] content) {
        try {
            Files.write(Path.of(path), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Найти файл по ID.
     */
    @Override
    public Optional<FileDto> findById(int id) throws SimpleServiceException {
        try {
            var fileOptional = fileRepository.findById(id);
            var content = readFileAsBytes(fileOptional.get().getPath());
            return Optional.of(new FileDto(fileOptional.get().getName(), content));
        } catch (RepositoryException e) {
            log.error("Can't find file with id = {}" + id, Arrays.toString(e.getStackTrace()));
            throw new SimpleServiceException("Can't find file", e);
        }
    }

    /**
     * Данный метод читает файл
     * по указанному пути.
     *
     * @param path путь к файлу.
     */
    private byte[] readFileAsBytes(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Удалить файл по ID.
     *
     * Сначала удаляем данные, а
     * потом объект DTO из карты по его ID.
     */
    @Override
    public void deleteById(int id) throws SimpleServiceException {
        try {
            var fileOptional = fileRepository.findById(id);
            deleteFile(fileOptional.get().getPath());
            fileRepository.deleteById(id);
        } catch (RepositoryException e) {
            log.error("Can't find file with id = {}" + id, Arrays.toString(e.getStackTrace()));
            throw new SimpleServiceException("Can't find file", e);
        }
    }

    private void deleteFile(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSetOfFiles(Set<File> files) {
        files.forEach(file -> {
            try {
                deleteById(file.getId());
            } catch (SimpleServiceException e) {
                log.error("Exception while deleting set of files. Cant find file with id: {}{}", Arrays.toString(e.getStackTrace()), file.getId());
            }
        });
    }

    /**
     * Данный метод производит удаление
     * контента. Записи в БД при этом
     * не удаляются.
     *
     * @param files список файлов.
     */
    @Override
    public void deleteContent(Set<File> files) {
        files.forEach(file -> deleteFile(file.getPath()));
    }
}
