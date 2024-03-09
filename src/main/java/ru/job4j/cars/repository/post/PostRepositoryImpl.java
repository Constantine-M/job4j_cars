package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

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
                    JOIN FETCH post.priceHistory JOIN FETCH post.users
                    """;
        return crudRepository.query(hql, Post.class);
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
                JOIN FETCH post.priceHistory JOIN FETCH post.users
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
