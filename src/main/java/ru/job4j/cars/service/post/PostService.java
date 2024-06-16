package ru.job4j.cars.service.post;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.dto.PostFilter;
import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.model.File;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * @author Constantine on 01.05.2024
 */
public interface PostService {

    Post savePost(Post post, Set<FileDto> fileDto);

    Collection<PostDto> findAllPostsDtoByUser(User user);

    Collection<PostDto> findAllPostsDto();

    Collection<PostDto> findAllPostDtoByQuerydsl(PostFilter filter);

    Optional<Post> findById(int id);

    void updatePost(Post post, Set<FileDto> fileDto) throws SimpleServiceException;

    void deleteById(int id) throws SimpleServiceException;

    void updateSold(int postId);
}
