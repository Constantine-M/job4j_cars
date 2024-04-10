package ru.job4j.cars.repository.post;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.car.CarRepository;
import ru.job4j.cars.repository.engine.EngineRepository;
import ru.job4j.cars.repository.file.FileRepository;
import ru.job4j.cars.repository.owner.OwnerRepository;
import ru.job4j.cars.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Constantine on 17.03.2024
 */
@Disabled
@SpringBootTest
class PostRepositoryImplTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void whenCreatePostThenGetTheSame() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        var engine = Engine.builder()
                .name("300HP")
                .build();
        var car = Car.builder()
                .name("Nissan")
                .historyOwners(emptyList())
                .engine(engine)
                .build();
        var file = File.builder()
                .name("Nissan-Rogue-2021")
                .path("files/Nissan-Rogue-2021.jpeg")
                .build();
        var priceHistory = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(5000L)
                .after(10000L)
                .build();
        var post = Post.builder()
                .created(currentLocalDateTime)
                .description("Fast sale!")
                .price(10000L)
                .build();
        post.setCar(car);
        post.setFiles(List.of(file));
        post.setPriceHistory(List.of(priceHistory));
        postRepository.create(post);
        var actualPost = postRepository.findById(1);
        assertThat(actualPost.get()).usingRecursiveComparison().isEqualTo(post);
    }

}