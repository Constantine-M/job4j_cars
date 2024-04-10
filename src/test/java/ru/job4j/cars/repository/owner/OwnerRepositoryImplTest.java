package ru.job4j.cars.repository.owner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.listener.CleanupH2DatabaseTestListener;
import ru.job4j.cars.model.Owner;


import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

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
 * @author Constantine on 31.03.2024
 */
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
class OwnerRepositoryImplTest {

    @Autowired
    private OwnerRepository ownerRepository;

    /**
     * Данный тест просто проверяет сохранение
     * владельца в БД. Здесь не учитываются
     * связи, т.к. сохранение связанных сущностей
     * происходит в отдельной сессии, что
     * влечет к ошибке после выполнения теста.
     */
    @Test
    void whenCreateOwnerThenGetTheSame() throws RepositoryException {
        var owner = Owner.builder()
                .name("Consta")
                .historyOwners(emptyList())
                .build();
        ownerRepository.create(owner);
        assertThat(ownerRepository.findById(1).get())
                .usingRecursiveComparison()
                .ignoringFields("historyOwners")
                .isEqualTo(owner);
    }

    @Test
    void whenFindOwnerByIdThenGetOwnerConsta() throws RepositoryException {
        var owner = Owner.builder()
                .name("Consta")
                .historyOwners(emptyList())
                .build();
        ownerRepository.create(owner);
        var actualOwner = ownerRepository.findById(1);
        assertThat(actualOwner.get().getName()).isEqualTo("Consta");
    }

    @Test
    void whenCannotFindOwnerByIdThenThrowRepositoryException() throws RepositoryException {
        assertThatThrownBy(() -> ownerRepository.findById(1))
                .isInstanceOf(RepositoryException.class)
                .hasMessage("Repository exception: cant find owner with ID = 1");
    }

    @Test
    void whenFindAllThenGetAllOwners() {
        var owner1 = Owner.builder()
                .name("owner 1")
                .historyOwners(emptyList())
                .build();
        ownerRepository.create(owner1);
        var owner2 = Owner.builder()
                .name("owner 2")
                .historyOwners(emptyList())
                .build();
        ownerRepository.create(owner2);
        var expected = List.of(owner1, owner2);
        var actual = ownerRepository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenUpdateOwnerNameFromConstaToJohnThenGetOwnerJohn() throws RepositoryException {
        var owner = Owner.builder()
                .name("Consta")
                .historyOwners(emptyList())
                .build();
        ownerRepository.create(owner);
        owner.setName("John");
        ownerRepository.updateOwner(owner);
        assertThat(ownerRepository.findById(1).get().getName()).isEqualTo("John");
    }
}