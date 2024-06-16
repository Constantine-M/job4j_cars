package ru.job4j.cars.repository.user;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import javax.persistence.PersistenceException;
import java.sql.SQLException;
import java.util.*;

/**
 * Чтобы не дублировать весь код CRUD-операций,
 * мы вынесли его в отдельный класс
 * {@link CrudRepository}, в котором
 * будет использоваться одна абстрактная
 * команда, а в данном классе мы реализуем
 * эту абстрактную команду.
 */
@Slf4j
@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * Данный метод добавляет модель
     * {@link User} в persistent context.
     * По сути то же самое, что делает
     * {@link Session#save}, но есть нюансы.
     * {@link Session#persist} ничего не
     * возвращает, работает только в рамках
     * транзакции. Если вызвать метод у
     * detached-объекта, то будет выброшено
     * исключение {@link PersistenceException}.
     * 
     * В Hibernate все построено на
     * event-ах и listener-ах.
     * Создается event, который обрабатывается
     * соответствующим listener-ом
     * {@link PersistEventListener#onPersist},
     * который собственно и выполнит команду
     * на добавление user-а в persistent
     * context.
     *
     * @param user пользователь
     * @return пользователь с ID
     */
    public User create(User user) throws RepositoryException {
        try {
            crudRepository.run(session -> session.persist(user));
            return user;
        } catch (PersistenceException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RepositoryException("Repository exception: user cant save.", e);
        }
    }

    /**
     * Обновить в базе пользователя.
     *
     * Метод {@link Session#merge} по сути
     * то же самое, что и метод
     * {@link Session#update}.
     *
     * В {@link Session#merge} главным
     * является наш User (которого передали в
     * метод) - поля в сущности главнее, чем
     * то, что хранится в БД,
     *
     * 1.Производится запрос в БД
     * 2.Создается новая сущность на основании
     * данных User
     * 3.Устанавливаются значения полей
     * нового User из старого (которого
     * мы передали в метод).
     *
     * {@link Session#merge} переводит
     * состояние объекта из detached в
     * persistent.
     *
     * @param user пользователь.
     */
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    /**
     * Удалить пользователя по id.
     *
     * @param userId ID
     */
    public void delete(int userId) {
        crudRepository.run(
                "DELETE User as user WHERE user.id = :fId",
                Map.of("fId", userId)
        );
    }

    /**
     * Список пользователей, отсортированных по id.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        return crudRepository.query(
                "FROM User user ORDER BY user.id ASC",
                User.class
        );
    }

    /**
     * Найти пользователя по ID
     *
     * В данном методе мы не сможем поймать
     * исключение и обработать его, т.к.
     * результат выполнения метода обернут в
     * {@link Optional}.
     *
     * @return пользователь.
     */
    public Optional<User> findById(int userId) throws RepositoryException {
        var userOptional = crudRepository.optional(
                "FROM User user WHERE user.id = :fId",
                User.class,
                Map.of("fId", userId)
        );
        if (userOptional.isEmpty()) {
            throw new RepositoryException("Repository exception: cant find user with ID = ".concat(String.valueOf(userId)));
        }
        return userOptional;
    }

    /**
     * Список пользователей по login LIKE %key%.
     *
     * Важно: поиск регистрозависимый!
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) throws RepositoryException {
        try {
            return crudRepository.query(
                    "FROM User user WHERE user.login LIKE :keyword",
                    User.class,
                    Map.of("keyword", "%" + key + "%")
            );
        } catch (HibernateException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RepositoryException("Repository exception: cant find user by keyword", e);
        }
    }

    /**
     * Найти пользователя по login.
     *
     * Важно: поиск регистрозависимый!
     *
     * В данном методе мы не сможем поймать
     * исключение и обработать его, т.к.
     * результат выполнения метода обернут в
     * {@link Optional}.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) throws RepositoryException {
        var userOptional = crudRepository.optional(
                "FROM User user WHERE user.login = :fLogin",
                User.class,
                Map.of("fLogin", login)
        );
        return userOptional;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) throws RepositoryException {
        String hql = """
                    FROM User user
                    WHERE user.login = :fLogin AND user.password = :fPassword
                    """;
        var userOptional = crudRepository
                .optional(hql, User.class,
                        Map.of("fLogin", login, "fPassword", password)
                );
        if (userOptional.isEmpty()) {
            throw new RepositoryException("Repository exception: cant find user by login and password = ".concat(login));
        }
        return userOptional;
    }
}
