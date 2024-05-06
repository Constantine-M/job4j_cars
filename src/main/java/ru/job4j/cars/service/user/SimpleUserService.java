package ru.job4j.cars.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.exception.SimpleServiceException;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Constantine on 29.04.2024
 */
@Service
@Slf4j
@AllArgsConstructor
public class SimpleUserService implements UserService {

    private UserRepository userRepository;

    @Override
    public User createUser(User user) throws SimpleServiceException {
        try {
            userRepository.create(user);
        } catch (RepositoryException e) {
            log.error("Could not create user! Exception logged: {}", e.getMessage());
            throw new SimpleServiceException(Arrays.toString(e.getStackTrace()));
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.delete(id);
    }

    @Override
    public List<User> findAllOrderById() {
        return userRepository.findAllOrderById();
    }

    @Override
    public Optional<User> findById(int userId) throws SimpleServiceException {
        try {
            return userRepository.findById(userId);
        } catch (RepositoryException e) {
            log.error("Could not find user by id! Exception logged: {}", Arrays.toString(e.getStackTrace()));
            throw new SimpleServiceException(Arrays.toString(e.getStackTrace()), e);
        }
    }

    @Override
    public List<User> findByLikeLogin(String key) throws SimpleServiceException {
        try {
            return userRepository.findByLikeLogin(key);
        } catch (RepositoryException e) {
            log.error("Could not find user by login (partial match)! Exception logged: {}", Arrays.toString(e.getStackTrace()));
            throw new SimpleServiceException(Arrays.toString(e.getStackTrace()), e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) throws SimpleServiceException {
        try {
            return userRepository.findByLogin(login);
        } catch (RepositoryException e) {
            log.error("Could not find user by login! Exception logged: {}", Arrays.toString(e.getStackTrace()));
            throw new SimpleServiceException(Arrays.toString(e.getStackTrace()), e);
        }
    }
}
