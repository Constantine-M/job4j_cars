package ru.job4j.cars.repository.post;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.dto.PostFilter;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

import static ru.job4j.cars.model.QPost.post;

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
     * Методом проб и дебага выяснил, что
     * при создании {@link Post} нужно
     * заполнять все поля, в т.ч.
     * {@link PriceHistory}, иначе в результате
     * будем получать пустой список из-за
     * того, что список {@link PriceHistory}
     * пустой.
     *
     * @return список объявлений
     */
    @Override
    public Collection<Post> findAll() {
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory
                    LEFT JOIN FETCH post.files
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
                    LEFT JOIN FETCH post.files
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
     * @param brand марка авто.
     * @return список объявлений по продаже авто
     * определенной марки.
     */
    public Collection<Post> findAllByName(String brand) {
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory
                    LEFT JOIN FETCH post.files
                    WHERE post.car.brand.name LIKE :brand
                    """;
        return crudRepository.query(hql, Post.class,
                Map.of("brand", "%" + brand + "%")
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
                LEFT JOIN FETCH post.files
                WHERE post.id = :fId
                """;
        return crudRepository.optional(hql, Post.class, Map.of("fId", id));
    }

    @Override
    public void updatePost(Post post) {
        crudRepository.run(session -> session.update(post));
    }

    /**
     * Найти все объявления пользователя.
     *
     * @param user залогиненый пользователь
     */
    @Override
    public Collection<Post> findAllByUser(User user) {
        String hql = """
                    SELECT DISTINCT post
                    FROM Post post
                    JOIN FETCH post.priceHistory
                    LEFT JOIN FETCH post.files
                    WHERE post.user = :user
                    """;
        return crudRepository.query(hql, Post.class,
                Map.of("user", user));
    }

    /**
     * Удалить по ID.
     *
     * @param id идентификатор объявления
     */
    @Override
    public void deleteById(int id) {
        var hql = """
                DELETE FROM Post post
                WHERE post.id = :fId
                """;
        crudRepository.run(hql, Map.of("fId", id));
    }

    @Override
    public void delete(Post post) {
        crudRepository.run(session -> session.delete(post));
    }

    /**
     * Найти все объявления.
     *
     * Поиск объявлений по фильтру.
     * loe = lessOrEqual
     * goe = greaterOrEqual
     */
    @Override
    public Collection<Post> findAllByQuerydsl(PostFilter filter) {
        Function<Session, List<Post>> command = session -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.isFileExists()) {
                predicates.add(post.files.isNotEmpty());
            }
            if (filter.getCreatedDaysBefore() > 0) {
                predicates.add(post.created.between(
                        LocalDateTime.now(ZoneId.of("UTC")).minusDays(filter.getCreatedDaysBefore()),
                        LocalDateTime.now(ZoneId.of("UTC"))
                ));
            }
            if (filter.getLowestPrice() != 0) {
                predicates.add(post.price.goe(filter.getLowestPrice()));
            }
            if (filter.getHighestPrice() != 0) {
                predicates.add(post.price.loe(filter.getHighestPrice()));
            }
            if (filter.getCar().getBrand().getId() != 0) {
                predicates.add(post.car.brand.id.eq(filter.getCar().getBrand().getId()));
            }
            if (!filter.getCar().getModel().isEmpty()) {
                predicates.add(post.car.model.like(filter.getCar().getModel()));
            }
            if (filter.getCar().getMileage() != 0) {
                predicates.add(post.car.mileage.loe(filter.getCar().getMileage()));
            }
            if (filter.getCar().getBody().getId() != 0) {
                predicates.add(post.car.body.id.eq(filter.getCar().getBody().getId()));
            }
            predicates.add(post.sold.isFalse());
            return new JPAQuery<Post>(session)
                    .select(post)
                    .distinct()
                    .from(post)
                    .leftJoin(post.priceHistory).fetchJoin()
                    .leftJoin(post.files).fetchJoin()
                    .where(predicates.toArray(Predicate[]::new))
                    .fetch();
        };
        return crudRepository.tx(command);
    }
}
