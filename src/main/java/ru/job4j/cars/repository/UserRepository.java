package ru.job4j.cars.repository;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * В данном классе мы использовали
 * аннотацию Lombok {@link Slf4j}
 * для дефолтной настройки логера.
 */
@Slf4j
@AllArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    /**
     * Сохранить в базе.
     *
     * Здесь мы произвели обычную вставку
     * в таблицу, поэтому используем метод
     * {@link Session#save}.
     *
     * Для данной операции используем
     * блок try-catch.
     *
     * В блоке finally необходимо закрывать
     * ресурс (по аналогии с Connection в
     * JDBC). Данный комментарий относится ко
     * всем методам, где мы не используем
     * try-with-resources.
     *
     * @param user пользователь
     * @return пользователь с ID
     */
    public User create(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("TRANSACTION ROLLBACK! Hibernate exception logged: {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     *
     * Для данной операции используем
     * блок try-catch.
     *
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User as user SET login = :fLogin, password = :fPassword WHERE user.id = :fId")
                    .setParameter("fLogin", user.getLogin())
                    .setParameter("fPassword", user.getPassword())
                    .setParameter("fId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("TRANSACTION ROLLBACK! Hibernate exception logged: {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Удалить пользователя по id.
     *
     * Для данной операции используем
     * блок try-catch.
     *
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE User as user WHERE user.id = :fId")
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("TRANSACTION ROLLBACK! Hibernate exception logged: {}", e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список пользователей, отсортированных по id.
     *
     * В данноме методе мы использовали
     * try-with-resources, поэтому наша
     * сессия закроется автоматически.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User user ORDER BY user.id", User.class);
            session.getTransaction().commit();
            return new ArrayList<>(query.list());
        }
    }

    /**
     * Найти пользователя по ID
     *
     * Здесь, чтобы вернуть Optional<User>,
     * мы используем специальный метод,
     * определенный в {@link Query} -
     * {@link Query#uniqueResultOptional}.
     *
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User user WHERE user.id = :fId", User.class);
            query.setParameter("fId", userId);
            session.getTransaction().commit();
            return query.uniqueResultOptional();
        }
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User user WHERE user.login LIKE :keyword", User.class);
            query.setParameter("keyword", "%" + key + "%");
            session.getTransaction().commit();
            return new ArrayList<>(query.list());
        }
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User user WHERE user.login = :fLogin", User.class);
            query.setParameter("fLogin", login);
            session.getTransaction().commit();
            return Optional.ofNullable(query.uniqueResult());
        }
    }
}
