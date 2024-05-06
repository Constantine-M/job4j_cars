package ru.job4j.cars.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.file.FileRepository;
import ru.job4j.cars.repository.post.PostRepository;
import ru.job4j.cars.service.post.SimplePostService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Constantine on 30.04.2024
 */
@Slf4j
@Service
public class SimpleFileService implements FileService {

    private final FileRepository fileRepository;

    private final PostRepository postRepository;

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
                             PostRepository postRepository,
                             @Value("${file.directory}") String storageDirectory) {
        this.fileRepository = fileRepository;
        this.postRepository = postRepository;
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
     * Найти все файлы, которые связаны с постом по id.
     *
     * @param postId ID объявления о продаже
     * @return список файлов
     */
    @Override
    public Collection<FileDto> findAllAttachedToPost(int postId) {
        var post = postRepository.findById(postId);
        var files = post.get().getFiles();
        return files.stream().map(file -> {
            var content = readFileAsBytes(file.getPath());
            return new FileDto(file.getName(), content);
        }).toList();
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
}
