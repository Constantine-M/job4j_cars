package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Constantine on 05.03.2024
 */
@Slf4j
@AllArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final CrudRepository crudRepository;

    /**
     * Найти все объявления.
     *
     * @return список объявлений
     */
    @Override
    public Collection<Post> findAll() {
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory
                    JOIN FETCH post.files
                    """;
        return crudRepository.query(hql, Post.class);
    }

    /**
     * Найти все объявления за последний день.
     *
     * @return список объявлений за последние 24 часа.
     */
    public Collection<Post> findAllLastDay() {
//        String hql = """
//                    SELECT DISTINCT post
//                    FROM Post post
//                    JOIN FETCH post.priceHistory
//                    LEFT JOIN FETCH post.files
//                    WHERE post.created > :lastTime
//                    """;
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory
                    LEFT JOIN FETCH post.files
                    WHERE post.created > :lastTime
                    """;
        var localDateTimeAtUTCZone = LocalDateTime.now(ZoneId.of("UTC"));
        return crudRepository.query(hql, Post.class,
                Map.of("lastTime", localDateTimeAtUTCZone.minusDays(1))
        );
    }

    /**
     * Показать все объявления с фото.
     *
     * @return список объявлений с фотографиями.
     */
    public Collection<Post> findAllWithPhoto() {
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory
                    JOIN FETCH post.files
                    WHERE size(post.files) <> 0
                    """;
        return crudRepository.query(hql, Post.class);
    }

    /**
     * Найти все объявления определенной марки.
     *
     * Поиск будет осуществляться по полю name
     * в {@link Car}, по частичному совпадению.
     *
     * @param model марка авто.
     * @return список объявлений по продаже авто
     * определенной марки.
     */
    public Collection<Post> findAllByName(String model) {
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory
                    LEFT JOIN FETCH post.files
                    WHERE post.car.model LIKE :brand
                    """;
        return crudRepository.query(hql, Post.class,
                Map.of("brand", "%" + model + "%")
        );
    }

    /**
     * Сохранить объявление в БД.
     *
     * @param post объявление о продаже машины
     */
    @Override
    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    /**
     * Найти объявление по ID.
     *
     * @param id идентификатор объявления
     */
    @Override
    public Optional<Post> findById(int id) {
        var hql = """
                FROM Post post
                JOIN FETCH post.priceHistory
                JOIN FETCH post.files
                WHERE post.id = :fId
                """;
        return crudRepository.optional(hql, Post.class, Map.of("fId", id));
    }

    @Override
    public void updatePost(Post post) {
        crudRepository.run(session -> session.update(post));
    }
}
