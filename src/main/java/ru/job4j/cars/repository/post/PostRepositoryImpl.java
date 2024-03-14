package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    private final SessionFactory sessionFactory;

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
                    JOIN FETCH post.priceHistory JOIN FETCH post.users JOIN FETCH post.files
                    """;
        return crudRepository.query(hql, Post.class);
    }

    /**
     * Найти все объявления за последний день.
     *
     * @return список объявлений за последние 24 часа.
     */
    public Collection<Post> findAllLastDay() {
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory JOIN FETCH post.users JOIN FETCH post.files
                    WHERE post.created BETWEEN :startDay AND :endDay
                    """;
        var localDateTimeAtUTCZone = LocalDateTime.now(ZoneId.of("UTC"));
        return crudRepository.query(hql, Post.class,
                Map.of("startDay", localDateTimeAtUTCZone.minusDays(1),
                        "endDay", localDateTimeAtUTCZone)
        );
    }

    /**
     * Показать все объявления с фото.
     *
     * В данном методе мы используем JPA Criteria API.
     * Благодаря ему мы можем конструировать запросы
     * любой сложности, не прибегая к нативным запросам.
     *
     * 1.Создаем {@link org.hibernate.Session}
     * 2.Создаем {@link CriteriaBuilder}
     * 3.Создаем {@link CriteriaQuery}
     * 4.Модифицируем наш criteriaQuery,
     * добавляя части из пп.2 и 3
     * 5.Создаем запрос и в качестве параметра передаем
     * сформированный ранее criteriaQuery.
     * 6.Получаем итоговый список.
     *
     * @return список объявлений с фотографиями.
     */
    public Collection<Post> findAllWithPhoto() {
        var session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root<Post> root = criteriaQuery.from(Post.class);
        CriteriaQuery<Post> postsWithPhoto = criteriaQuery
                .select(root)
                .where(criteriaBuilder
                        .isNotEmpty(root.get("fileId")));
        var query = session.createQuery(postsWithPhoto);
        return query.getResultList();
    }

    /**
     * Найти все объявления определенной марки.
     *
     * Поиск будет осуществляться по полю name
     * в {@link Car}, по содержанию.
     *
     * @param brand марка авто.
     * @return список объявлений по продаже авто
     * определенной марки.
     */
    public Collection<Post> findAllByNameLike(String brand) {
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory JOIN FETCH post.users JOIN FETCH post.files
                    WHERE post.car = (
                        SELECT FROM Car car
                        WHERE car.name LIKE :key)
                    """;
        return crudRepository.query(hql, Post.class,
                Map.of("key", "%" + brand + "%")
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
    public Optional<Post> findById(int id) throws RepositoryException {
        var hql = """
                FROM Post post 
                JOIN FETCH post.priceHistory JOIN FETCH post.users JOIN FETCH post.files
                WHERE post.id = :fId
                """;
        try {
            return crudRepository.optional(hql, Post.class,
                    Map.of("fId", id)
            );
        } catch (HibernateException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RepositoryException("Repository exception: cant find post.", e);
        }
    }

    @Override
    public void updatePost(Post post) {
        crudRepository.run(session -> session.update(post));
    }
}
