package ru.job4j.cars.repository.post;

import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.Post;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 05.03.2024
 */
public interface PostRepository {

    Collection<Post> findAll();

    Post create(Post post);

    Optional<Post> findById(int id) throws RepositoryException;

    void updatePost(Post post);

    Collection<Post> findAllLastDay();

    Collection<Post> findAllWithPhoto();

    Collection<Post> findAllByNameLike(String brand);
}
