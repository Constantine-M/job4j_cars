package ru.job4j.cars.repository.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * В тестовом классе используется аннотация
 * {@link TestExecutionListeners}, которая
 * позволяет управлять выполнением тестов и
 * обрабатывать события до и после их запуска.
 *
 * Но, чтобы передать собственный listener,
 * требуется также передать дефолтную реализацию
 * {@link DependencyInjectionTestExecutionListener},
 * которая обеспечивает внедрение зависимости.
 * "Hence, we’ve added DependencyInjectionTestExecutionListener
 * explicitly so that we can use auto wiring in
 * our test class."
 *
 * @author Constantine on 27.03.2024
 */
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenSaveUserThenGetTheSame() throws RepositoryException {
        var user = User.builder()
                .username("Nobody")
                .phoneNumber("123456789")
                .login("test")
                .password("123")
                .userZone("uzerZone")
                .build();
        userRepository.create(user);
        assertThat(userRepository.findById(1).get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void whenFindUserById2ThenGetUserConsta() throws RepositoryException {
        var userOlga = User.builder()
                .username("Olga")
                .phoneNumber("123456789")
                .login("boss")
                .password("1")
                .userZone("uzerZone")
                .build();
        var userConsta = User.builder()
                .username("Consta")
                .phoneNumber("987654321")
                .login("BossBossa")
                .password("2")
                .userZone("uzerZone")
                .build();
        userRepository.create(userOlga);
        userRepository.create(userConsta);
        assertThat(userRepository.findById(2).get())
                .usingRecursiveComparison()
                .isEqualTo(userConsta);
    }

    @Test
    void whenUpdateUserWithLoginConstaThenGetUserWithLoginConstaMezenin() throws RepositoryException {
        var userConsta = User.builder()
                .username("Consta")
                .phoneNumber("987654321")
                .login("BossBossa")
                .password("2")
                .userZone("uzerZone")
                .build();
        userRepository.create(userConsta);
        userConsta.setLogin("Consta Mezenin");
        userRepository.update(userConsta);
        assertThat(userRepository.findById(1).get().getLogin())
                .isEqualTo("Consta Mezenin");
    }

    @Test
    void whenFindAllThenGetListOfUsersWithConstaAndOlga() throws RepositoryException {
        var userOlga = User.builder()
                .username("Olga")
                .phoneNumber("123456789")
                .login("boss")
                .password("1")
                .userZone("uzerZone")
                .build();
        var userConsta = User.builder()
                .username("Consta")
                .phoneNumber("987654321")
                .login("BossBossa")
                .password("2")
                .userZone("uzerZone")
                .build();
        userRepository.create(userOlga);
        userRepository.create(userConsta);
        var actual = userRepository.findAllOrderById();
        var expected = List.of(userOlga, userConsta);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenDeleteUser1ThenGetOnlyUser2() throws RepositoryException {
        var user1 = User.builder()
                .username("Olga")
                .phoneNumber("123456789")
                .login("boss")
                .password("1")
                .userZone("uzerZone")
                .build();
        var user2 = User.builder()
                .username("Consta")
                .phoneNumber("987654321")
                .login("BossBossa")
                .password("2")
                .userZone("uzerZone")
                .build();
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.delete(user1.getId());
        assertThat(userRepository.findAllOrderById()).isEqualTo(List.of(user2));
    }

    @Test
    void whenSaveTwoIdenticalUsersThenGetRepositoryException() throws RepositoryException {
        var user = User.builder()
                .username("Olga")
                .phoneNumber("123456789")
                .login("boss")
                .password("1")
                .userZone("uzerZone")
                .build();
        var userGemini = User.builder()
                .username("Olga")
                .phoneNumber("123456789")
                .login("boss")
                .password("1")
                .userZone("uzerZone")
                .build();
        userRepository.create(user);
        var errorMessage = "Repository exception: user cant save.";
        assertThatThrownBy(() -> userRepository.create(userGemini))
                .isInstanceOf(RepositoryException.class)
                .hasMessage(errorMessage);
    }

    /**
     * В данном тесте важно не получать объект
     * {@link User}, т.к. мы проверяем метод, а метод
     * возвращает {@link Optional}.
     */
    @Test
    void whenCannotFindByIdThenGetRepositoryException() throws RepositoryException {
        var testUserId = 1;
        var errMessage = "Repository exception: cant find user with ID = ".concat(String.valueOf(testUserId));
        assertThatThrownBy(() -> userRepository.findById(testUserId))
                .isInstanceOf(RepositoryException.class)
                .hasMessage(errMessage);
    }

    @Test
    void whenFindByLikeOnstaThenGetListOf2UsersWithConstaAndConstantine() throws RepositoryException {
        var user1 = User.builder()
                .username("Consta")
                .phoneNumber("987654321")
                .login("nagibator2016")
                .password("2")
                .userZone("uzerZone")
                .build();
        var user2 = User.builder()
                .username("Constantine")
                .phoneNumber("987654321")
                .login("xXxNagibatorxXx2021")
                .password("3")
                .userZone("uzerZone")
                .build();
        userRepository.create(user1);
        userRepository.create(user2);
        var expected = List.of(user1, user2);
        assertThat(userRepository.findByLikeLogin("ibator")).isEqualTo(expected);
    }

    @Test
    void whenFindByLoginThenGetConsta() throws RepositoryException {
        var user = User.builder()
                .username("Consta")
                .phoneNumber("987654321")
                .login("Boss")
                .password("2")
                .userZone("uzerZone")
                .build();
        userRepository.create(user);
        assertThat(userRepository.findByLogin("Boss").get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void whenCannotFindUserByLoginThenGetAnEmptyOptional() throws RepositoryException {
        assertThat(userRepository.findByLogin("test")).isEqualTo(Optional.empty());
    }
}