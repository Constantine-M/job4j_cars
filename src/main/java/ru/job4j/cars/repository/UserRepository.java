package ru.job4j.cars.repository;


import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.event.spi.PersistEventListener;
import ru.job4j.cars.model.User;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Чтобы не дублировать весь код CRUD-операций,
 * мы вынесли его в отдельный класс
 * {@link CrudRepository}, в котором
 * будет использоваться одна абстрактная
 * команда, а в данном классе мы реализуем
 * эту абстрактную команду.
 */
@AllArgsConstructor
public class UserRepository {

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
    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
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
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "FROM User user WHERE user.id = :fId",
                User.class,
                Map.of("fId", userId)
        );
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "FROM User user WHERE user.login LIKE :keyword",
                User.class,
                Map.of("keyword", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "FROM User user WHERE user.login = :fLogin",
                User.class,
                Map.of("fLogin", login)
        );
    }
}
