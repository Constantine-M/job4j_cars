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
import ru.job4j.cars.repository.body.BodyRepository;
import ru.job4j.cars.repository.brand.CarBrandRepository;
import ru.job4j.cars.repository.car.CarRepository;
import ru.job4j.cars.repository.color.CarColorRepository;
import ru.job4j.cars.repository.engine.EngineRepository;

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
//@Disabled
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class PostRepositoryImplTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BodyRepository bodyRepository;

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private CarColorRepository carColorRepository;

    @Autowired
    private EngineRepository engineRepository;

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
        var actualPost = postRepository.findById(1).get();
        assertThat(actualPost)
                .usingRecursiveComparison()
                .isEqualTo(post);
    }

    @Test
    void whenUpdateCarFromToyotaToVolkswagenInPostThenGetPostWithVolkswagen() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("Eddie")
                .start(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MINUTES))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .model("Crown")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
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
        var engine2 = engineRepository.findById(1).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(3).get();
        var passport2 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var newCar = Car.builder()
                .model("Jetta")
                .mileage(130000)
                .build();
        newCar.setBody(body2);
        newCar.setEngine(engine2);
        newCar.setPassport(passport2);
        newCar.setCarColor(color2);
        newCar.setBrand(carBrand2);
        post.setCar(newCar);
        postRepository.updatePost(post);
        assertThat(postRepository.findById(1).get().getCar())
                .usingRecursiveComparison()
                .isEqualTo(newCar);
    }

    @Test
    void whenCannotFindThePostWithIdEqualsTo3ThenGetAnEmptyOptional() {
        assertThat(postRepository.findById(3)).isEqualTo(Optional.empty());
    }

    @Test
    void whenFindAllThenGetListOfTwoPostsWithToyotaAndVW() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("Eddie")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .model("Crown")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
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
                .title("Test sale Crown")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(Set.of(file));
        post.setPriceHistory(List.of(priceHistory));
        post.setCar(car);
        postRepository.create(post);
        var owner2 = Owner.builder()
                .name("Consta")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine2 = engineRepository.findById(2).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(1).get();
        var passport2 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner2))
                .build();
        var car2 = Car.builder()
                .model("Golf")
                .mileage(88000)
                .build();
        car2.setBody(body2);
        car2.setEngine(engine2);
        car2.setPassport(passport2);
        car2.setCarColor(color2);
        car2.setBrand(carBrand2);
        var file2 = File.builder()
                .name("Test car 2")
                .path("/testpath_2")
                .build();
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post2 = Post.builder()
                .price(0L)
                .title("Test sale VW")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post2.setFiles(Set.of(file2));
        post2.setPriceHistory(List.of(priceHistory2));
        post2.setCar(car2);
        postRepository.create(post2);
        var expected = List.of(post, post2);
        assertThat(postRepository.findAll()).isEqualTo(expected);
    }

    @Test
    void whenAddTwoPostsThenGetOnlyOneWithPhoto() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("Eddie")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .model("Crown")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
        var priceHistory = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post = Post.builder()
                .price(0L)
                .title("Test sale Crown")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(emptySet());
        post.setPriceHistory(List.of(priceHistory));
        post.setCar(car);
        postRepository.create(post);
        var owner2 = Owner.builder()
                .name("Consta")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine2 = engineRepository.findById(2).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(1).get();
        var passport2 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner2))
                .build();
        var car2 = Car.builder()
                .model("Golf")
                .mileage(88000)
                .build();
        car2.setBody(body2);
        car2.setEngine(engine2);
        car2.setPassport(passport2);
        car2.setCarColor(color2);
        car2.setBrand(carBrand2);
        var file2 = File.builder()
                .name("Test car 2")
                .path("/testpath_2")
                .build();
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post2 = Post.builder()
                .price(0L)
                .title("Test sale VW")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post2.setFiles(Set.of(file2));
        post2.setPriceHistory(List.of(priceHistory2));
        post2.setCar(car2);
        postRepository.create(post2);
        var expected = List.of(post2);
        assertThat(postRepository.findAllWithPhoto()).isEqualTo(expected);
    }

    @Test
    void whenAddThreePostsThenGet2OfThemCreatedByLastDay() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("Eddie")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .model("Crown")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
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
                .title("Test sale Crown")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(Set.of(file));
        post.setPriceHistory(List.of(priceHistory));
        post.setCar(car);
        postRepository.create(post);
        var owner2 = Owner.builder()
                .name("Consta")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine2 = engineRepository.findById(2).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(1).get();
        var passport2 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner2))
                .build();
        var car2 = Car.builder()
                .model("Golf")
                .mileage(88000)
                .build();
        car2.setBody(body2);
        car2.setEngine(engine2);
        car2.setPassport(passport2);
        car2.setCarColor(color2);
        car2.setBrand(carBrand2);
        var file2 = File.builder()
                .name("Test car 2")
                .path("/testpath_2")
                .build();
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post2 = Post.builder()
                .price(0L)
                .title("Test sale VW")
                .created(currentLocalDateTime)
                .description("Test description")
                .sold(false)
                .build();
        post2.setFiles(Set.of(file2));
        post2.setPriceHistory(List.of(priceHistory2));
        post2.setCar(car2);
        postRepository.create(post2);
        var owner3 = Owner.builder()
                .name("Marina")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine3 = engineRepository.findById(2).get();
        var body3 = bodyRepository.findById(3).get();
        var carBrand3 = carBrandRepository.findById(3).get();
        var color3 = carColorRepository.findById(2).get();
        var passport3 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner3))
                .build();
        var car3 = Car.builder()
                .model("Exceed")
                .mileage(1000)
                .build();
        car3.setBody(body3);
        car3.setEngine(engine3);
        car3.setPassport(passport3);
        car3.setCarColor(color3);
        car3.setBrand(carBrand3);
        var file3 = File.builder()
                .name("Test car 3")
                .path("/testpath_3")
                .build();
        var priceHistory3 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post3 = Post.builder()
                .price(0L)
                .title("Test sale Cherry")
                .created(currentLocalDateTime)
                .description("Test description")
                .sold(false)
                .build();
        post3.setFiles(Set.of(file3));
        post3.setPriceHistory(List.of(priceHistory3));
        post3.setCar(car3);
        postRepository.create(post3);
        var expected = List.of(post2, post3);
        var actual = postRepository.findAllLastDay();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenAddThreePostsThenGetTwoOfThemWithModelNameContainedConceptWord() {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var owner = Owner.builder()
                .name("Eddie")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine = engineRepository.findById(1).get();
        var body = bodyRepository.findById(1).get();
        var carBrand = carBrandRepository.findById(1).get();
        var color = carColorRepository.findById(1).get();
        var passport = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner))
                .build();
        var car = Car.builder()
                .model("Concept cqr1")
                .mileage(88000)
                .build();
        car.setBody(body);
        car.setEngine(engine);
        car.setPassport(passport);
        car.setCarColor(color);
        car.setBrand(carBrand);
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
                .title("Test sale Crown")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post.setFiles(Set.of(file));
        post.setPriceHistory(List.of(priceHistory));
        post.setCar(car);
        postRepository.create(post);
        var owner2 = Owner.builder()
                .name("Consta")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine2 = engineRepository.findById(2).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(1).get();
        var passport2 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner2))
                .build();
        var car2 = Car.builder()
                .model("Concept car")
                .mileage(88000)
                .build();
        car2.setBody(body2);
        car2.setEngine(engine2);
        car2.setPassport(passport2);
        car2.setCarColor(color2);
        car2.setBrand(carBrand2);
        var file2 = File.builder()
                .name("Test car 2")
                .path("/testpath_2")
                .build();
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post2 = Post.builder()
                .price(0L)
                .title("Test sale VW")
                .created(currentLocalDateTime)
                .description("Test description")
                .sold(false)
                .build();
        post2.setFiles(Set.of(file2));
        post2.setPriceHistory(List.of(priceHistory2));
        post2.setCar(car2);
        postRepository.create(post2);
        var owner3 = Owner.builder()
                .name("Marina")
                .start(currentLocalDateTime.minusDays(1))
                .end(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        var engine3 = engineRepository.findById(2).get();
        var body3 = bodyRepository.findById(3).get();
        var carBrand3 = carBrandRepository.findById(3).get();
        var color3 = carColorRepository.findById(2).get();
        var passport3 = AutoPassport.builder()
                .original(true)
                .owners(Set.of(owner3))
                .build();
        var car3 = Car.builder()
                .model("Exceed")
                .mileage(1000)
                .build();
        car3.setBody(body3);
        car3.setEngine(engine3);
        car3.setPassport(passport3);
        car3.setCarColor(color3);
        car3.setBrand(carBrand3);
        var file3 = File.builder()
                .name("Test car 3")
                .path("/testpath_3")
                .build();
        var priceHistory3 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var post3 = Post.builder()
                .price(0L)
                .title("Test sale Cherry")
                .created(currentLocalDateTime)
                .description("Test description")
                .sold(false)
                .build();
        post3.setFiles(Set.of(file3));
        post3.setPriceHistory(List.of(priceHistory3));
        post3.setCar(car3);
        postRepository.create(post3);
        var expected = List.of(post, post2);
        var actual = postRepository.findAllByName("Concept");
        assertThat(actual).isEqualTo(expected);
    }
}