package ru.job4j.cars.service.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.PostFilter;
import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.mapper.post.PostMapper;
import ru.job4j.cars.mapper.post.PostMapperImpl;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.post.PostRepository;
import ru.job4j.cars.service.body.BodyService;
import ru.job4j.cars.service.brand.CarBrandService;
import ru.job4j.cars.service.color.CarColorService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.file.FileService;
import ru.job4j.cars.service.gearbox.GearBoxService;
import ru.job4j.cars.util.TimeZoneUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Constantine on 01.05.2024
 */
@Slf4j
@AllArgsConstructor
@Service
public class SimplePostService implements PostService {

    private static final int FIRST_FILE_INDEX = 0;

    private final PostRepository postRepository;

    private final FileService fileService;

    private final CarBrandService carBrandService;

    private final BodyService bodyService;

    private final EngineService engineService;

    private final CarColorService carColorService;

    private final GearBoxService gearBoxService;

    private final PostMapper postMapper = new PostMapperImpl();

    /**
     * Сохранить объявление.
     *
     * В данном методе мы сетим всё в {@link Post}.
     * 1.Читаем файлы, собираем в список
     * и сетим в {@link Post}.
     * 2.PriceHistory в начале - это стоимость авто
     * (before и after = стоимость машины).
     *
     * @param post объявление, которое нужно сохранить
     * @param fileDto файлы, которые приняли с фронта
     *                и которые нужно сохранить
     */
    @Override
    public Post savePost(Post post, Set<FileDto> fileDto) {
        Set<File> files = new HashSet<>();
        if (!isContentEmpty(fileDto)) {
            fileDto.forEach(file -> files.add(fileService.save(file)));
        }
        post.setFiles(files);
        post.setCreated(LocalDateTime.now(ZoneId.of("UTC")));
        setStartPriceHistory(post);
        setCarFields(post);
        return postRepository.create(post);
    }

    @Override
    public Collection<PostDto> findAllPostsDtoByUser(User user) {
        var posts = postRepository.findAllByUser(user);
        TimeZoneUtil.setDateTimeByUserTimezone(user, posts);
        return mapToPostDto(posts);
    }

    /**
     * Метод возвращает список DTO.
     * Данный список DTO нужен для
     * отображения всей необходимой информации
     * на странице со всеми объявлениями.
     *
     * @return список {@link PostDto}
     */
    @Override
    public Collection<PostDto> findAllPostsDto() {
        var posts = postRepository.findAll();
        return mapToPostDto(posts);
    }

    @Override
    public Collection<PostDto> findAllPostDtoByQuerydsl(PostFilter filter) {
        var posts = postRepository.findAllByQuerydsl(filter);
        return mapToPostDto(posts);
    }

    private List<PostDto> mapToPostDto(Collection<Post> posts) {
        return posts.stream()
                .map(postMapper::mapPostToPostDto)
                .toList();
    }

    /**
     * Найти объявление по ID.
     *
     * @param id идентификатор объявления
     * @return {@link Post}
     */
    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
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

    private Car setCarColor(Car car) {
        var carColor = carColorService.findById(car.getCarColor().getId());
        car.setCarColor(carColor);
        return car;
    }

    private Car setGearBox(Car car) {
        var gearBox = gearBoxService.findById(car.getGearBox().getId());
        car.setGearBox(gearBox);
        return car;
    }

    /**
     * Метод устанавливает изменение
     * стоимости автомобиля в момент создания
     * объявления.
     *
     * @param post объявление о продаже
     */
    private Post setStartPriceHistory(Post post) {
        var newPriceHistory = PriceHistory.builder()
                .created(LocalDateTime.now(ZoneId.of("UTC")))
                .before(post.getPrice())
                .after(post.getPrice())
                .build();
        post.getPriceHistory().add(newPriceHistory);
        return post;
    }

    /**
     * Обновить объявление.
     *
     * 1.Если картинок приложенных нет - просто
     * обновляем объявление. Было 2 варианта:
     * передать список ID файлов, найти их и
     * засетить в контроллере; найти {@link Post}
     * по ID здесь, вытащить файлы и точно так же
     * засетить, но в слое сервисов. Выбрал второе.
     *
     * 2.Если из представления в контроллер
     * пришел непустой список файлов (имеется
     * содержимое), то сохраняем старое для
     * дальнейшего удаления. Новое
     * сохраняем и сетим в {@link Post}.
     *
     * 3.Обновляем историю изменения цены.
     * История изменения обновится вне зависимости
     * от того, изменилась цена или нет.
     *
     * 4.Обновляем {@link Post} и в зависимости от
     * случая, удаляем старые файлы.
     *
     * Пока не могу разобраться с цепочкой
     * исключений. Какой-то строгой и понятной
     * связи не получается.
     */
    @Override
    public void updatePost(Post post, Set<FileDto> fileDto) throws SimpleServiceException {
        var oldFileList = postRepository.findById(post.getId()).get().getFiles();
        if (isContentEmpty(fileDto)) {
            updateWithOldContent(post, oldFileList);
        } else {
            updateWithNewContent(post, fileDto);
            deleteFilesIfExists(oldFileList);
        }
    }

    /**
     * Проверить наличие содержимого в файлах.
     *
     * @param fileDtoSet список файлов, которые прикрепил или
     *                   не прикрепил пользователь.
     * @return true, если файлы без содержимого.
     */
    private boolean isContentEmpty(Set<FileDto> fileDtoSet) {
        return new ArrayList<>(fileDtoSet).get(FIRST_FILE_INDEX).getContent().length == 0;
    }

    /**
     * Обновить объявление со старыми прикрепленными
     * файлами, если таковые есть.
     */
    private void updateWithOldContent(Post post, Set<File> files) {
        post.setFiles(files);
        setCarFields(post);
        updatePriceHistory(post);
        postRepository.updatePost(post);
    }

    /**
     * Обновить объявление с новыми прикрепленными
     * файлами, если таковые есть.
     */
    private void updateWithNewContent(Post post, Set<FileDto> contentFiles) {
        Set<File> newFiles = new HashSet<>();
        contentFiles.forEach(file -> newFiles.add(fileService.save(file)));
        post.setFiles(newFiles);
        setCarFields(post);
        updatePriceHistory(post);
        postRepository.updatePost(post);
    }

    private void deleteFilesIfExists(Set<File> files) throws SimpleServiceException {
        if (!files.isEmpty()) {
            fileService.deleteSetOfFiles(files);
        }
    }

    private void setCarFields(Post post) {
        setBody(post.getCar());
        setCarBrand(post.getCar());
        setEngine(post.getCar());
        setCarColor(post.getCar());
        setGearBox(post.getCar());
    }

    /**
     * Обновить историю изменения цены
     * на автомобиль в объявлении.
     *
     * 1.Получаем из списка последнюю
     * запись {@link PriceHistory},
     * берем оттуда цену "after" -
     * она будет старой ценой в новой записи.
     * 2.Формируем новую запись.
     */
    private void updatePriceHistory(Post post) {
        var lastPriceHistory = post.getPriceHistory().stream()
                .reduce((first, second) -> second)
                .get();
        var newPriceHistory = PriceHistory.builder()
                .before(lastPriceHistory.getAfter())
                .after(post.getPrice())
                .created(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
        post.getPriceHistory().add(newPriceHistory);
    }

    /**
     * Удалить объявление по ID.
     *
     * 1.Сначала удаляем контент, т.к.
     * во время удаления Post каскадно удалится
     * весь список файлов.
     * 2.Удаляем {@link Post}.
     *
     * Приходится искать {@link Post}, т.к.
     * не удается из представления получить
     * объект {@link Post}.
     */
    @Override
    public void deleteById(int id) throws SimpleServiceException {
        var post = postRepository.findById(id).get();
        fileService.deleteContent(post.getFiles());
        postRepository.delete(post);
    }

    /**
     * Данный метод меняет значение поля
     * {@link Post#isSold()} c false на
     * true и наоборот при необходимости.
     * Требуется в случаях снятия с продажи
     * или убрать в архив например.
     *
     * @param postId ID объявления, которое требуется
     *             снять с продажи.
     */
    @Override
    public void updateSold(int postId) {
        var post = postRepository.findById(postId).get();
        if (!post.isSold()) {
            post.setSold(true);
            postRepository.updatePost(post);
        } else {
            post.setSold(false);
            postRepository.updatePost(post);
        }
    }
}
