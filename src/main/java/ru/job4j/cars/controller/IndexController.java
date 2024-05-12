package ru.job4j.cars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Данный класс находится в слое
 * контроллеров и обрабатывает запросы
 * пользователя.
 *
 * Индексный файл ищется по умолчанию
 * веб-сервером, если в URL указан не файл, а каталог.
 *
 * Если мы возвращаем просто строку, то
 * можно использовать аннотацию
 * {@link RestController} вместо
 * {@link Controller}, т.к. первая
 * аннотация сочетает в себе
 * Controller и ResponseBody. Таким образом,
 * нам не нужно оборачивать наш
 * ответ в ResponseBody.
 *
 * @author Constantine on 10.05.2024
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    public String getIndex() {
        return "/index";
    }
}
