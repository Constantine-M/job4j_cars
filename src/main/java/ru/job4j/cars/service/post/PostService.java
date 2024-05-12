package ru.job4j.cars.service.post;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.model.Post;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Constantine on 01.05.2024
 */
public interface PostService {

    Post savePost(Post post, Collection<FileDto> fileDto);

    Collection<Post> findAllPosts();
}
