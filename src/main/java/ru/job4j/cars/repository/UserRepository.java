package ru.job4j.cars.repository;


import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

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
            LOG.error("HIBERNATE EXCEPTION LOGGED {}", e.getMessage());
            session.getTransaction().rollback();
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
            LOG.error("HIBERNATE EXCEPTION LOGGED {}", e.getMessage());
            session.getTransaction().rollback();
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
            LOG.error("HIBERNATE EXCEPTION LOGGED {}", e.getMessage());
            session.getTransaction().rollback();
        }
    }

    /**
     * Список пользователей, отсортированных по id.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("FROM User user ORDER BY user.id");
        return new ArrayList<>(query.list());
    }

    /**
     * Найти пользователя по ID
     *
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery(
                "FROM User user WHERE user.id = :fId", User.class
        );
        query.setParameter("fId", userId);
        return Optional.ofNullable(query.uniqueResult());
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("FROM User user WHERE user.login LIKE :keyword", User.class);
        query.setParameter("keyword", "%" + key + "%");
        return new ArrayList<>(query.list());
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("FROM User user WHERE user.login = :fLogin", User.class);
        query.setParameter("fLogin", login);
        return Optional.ofNullable(query.uniqueResult());
    }
}
