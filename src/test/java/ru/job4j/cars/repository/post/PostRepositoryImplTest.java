package ru.job4j.cars.repository.post;

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
import ru.job4j.cars.repository.color.CarColorRepository;
import ru.job4j.cars.repository.engine.EngineRepository;
import ru.job4j.cars.repository.gearbox.GearBoxRepository;
import ru.job4j.cars.repository.user.UserRepository;

import java.time.LocalDate;
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

    @Autowired
    private GearBoxRepository gearBoxRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenCreatePostThenGetTheSame() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.MINUTES);
        var currentLocalDate = LocalDate.now();
        var file1 = File.builder()
                .name("Test car")
                .path("/testpath")
                .build();
        var priceHistory1 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user1 = User.builder()
                .userZone("testZone")
                .username("user")
                .login("login")
                .phoneNumber("12345")
                .password("pass")
                .build();
        userRepository.create(user1);
        var engine1 = engineRepository.findById(1).get();
        var body1 = bodyRepository.findById(1).get();
        var carBrand1 = carBrandRepository.findById(1).get();
        var color1 = carColorRepository.findById(1).get();
        var gearBox1 = gearBoxRepository.findById(1).get();
        var passport1 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car1 = Car.builder()
                .carYear(2020)
                .model("F150")
                .mileage(88000)
                .build();
        car1.setBody(body1);
        car1.setEngine(engine1);
        car1.setPassport(passport1);
        car1.setCarColor(color1);
        car1.setBrand(carBrand1);
        car1.setGearBox(gearBox1);
        var post1 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post1.setCar(car1);
        post1.setUser(user1);
        post1.setFiles(Set.of(file1));
        post1.setPriceHistory(List.of(priceHistory1));
        postRepository.create(post1);
        var actualPost = postRepository.findById(1).get();
        assertThat(actualPost)
                .usingRecursiveComparison()
                .isEqualTo(post1);
    }

    @Test
    void whenUpdateCarFromToyotaToVolkswagenInPostThenGetPostWithVolkswagen() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.MINUTES);
        var currentLocalDate = LocalDate.now();
        var file1 = File.builder()
                .name("Test car")
                .path("/testpath")
                .build();
        var priceHistory1 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user1 = User.builder()
                .userZone("testZone")
                .username("user")
                .login("login")
                .phoneNumber("12345")
                .password("pass")
                .build();
        userRepository.create(user1);
        var engine1 = engineRepository.findById(1).get();
        var body1 = bodyRepository.findById(1).get();
        var carBrand1 = carBrandRepository.findById(1).get();
        var color1 = carColorRepository.findById(1).get();
        var gearBox1 = gearBoxRepository.findById(1).get();
        var passport1 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car1 = Car.builder()
                .carYear(2020)
                .model("F150")
                .mileage(88000)
                .build();
        car1.setBody(body1);
        car1.setEngine(engine1);
        car1.setPassport(passport1);
        car1.setCarColor(color1);
        car1.setBrand(carBrand1);
        car1.setGearBox(gearBox1);
        var post1 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post1.setCar(car1);
        post1.setUser(user1);
        post1.setFiles(Set.of(file1));
        post1.setPriceHistory(List.of(priceHistory1));
        postRepository.create(post1);
        var engine2 = engineRepository.findById(1).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(3).get();
        var gearBox2 = gearBoxRepository.findById(2).get();
        var passport2 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
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
        newCar.setGearBox(gearBox2);
        post1.setCar(newCar);
        postRepository.updatePost(post1);
        assertThat(postRepository.findById(1).get().getCar())
                .usingRecursiveComparison()
                .isEqualTo(newCar);
    }

    @Test
    void whenCannotFindThePostWithIdEqualsTo3ThenGetAnEmptyOptional() {
        assertThat(postRepository.findById(3)).isEqualTo(Optional.empty());
    }

    @Test
    void whenFindAllThenGetListOfTwoPostsWithToyotaAndVW() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.MINUTES);
        var currentLocalDate = LocalDate.now();
        var file1 = File.builder()
                .name("Test car")
                .path("/testpath")
                .build();
        var priceHistory1 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user1 = User.builder()
                .userZone("testZone")
                .username("user")
                .login("login")
                .phoneNumber("12345")
                .password("pass")
                .build();
        userRepository.create(user1);
        var engine1 = engineRepository.findById(1).get();
        var body1 = bodyRepository.findById(1).get();
        var carBrand1 = carBrandRepository.findById(1).get();
        var color1 = carColorRepository.findById(1).get();
        var gearBox1 = gearBoxRepository.findById(1).get();
        var passport1 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car1 = Car.builder()
                .carYear(2020)
                .model("Camry")
                .mileage(88000)
                .build();
        car1.setBody(body1);
        car1.setEngine(engine1);
        car1.setPassport(passport1);
        car1.setCarColor(color1);
        car1.setBrand(carBrand1);
        car1.setGearBox(gearBox1);
        var post1 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post1.setCar(car1);
        post1.setUser(user1);
        post1.setFiles(Set.of(file1));
        post1.setPriceHistory(List.of(priceHistory1));
        postRepository.create(post1);

        var file2 = File.builder()
                .name("Test car")
                .path("/testpath2")
                .build();
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user2 = User.builder()
                .userZone("testZone2")
                .username("user2")
                .login("login2")
                .phoneNumber("123456")
                .password("pass2")
                .build();
        userRepository.create(user2);
        var engine2 = engineRepository.findById(1).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(1).get();
        var gearBox2 = gearBoxRepository.findById(1).get();
        var passport2 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car2 = Car.builder()
                .carYear(2020)
                .model("Jetta")
                .mileage(88000)
                .build();
        car2.setBody(body2);
        car2.setEngine(engine2);
        car2.setPassport(passport2);
        car2.setCarColor(color2);
        car2.setBrand(carBrand2);
        car2.setGearBox(gearBox2);
        var post2 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post2.setCar(car2);
        post2.setUser(user2);
        post2.setFiles(Set.of(file2));
        post2.setPriceHistory(List.of(priceHistory2));
        postRepository.create(post2);
        var expected = List.of(post1, post2);
        assertThat(postRepository.findAll()).isEqualTo(expected);
    }

    @Test
    void whenAddTwoPostsThenGetOnlyOneWithPhoto() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.MINUTES);
        var currentLocalDate = LocalDate.now();
        var priceHistory1 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user1 = User.builder()
                .userZone("testZone")
                .username("user")
                .login("login")
                .phoneNumber("12345")
                .password("pass")
                .build();
        userRepository.create(user1);
        var engine1 = engineRepository.findById(1).get();
        var body1 = bodyRepository.findById(1).get();
        var carBrand1 = carBrandRepository.findById(1).get();
        var color1 = carColorRepository.findById(1).get();
        var gearBox1 = gearBoxRepository.findById(1).get();
        var passport1 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car1 = Car.builder()
                .carYear(2020)
                .model("Camry")
                .mileage(88000)
                .build();
        car1.setBody(body1);
        car1.setEngine(engine1);
        car1.setPassport(passport1);
        car1.setCarColor(color1);
        car1.setBrand(carBrand1);
        car1.setGearBox(gearBox1);
        var post1 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post1.setCar(car1);
        post1.setUser(user1);
        post1.setFiles(emptySet());
        post1.setPriceHistory(List.of(priceHistory1));
        postRepository.create(post1);

        var file2 = File.builder()
                .name("Test car")
                .path("/testpath2")
                .build();
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user2 = User.builder()
                .userZone("testZone2")
                .username("user2")
                .login("login2")
                .phoneNumber("123456")
                .password("pass2")
                .build();
        userRepository.create(user2);
        var engine2 = engineRepository.findById(1).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(1).get();
        var gearBox2 = gearBoxRepository.findById(1).get();
        var passport2 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car2 = Car.builder()
                .carYear(2020)
                .model("Jetta")
                .mileage(88000)
                .build();
        car2.setBody(body2);
        car2.setEngine(engine2);
        car2.setPassport(passport2);
        car2.setCarColor(color2);
        car2.setBrand(carBrand2);
        car2.setGearBox(gearBox2);
        var post2 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post2.setCar(car2);
        post2.setUser(user2);
        post2.setFiles(Set.of(file2));
        post2.setPriceHistory(List.of(priceHistory2));
        postRepository.create(post2);
        var expected = List.of(post2);
        assertThat(postRepository.findAllWithPhoto()).isEqualTo(expected);
    }

    @Test
    void whenAddThreePostsThenGet2OfThemCreatedByLastDay() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.MINUTES);
        var currentLocalDate = LocalDate.now();
        var priceHistory1 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user1 = User.builder()
                .userZone("testZone")
                .username("user")
                .login("login")
                .phoneNumber("12345")
                .password("pass")
                .build();
        userRepository.create(user1);
        var engine1 = engineRepository.findById(1).get();
        var body1 = bodyRepository.findById(1).get();
        var carBrand1 = carBrandRepository.findById(3).get();
        var color1 = carColorRepository.findById(1).get();
        var gearBox1 = gearBoxRepository.findById(1).get();
        var passport1 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car1 = Car.builder()
                .carYear(2020)
                .model("Tiggo 4")
                .mileage(88000)
                .build();
        car1.setBody(body1);
        car1.setEngine(engine1);
        car1.setPassport(passport1);
        car1.setCarColor(color1);
        car1.setBrand(carBrand1);
        car1.setGearBox(gearBox1);
        var post1 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post1.setCar(car1);
        post1.setUser(user1);
        post1.setFiles(emptySet());
        post1.setPriceHistory(List.of(priceHistory1));
        postRepository.create(post1);
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user2 = User.builder()
                .userZone("testZone2")
                .username("user2")
                .login("login2")
                .phoneNumber("123456")
                .password("pass2")
                .build();
        userRepository.create(user2);
        var engine2 = engineRepository.findById(1).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(1).get();
        var gearBox2 = gearBoxRepository.findById(1).get();
        var passport2 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car2 = Car.builder()
                .carYear(2020)
                .model("Jetta")
                .mileage(88000)
                .build();
        car2.setBody(body2);
        car2.setEngine(engine2);
        car2.setPassport(passport2);
        car2.setCarColor(color2);
        car2.setBrand(carBrand2);
        car2.setGearBox(gearBox2);
        var post2 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime)
                .description("Test description")
                .sold(false)
                .build();
        post2.setCar(car2);
        post2.setUser(user2);
        post2.setFiles(emptySet());
        post2.setPriceHistory(List.of(priceHistory2));
        postRepository.create(post2);
        var file3 = File.builder()
                .name("Test car")
                .path("/testpath3")
                .build();
        var priceHistory3 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user3 = User.builder()
                .userZone("testZone3")
                .username("user3")
                .login("login3")
                .phoneNumber("1234567")
                .password("pass3")
                .build();
        userRepository.create(user3);
        var engine3 = engineRepository.findById(1).get();
        var body3 = bodyRepository.findById(1).get();
        var carBrand3 = carBrandRepository.findById(3).get();
        var color3 = carColorRepository.findById(1).get();
        var gearBox3 = gearBoxRepository.findById(1).get();
        var passport3 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car3 = Car.builder()
                .carYear(2020)
                .model("Tiggo 7")
                .mileage(567645)
                .build();
        car3.setBody(body3);
        car3.setEngine(engine3);
        car3.setPassport(passport3);
        car3.setCarColor(color3);
        car3.setBrand(carBrand3);
        car3.setGearBox(gearBox3);
        var post3 = Post.builder()
                .price(500L)
                .summary("Test title3")
                .created(currentLocalDateTime)
                .description("Test description3")
                .sold(false)
                .build();
        post3.setCar(car3);
        post3.setUser(user3);
        post3.setFiles(Set.of(file3));
        post3.setPriceHistory(List.of(priceHistory3));
        postRepository.create(post3);
        var expected = List.of(post2, post3);
        assertThat(postRepository.findAllLastDay()).isEqualTo(expected);
    }

    @Test
    void whenAddThreePostsThenGetTwoOfThemWithBrandNameContainedCherry() throws RepositoryException {
        var currentLocalDateTime = LocalDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        var currentLocalDate = LocalDate.now();
        var priceHistory1 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user1 = User.builder()
                .userZone("testZone")
                .username("user")
                .login("login")
                .phoneNumber("12345")
                .password("pass")
                .build();
        userRepository.create(user1);
        var engine1 = engineRepository.findById(1).get();
        var body1 = bodyRepository.findById(1).get();
        var carBrand1 = carBrandRepository.findById(3).get();
        var color1 = carColorRepository.findById(1).get();
        var gearBox1 = gearBoxRepository.findById(1).get();
        var passport1 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car1 = Car.builder()
                .carYear(2020)
                .model("Tiggo 4")
                .mileage(88000)
                .build();
        car1.setBody(body1);
        car1.setEngine(engine1);
        car1.setPassport(passport1);
        car1.setCarColor(color1);
        car1.setBrand(carBrand1);
        car1.setGearBox(gearBox1);
        var post1 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post1.setCar(car1);
        post1.setUser(user1);
        post1.setFiles(emptySet());
        post1.setPriceHistory(List.of(priceHistory1));
        postRepository.create(post1);
        var priceHistory2 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user2 = User.builder()
                .userZone("testZone2")
                .username("user2")
                .login("login2")
                .phoneNumber("123456")
                .password("pass2")
                .build();
        userRepository.create(user2);
        var engine2 = engineRepository.findById(1).get();
        var body2 = bodyRepository.findById(1).get();
        var carBrand2 = carBrandRepository.findById(2).get();
        var color2 = carColorRepository.findById(1).get();
        var gearBox2 = gearBoxRepository.findById(1).get();
        var passport2 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car2 = Car.builder()
                .carYear(2020)
                .model("Jetta")
                .mileage(88000)
                .build();
        car2.setBody(body2);
        car2.setEngine(engine2);
        car2.setPassport(passport2);
        car2.setCarColor(color2);
        car2.setBrand(carBrand2);
        car2.setGearBox(gearBox2);
        var post2 = Post.builder()
                .price(500L)
                .summary("Test title")
                .created(currentLocalDateTime.minusDays(10))
                .description("Test description")
                .sold(false)
                .build();
        post2.setCar(car2);
        post2.setUser(user2);
        post2.setFiles(emptySet());
        post2.setPriceHistory(List.of(priceHistory2));
        postRepository.create(post2);
        var file3 = File.builder()
                .name("Test car")
                .path("/testpath3")
                .build();
        var priceHistory3 = PriceHistory.builder()
                .created(currentLocalDateTime)
                .before(1L)
                .after(2L)
                .build();
        var user3 = User.builder()
                .userZone("testZone3")
                .username("user3")
                .login("login3")
                .phoneNumber("1234567")
                .password("pass3")
                .build();
        userRepository.create(user3);
        var engine3 = engineRepository.findById(1).get();
        var body3 = bodyRepository.findById(1).get();
        var carBrand3 = carBrandRepository.findById(3).get();
        var color3 = carColorRepository.findById(1).get();
        var gearBox3 = gearBoxRepository.findById(1).get();
        var passport3 = AutoPassport.builder()
                .havingPassport("have passport")
                .boughtAt(currentLocalDate)
                .cleared(true)
                .build();
        var car3 = Car.builder()
                .carYear(2020)
                .model("Tiggo 7")
                .mileage(567645)
                .build();
        car3.setBody(body3);
        car3.setEngine(engine3);
        car3.setPassport(passport3);
        car3.setCarColor(color3);
        car3.setBrand(carBrand3);
        car3.setGearBox(gearBox3);
        var post3 = Post.builder()
                .price(500L)
                .summary("Test title3")
                .created(currentLocalDateTime.minusDays(13))
                .description("Test description3")
                .sold(false)
                .build();
        post3.setCar(car3);
        post3.setUser(user3);
        post3.setFiles(Set.of(file3));
        post3.setPriceHistory(List.of(priceHistory3));
        postRepository.create(post3);
        var expected = List.of(post1, post3);
        var actual = postRepository.findAllByName("Cherry");
        assertThat(actual).isEqualTo(expected);
    }
}