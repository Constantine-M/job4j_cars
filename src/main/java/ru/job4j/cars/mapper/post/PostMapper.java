package ru.job4j.cars.mapper.post;

import org.mapstruct.Mapper;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Post;

/**
 * Данный интерфейс описывает
 * преобразование сущности (Entity)
 * {@link Post} в DTO {@link PostDto}.
 *
 * Для преобразования была выбрана
 * библиотека MapStruct.
 *
 * В аннотации {@link Mapper} свойство
 * componentModel = "spring" позволяет
 * добавить наш маппер в контекст.
 *
 * @author Constantine on 16.05.2024
 */
@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDto mapPostToPostDto(Post post);
}
