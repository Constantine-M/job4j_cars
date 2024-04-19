package ru.job4j.cars.repository.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Constantine on 17.03.2024
 */
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class PostRepositoryImplTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void whenCreatePostThenGetTheSame() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var file = File.builder()
                .name("Test car")
                .path("/testpath")
                .build();
        var priceHistory = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post = Post.builder()
                .price(0L)
                .title("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(Set.of(file));
        post.setPriceHistory(List.of(priceHistory));
        postRepository.create(post);
        var actualPost = postRepository.findById(1);
        assertThat(actualPost.get())
                .usingRecursiveComparison()
                .ignoringFields("car")
                .isEqualTo(post);
    }

    @Test
    void whenUpdateCarFromFordToLadaInPostThenGetPostWithLada() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("owner")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
                .type("diesel")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
                .body("pickup")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .color("red")
                .model("Ford F150")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        var file = File.builder()
                .name("Test car")
                .path("/testpath")
                .build();
        var priceHistory = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post = Post.builder()
                .price(0L)
                .title("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(Set.of(file));
        post.setPriceHistory(List.of(priceHistory));
        post.setCar(car);
        postRepository.create(post);
        var ownerEddie = Owner.builder()
                .name("Ed")
                .start(currentLocalDateTime.minusDays(20))
                .end(currentLocalDateTime)
                .build();
        var passportLada = AutoPassport.builder()
                .original(true)
                .owners(Set.of(ownerEddie))
                .build();
        var bodyLada = Body.builder()
                .body("lived-back")
                .build();
        var engineLada = Engine.builder()
                .horsePower(75)
                .capacity(1.5F)
                .type("benzino")
                .build();
        var carLada = Car.builder()
                .color("silver")
                .model("Granta")
                .mileage(0)
                .build();
        carLada.setBody(bodyLada);
        carLada.setEngine(engineLada);
        carLada.setPassport(passportLada);
        post.setCar(carLada);
        postRepository.updatePost(post);
        assertThat(postRepository.findById(1).get().getCar().getModel())
                .isEqualTo("Granta");
    }

    @Test
    void whenCannotFindThePostWithIdEqualsTo3ThenThrowsRepositoryException() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var file = File.builder()
                .name("Test car")
                .path("/testpath")
                .build();
        var priceHistory = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post = Post.builder()
                .price(0L)
                .title("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(Set.of(file));
        post.setPriceHistory(List.of(priceHistory));
        postRepository.create(post);
        assertThatThrownBy(() -> postRepository.findById(3))
                .isInstanceOf(RepositoryException.class)
                .hasMessage("Post with id = 3 not found");
    }

    @Test
    void whenFindAllThenGetListOfTwoPostsWithFordAndLada() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("owner")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
                .type("diesel")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
                .body("pickup")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .color("red")
                .model("Ford F150")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        var owner2 = Owner.builder()
                .name("owner 2")
                .start(currentLocalDateTime.minusDays(10))
                .end(currentLocalDateTime)
                .build();
        var passport2 = AutoPassport.builder()
                .original(false)
                .owners(Set.of(owner2))
                .build();
        var engine2 = Engine.builder()
                .type("gasoline")
                .capacity(1.6F)
                .horsePower(110)
                .build();
        var body2 = Body.builder()
                .body("sedan")
                .build();
        var car2 = Car.builder()
                .color("yellow")
                .model("Vesta")
                .mileage(56000)
                .build();
        car2.setPassport(passport2);
        car2.setBody(body2);
        car2.setEngine(engine2);
        var file = File.builder()
                .name("Test car")
                .path("/testpath")
                .build();
        var priceHistory = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post = Post.builder()
                .price(0L)
                .title("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(Set.of(file));
        post.setPriceHistory(List.of(priceHistory));
        post.setCar(car);
        var file2 = File.builder()
                .name("Test file")
                .path("/test")
                .build();
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(10L)
                .after(200L)
                .build();
        var post2 = Post.builder()
                .created(currentLocalDateTime)
                .sold(true)
                .title("Fast sale Lada!")
                .description("Fast please!")
                .build();
        post2.setCar(car2);
        post2.setPriceHistory(List.of(priceHistory2));
        post2.setFiles(Set.of(file2));
        postRepository.create(post);
        postRepository.create(post2);
        var expected = List.of(post, post2);
        assertThat(postRepository.findAll()).isEqualTo(expected);
    }
}