package ru.job4j.cars.mapper.post;

import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Post;

/**
 * @author Constantine on 16.05.2024
 */
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDto mapPostToPostDto(Post post) {
        if (post == null) {
            return null;
        }
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setFiles(post.getFiles());
        postDto.setCarBrand(post.getCar().getBrand().getName());
        postDto.setCreated(post.getCreated());
        postDto.setCarBody(post.getCar().getBody().getName());
        postDto.setCarColor(post.getCar().getCarColor().getName());
        postDto.setCarYear(post.getCar().getCarYear());
        postDto.setGearBox(post.getCar().getGearBox().getName());
        postDto.setModel(post.getCar().getModel());
        postDto.setEngineCapacity(post.getCar().getEngine().getCapacity());
        postDto.setEngineHorsePower(post.getCar().getEngine().getHorsePower());
        postDto.setEngineFuelType(post.getCar().getEngine().getFuelType());
        postDto.setMileage(post.getCar().getMileage());
        postDto.setSummary(post.getSummary());
        postDto.setSold(post.isSold());
        postDto.setPrice(post.getPrice());
        return postDto;
    }
}
