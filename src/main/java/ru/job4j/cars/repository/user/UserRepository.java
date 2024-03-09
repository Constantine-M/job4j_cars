package ru.job4j.cars.repository.user;

import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Constantine on 02.03.2024
 */
public interface UserRepository {

    User create(User user) throws RepositoryException;

    void update(User user);

    void delete(int userId);

    List<User> findAllOrderById();

    Optional<User> findById(int userId) throws RepositoryException;

    List<User> findByLikeLogin(String key) throws RepositoryException;

    Optional<User> findByLogin(String login) throws RepositoryException;
}
