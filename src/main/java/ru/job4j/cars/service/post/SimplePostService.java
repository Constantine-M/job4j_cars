package ru.job4j.cars.service.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.post.PostRepository;
import ru.job4j.cars.service.body.BodyService;
import ru.job4j.cars.service.brand.CarBrandService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.file.FileService;

import java.util.*;

/**
 * @author Constantine on 01.05.2024
 */
@Slf4j
@AllArgsConstructor
@Service
public class SimplePostService implements PostService {

    private final PostRepository postRepository;

    private final FileService fileService;

    private final CarBrandService carBrandService;

    private final BodyService bodyService;

    private final EngineService engineService;

    /**
     * Сохранить объявление.
     *
     * В данном методе мы сетим всё в {@link Post}.
     * 1.Читаем файлы, собираем в список
     * и сетим в {@link Post} (делаем setFiles()).
     * 2.PriceHistory в начале - это пустой список, т.к.
     * у нас нет данных о прошлой стоимости.
     *
     * @param post объявление, которое нужно сохранить
     * @param fileDto файлы, которые приняли с фронта
     *                и которые нужно сохранить
     */
    @Override
    public Post savePost(Post post, Collection<FileDto> fileDto) {
        Set<File> files = new HashSet<>();
        if (!fileDto.isEmpty()) {
            fileDto.forEach(file -> files.add(fileService.save(file)));
        }
        post.setFiles(files);
        setBody(post.getCar());
        setCarBrand(post.getCar());
        setEngine(post.getCar());
        var savedPost = postRepository.create(post);
        return savedPost;
    }

    @Override
    public Collection<Post> findAllPosts() {
        return postRepository.findAll();
    }

    private Car setBody(Car car) {
        var body = bodyService.findById(car.getBody().getId());
        car.setBody(body);
        return car;
    }

    private Car setCarBrand(Car car) {
        var carBrand = carBrandService.findById(car.getBrand().getId());
        car.setBrand(carBrand);
        return car;
    }

    private Car setEngine(Car car) {
        var engine = engineService.findById(car.getEngine().getId());
        car.setEngine(engine);
        return car;
    }
}
