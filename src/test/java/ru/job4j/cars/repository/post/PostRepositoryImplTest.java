package ru.job4j.cars.repository.post;

import org.junit.jupiter.api.Disabled;
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
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Constantine on 17.03.2024
 */
@Disabled
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class PostRepositoryImplTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void whenCreatePostThenGetTheSame() {
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
//                .type("diesel")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
//                .body("pickup")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
//                .color("red")
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
//                .body("lived-back")
                .build();
        var engineLada = Engine.builder()
                .horsePower(75)
                .capacity(1.5F)
//                .type("benzino")
                .build();
        var carLada = Car.builder()
//                .color("silver")
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
    void whenCannotFindThePostWithIdEqualsTo3ThenGetAnEmptyOptional() {
        assertThat(postRepository.findById(3)).isEqualTo(Optional.empty());
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
//                .type("diesel")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
//                .body("pickup")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
//                .color("red")
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
//                .type("gasoline")
                .capacity(1.6F)
                .horsePower(110)
                .build();
        var body2 = Body.builder()
//                .body("sedan")
                .build();
        var car2 = Car.builder()
//                .color("yellow")
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

    @Test
    void whenAddTwoPostsThenGetOnlyOneWithPhoto() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("owner")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
//                .type("diesel")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
//                .body("pickup")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
//                .color("red")
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
//                .type("gasoline")
                .capacity(1.6F)
                .horsePower(110)
                .build();
        var body2 = Body.builder()
//                .body("sedan")
                .build();
        var car2 = Car.builder()
//                .color("yellow")
                .model("Vesta")
                .mileage(56000)
                .build();
        car2.setPassport(passport2);
        car2.setBody(body2);
        car2.setEngine(engine2);
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
        post.setFiles(emptySet());
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
        var expected = List.of(post2);
        assertThat(postRepository.findAllWithPhoto()).isEqualTo(expected);
    }

    @Test
    void whenAddThreePostsThenGet2OfThemCreatedByLastDay() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("owner")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
//                .type("diesel")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
//                .body("pickup")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
//                .color("red")
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
//                .type("gasoline")
                .capacity(1.6F)
                .horsePower(110)
                .build();
        var body2 = Body.builder()
//                .body("sedan")
                .build();
        var car2 = Car.builder()
//                .color("yellow")
                .model("Vesta")
                .mileage(56000)
                .build();
        car2.setPassport(passport2);
        car2.setBody(body2);
        car2.setEngine(engine2);
        var priceHistory = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post = Post.builder()
                .price(0L)
                .title("Test title")
                .created(currentLocalDateTime.minusHours(2))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(emptySet());
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
                .created(currentLocalDateTime.minusDays(10))
                .sold(true)
                .title("Fast sale Lada!")
                .description("Fast please!")
                .build();
        post2.setCar(car2);
        post2.setPriceHistory(List.of(priceHistory2));
        post2.setFiles(Set.of(file2));
        var owner3 = Owner.builder()
                .name("owner3")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine3 = Engine.builder()
//                .type("electro")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body3 = Body.builder()
//                .body("hatchback")
                .build();
        var passport3 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner3))
                .build();
        var car3 = Car.builder()
//                .color("white")
                .model("Tesla")
                .mileage(88000)
                .build();
        car3.setBody(body3);
        car3.setEngine(engine3);
        car3.setPassport(passport3);
        var priceHistory3 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(0L)
                .after(200L)
                .build();
        var post3 = Post.builder()
                .price(0L)
                .title("Test electro title")
                .created(currentLocalDateTime.minusHours(8))
                .description("Test3 description")
                .sold(false)
                .build();
        var file3 = File.builder()
                .name("Test1")
                .path("/test1_path")
                .build();
        post3.setFiles(Set.of(file3));
        post3.setPriceHistory(List.of(priceHistory3));
        post3.setCar(car3);
        postRepository.create(post);
        postRepository.create(post2);
        postRepository.create(post3);
        var expected = List.of(post, post3);
        var actual = postRepository.findAllLastDay();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenAddThreePostsThenGetTwoOfThemWithModelNameContainedLada() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("owner")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = Engine.builder()
//                .type("diesel")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body = Body.builder()
//                .body("pickup")
                .build();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
//                .color("red")
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
//                .type("gasoline")
                .capacity(1.6F)
                .horsePower(110)
                .build();
        var body2 = Body.builder()
//                .body("sedan")
                .build();
        var car2 = Car.builder()
//                .color("yellow")
                .model("Lada Vesta")
                .mileage(56000)
                .build();
        car2.setPassport(passport2);
        car2.setBody(body2);
        car2.setEngine(engine2);
        var priceHistory = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post = Post.builder()
                .price(0L)
                .title("Test title")
                .created(currentLocalDateTime.minusHours(2))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(emptySet());
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
                .created(currentLocalDateTime.minusDays(10))
                .sold(true)
                .title("Fast sale Lada!")
                .description("Fast please!")
                .build();
        post2.setCar(car2);
        post2.setPriceHistory(List.of(priceHistory2));
        post2.setFiles(Set.of(file2));
        var owner3 = Owner.builder()
                .name("owner3")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine3 = Engine.builder()
//                .type("electro")
                .capacity(6.5F)
                .horsePower(280)
                .build();
        var body3 = Body.builder()
//                .body("hatchback")
                .build();
        var passport3 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner3))
                .build();
        var car3 = Car.builder()
//                .color("white")
                .model("Lada Largus E")
                .mileage(88000)
                .build();
        car3.setBody(body3);
        car3.setEngine(engine3);
        car3.setPassport(passport3);
        var priceHistory3 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(0L)
                .after(200L)
                .build();
        var post3 = Post.builder()
                .price(0L)
                .title("Test electro title")
                .created(currentLocalDateTime.minusHours(8))
                .description("Test3 description")
                .sold(false)
                .build();
        var file3 = File.builder()
                .name("Test1")
                .path("/test1_path")
                .build();
        post3.setFiles(Set.of(file3));
        post3.setPriceHistory(List.of(priceHistory3));
        post3.setCar(car3);
        postRepository.create(post);
        postRepository.create(post2);
        postRepository.create(post3);
        var expected = List.of(post2, post3);
        var actual = postRepository.findAllByName("Lada");
        assertThat(actual).isEqualTo(expected);
    }
}