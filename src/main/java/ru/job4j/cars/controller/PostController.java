package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.dto.PostFilter;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.service.body.BodyService;
import ru.job4j.cars.service.brand.CarBrandService;
import ru.job4j.cars.service.post.PostService;
import ru.job4j.cars.util.SearchValidator;

/**
 * @author Constantine on 10.05.2024
 */
@Slf4j
@RequestMapping({"/posts", "/"})
@AllArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final CarBrandService carBrandService;
    private final BodyService bodyService;

    private final SearchValidator searchValidator;

    @GetMapping
    public String getAllPosts(Model model,
                              @ModelAttribute PostFilter postFilter,
                              BindingResult br) {
        model.addAttribute("carBrand", carBrandService.findAll());
        model.addAttribute("body", bodyService.findAll());

        searchValidator.validate(postFilter, br);
        if (!isSearchActive(br)) {
            model.addAttribute("posts", postService.findAllPostsDto());
        } else {
            model.addAttribute("posts", postService.findAllPostDtoByQuerydsl(postFilter));
        }
        return "posts/all";
    }

    /**
     * Получить объявление по ID.
     *
     * Столкнулся с тем, что история изменения цен
     * дублируется. Решил тем, что в методе
     * добавил переменную, в которую собрал
     * список уникальных {@link PriceHistory}.
     */
    @GetMapping("/{id}")
    public String getPostById(@PathVariable int id,
                              Model model) {
        var postOptional = postService.findById(id);
        if (postOptional.isEmpty()) {
            model.addAttribute("error", "Post with ID = " + id + " not found");
            return "/error/404";
        }
        var priceDynamicList = postOptional.get().getPriceHistory()
                                                    .stream()
                                                    .distinct()
                                                    .toList();
        model.addAttribute("post", postOptional.get());
        model.addAttribute("priceDynamicList", priceDynamicList);
        return "posts/detail";
    }

    /**
     * Проверить заполнение полей {@link PostFilter}.
     *
     * @return false, если не заполнены все поля.
     * true, если заполнено хотя бы одно поле.
     */
    private boolean isSearchActive(BindingResult br) {
        return br.getErrorCount() < PostFilter.class.getDeclaredFields().length;
    }
}
