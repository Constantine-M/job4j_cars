package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.exception.ControllerException;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.body.BodyService;
import ru.job4j.cars.service.brand.CarBrandService;
import ru.job4j.cars.service.color.CarColorService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.gearbox.GearBoxService;
import ru.job4j.cars.service.post.PostService;
import ru.job4j.cars.service.pricehistory.PriceHistoryService;

import java.io.IOException;
import java.util.*;

/**
 * Данный контроллер обрабатывает запросы,
 * которые касаются пользователя:
 * - пользовательские объявления
 * - редактирование пользовательских объявлений
 * - создание пользовательских объявлений
 *
 * @author Constantine on 26.05.2024
 */
@Slf4j
@RequestMapping({"/userposts"})
@AllArgsConstructor
@Controller
public class UserPostController {

    private final PostService postService;
    private final CarBrandService carBrandService;
    private final BodyService bodyService;
    private final EngineService engineService;
    private final CarColorService carColorService;
    private final GearBoxService gearBoxService;
    private final PriceHistoryService priceHistoryService;

    @GetMapping
    public String getAllPostsByUser(Model model,
                                    @SessionAttribute User user) {
        model.addAttribute("userPosts", postService.findAllPostsDtoByUser(user));
        return "userposts/all";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("carBrand", carBrandService.findAll());
        model.addAttribute("body", bodyService.findAll());
        model.addAttribute("engine", engineService.findAll());
        model.addAttribute("carColor", carColorService.findAll());
        model.addAttribute("gearBox", gearBoxService.findAll());
        return "userposts/create";
    }

    /**
     * Сохранить объявление о продаже.
     *
     * С помощью {@link ModelAttribute}
     * указываем спрингу, какие объекты
     * потребуется собрать. По сути это
     * объекты, в которых мы, посредством
     * форм на фронте, частично или полностью
     * заполняем поля.
     *
     * Например, мы полностью сами формируем
     * {@link AutoPassport}. Мы заполняем форму,
     * а Spring собирает объект.
     *
     * Таким образом, здесь я указал все
     * объекты, которые не могу сразу собрать
     * самостоятельно, как например
     * {@link Engine} или {@link CarBrand}.
     */
    @PostMapping("/create")
    public String createPost(@SessionAttribute User user,
                             @ModelAttribute Post post,
                             @RequestParam Set<MultipartFile> files,
                             @ModelAttribute Car car,
                             @ModelAttribute AutoPassport autoPassport) {
        var fileDtoList = convertFromMultipartFilesToDto(files);
        car.setPassport(autoPassport);
        post.setUser(user);
        post.setCar(car);
        postService.savePost(post, fileDtoList);
        return "redirect:/posts";
    }

    /**
     * Данный метод преобразует файлы,
     * которые загружает пользователь в браузере,
     * в DTO.
     *
     * Дальнейшая работа строится уже
     * с файлами {@link FileDto}.
     *
     * @param files файлы, которые приходят со стороны
     *              frontend
     * @return список {@link FileDto}
     */
    private Set<FileDto> convertFromMultipartFilesToDto(Set<MultipartFile> files) {
        Set<FileDto> result = new HashSet<>();
        try {
            for (MultipartFile file : files) {
                result.add(new FileDto(file.getOriginalFilename(), file.getBytes()));
            }
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return result;
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(Model model,
                              @PathVariable int id) {
        var post = postService.findById(id).get();
        model.addAttribute("post", post);
        model.addAttribute("carBrand", carBrandService.findAll());
        model.addAttribute("body", bodyService.findAll());
        model.addAttribute("engine", engineService.findAll());
        model.addAttribute("carColor", carColorService.findAll());
        model.addAttribute("gearBox", gearBoxService.findAll());
        return "userposts/edit";
    }

    /**
     * Обновить информацию в объявлении.
     */
    @PostMapping("/update")
    public String updatePost(@ModelAttribute Post post,
                             @SessionAttribute User user,
                             @RequestParam Set<MultipartFile> files,
                             @RequestParam(value = "priceHistoryIds") List<Integer> priceHistoryIds) throws SimpleServiceException {
        var fileDtoList = convertFromMultipartFilesToDto(files);
        setPriceHistoryToPost(post, priceHistoryIds);
        post.setUser(user);
        postService.updatePost(post, fileDtoList);
        return "redirect:/userposts";
    }

    /**
     * Найти список {@link PriceHistory},
     * которые принадлежат нашему {@link Post}
     * и засетить его.
     *
     * Из представления прилетает список
     * с дублями ID. Причину найти не  удалось.
     */
    private void setPriceHistoryToPost(Post post, List<Integer> priceHistoryIds) {
        var priceHistory = priceHistoryService.findAllByIds(priceHistoryIds)
                .stream()
                .distinct()
                .toList();
        post.setPriceHistory(new ArrayList<>(priceHistory));
    }

    @GetMapping("/delete/{id}")
    public String deletePost(Model model,
                             @PathVariable int id) throws ControllerException {
        try {
            postService.deleteById(id);
        } catch (SimpleServiceException e) {
            if (e.getCause() instanceof RepositoryException) {
                model.addAttribute("error", "File or files, attached to Post, does not exist(s)");
            }
            log.error("Cant delete Post. Controller exception logged: {}", Arrays.toString(e.getStackTrace()));
            throw new ControllerException("Post not found", e);
        }
        return "redirect:/userposts";
    }

    /**
     * Закрыть объявление, убрать в архив,
     * указав что машина продана.
     *
     * @param id ID объявления, которое хотите скрыть
     *           из списка машин на продажу
     */
    @GetMapping("/close/{id}")
    public String closePost(@PathVariable int id) {
        postService.updateSold(id);
        return "redirect:/userposts";
    }
}
