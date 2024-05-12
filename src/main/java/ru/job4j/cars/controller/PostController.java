package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.AutoPassport;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.body.BodyService;
import ru.job4j.cars.service.brand.CarBrandService;
import ru.job4j.cars.service.color.CarColorService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.post.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Constantine on 10.05.2024
 */
@Slf4j
@RequestMapping("/posts")
@AllArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final CarBrandService carBrandService;
    private final BodyService bodyService;
    private final EngineService engineService;
    private final CarColorService carColorService;

    @GetMapping
    public String getAllPosts(Model model,
                              @SessionAttribute User user) {
        model.addAttribute("posts", postService.findAllPosts());
        return "posts/all";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("carBrand", carBrandService.findAll());
        model.addAttribute("body", bodyService.findAll());
        model.addAttribute("engine", engineService.findAll());
        model.addAttribute("carColor", carColorService.findAll());
        return "posts/create";
    }

    @PostMapping("/create")
    public String createPost(Model model,
                             @SessionAttribute User user,
                             @ModelAttribute Post post,
                             @RequestParam("files") List<MultipartFile> files,
                             @ModelAttribute Owner owner,
                             @ModelAttribute("passport") AutoPassport autoPassport) {
        var fileDtoList = convertFromMultipartFilesToDto(files);
        post.setUser(user);
        postService.savePost(post, fileDtoList);
        return "posts/create";
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
    private List<FileDto> convertFromMultipartFilesToDto(List<MultipartFile> files) {
        List<FileDto> result = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                result.add(new FileDto(file.getOriginalFilename(), file.getBytes()));
            }
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return result;
    }
}
