package ru.job4j.cars.repository.post;

import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.PostFilter;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 05.03.2024
 */
public interface PostRepository {

    Collection<Post> findAll();

    Collection<Post> findAllByUser(User user);

    Post create(Post post);

    Optional<Post> findById(int id);

    void updatePost(Post post);

    Collection<Post> findAllLastDay();

    Collection<Post> findAllWithPhoto();

    Collection<Post> findAllByName(String brand);

    void deleteById(int id);

    void delete(Post post);

    Collection<Post> findAllByQuerydsl(PostFilter filter);
}
