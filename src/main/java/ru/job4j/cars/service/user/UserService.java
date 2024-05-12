package ru.job4j.cars.service.user;

import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Constantine on 29.04.2024
 */
public interface UserService {

    User createUser(User user) throws SimpleServiceException;

    void updateUser(User user);

    void deleteUserById(int id);

    List<User> findAllOrderById();

    Optional<User> findById(int userId) throws SimpleServiceException;

    List<User> findByLikeLogin(String key) throws SimpleServiceException;

    Optional<User> findByLogin(String login) throws SimpleServiceException;

    Optional<User> findByLoginAndPassword(String login, String password) throws SimpleServiceException;
}
