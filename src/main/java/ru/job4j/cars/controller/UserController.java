package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.exception.ControllerException;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.user.UserService;
import ru.job4j.cars.util.ExceptionUtil;
import ru.job4j.cars.util.TimeZoneUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Constantine on 10.05.2024
 */
@Slf4j
@RequestMapping("/users")
@AllArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    /**
     * Данный метод обрабатывает запрос на
     * аутентификацию пользователя.
     *
     * Чтобы закрепить открытую сессию
     * за пользователем, воспользуемся
     * интерфейсом {@link HttpServletRequest}
     * и получим сессию.
     *
     * Метод {@link HttpServletRequest#getSession()}
     * вернет нам объект {@link HttpSession}.
     *
     * А затем добавим данные в сессию
     * с помощью метода {@link HttpSession#setAttribute}.
     * Например, добавим туда пользователя.
     * Т.о. мы закрепили пользователя за сессией.
     * Данные сессии привязываются к клиенту
     * и доступны во всем приложении.
     *
     * Обрати внимание на то, что внутри HttpSession
     * используется многопоточная коллекция
     * ConcurrentHashMap. Это связано с многопоточным
     * окружением. Увидеть это можно в реализации
     * модуля catalina -> session
     * {@link org.apache.catalina.session.StandardSession}.
     */
    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user,
                            Model model,
                            HttpSession session) throws ControllerException {
        try {
            var userOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
            session.setAttribute("user", userOptional.get());
            return "redirect:/";
        } catch (SimpleServiceException e) {
            if (e.getCause() instanceof RepositoryException) {
                model.addAttribute("error", "The email or password is incorrect");
                return "users/login";
            }
            throw new ControllerException("Cant login user", e);
        }
    }

    /**
     * Метод обрабатывает запрос на получение
     * страницы регистрации пользователя.
     *
     * Во время регистрации пользователь может
     * выбрать часовой пояс. Логика работы с часовыми
     * поясами вынесена в утилитный класс.
     * В контроллере это излишне.
     */
    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("timeZones", TimeZoneUtil.getAllAvailableTimeZones());
        return "users/register";
    }

    /**
     * Данный метод обрабытвает запрос
     * на регистрацию пользователя.
     *
     * Если регистрация прошла успешно,
     * то пользователя перебрасывает на
     * страницу входа.
     *
     * В случае неуспешного выполнения
     * метода полуцчим цепочку исключений
     * RepositoryException --> SimpleServiceException.
     *
     * SimpleServiceException в даннои случае
     * является первопричиной. Если бы в
     * {@link UserService#createUser(User)}
     * мы выбросили RepositoryException, то
     * первопричиной стало бы именно это
     * исключение.
     */
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           Model model) throws ControllerException {
        try {
            userService.createUser(user);
        } catch (SimpleServiceException e) {
            if (ExceptionUtil.getRootCause(e) instanceof SimpleServiceException) {
                model.addAttribute("error", "User already exists with this login!");
                return "users/register";
            }
            throw new ControllerException("User cant reg", e);
        }
        return "users/login";
    }

    /**
     * Данный метод обрабатывает запрос
     * пользователя, который закрывает сессию
     * (разлогинивается).
     *
     * В данном случае, чтобы удалить
     * все данные, связанные с пользователем,
     * нужно использовать метод
     * {@link HttpSession#invalidate()}.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:users/login";
    }
}
