package ru.job4j.cars.repository.post;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Constantine on 17.03.2024
 */
@SpringBootTest
class PostRepositoryImplTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeAll
    static void initSession() {

    }

    @Disabled
    @Test
    void whenCreatePostThenGetTheSame() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        var engine = Engine.builder()
                .name("300HP")
                .build();
        var car = Car.builder()
                .name("Nissan")
                .engine(engine)
                .build();
        var owner = Owner.builder()
                .name("Owner")
                .build();
        var historyOwner = HistoryOwner.builder()
                .startAt(currentLocalDateTime.minusDays(1))
                .endAt(currentLocalDateTime)
                .owner(owner)
                .car(car)
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
        var user = User.builder()
                .login("login")
                .password("pass")
                .build();
        var post = Post.builder()
                .created(currentLocalDateTime)
                .description("Fast sale!")
                .price(10000L)
                .car(car)
                .files(List.of(file))
                .users(List.of(user))
                .priceHistory(List.of(priceHistory))
                .build();

        postRepository.create(post);
        var actualPost = postRepository.findById(6);
        assertThat(actualPost.get()).usingRecursiveComparison().isEqualTo(post);
    }

    @Test
    void whenFindAllByNameNissanThenGetOnePost() {
    }
}