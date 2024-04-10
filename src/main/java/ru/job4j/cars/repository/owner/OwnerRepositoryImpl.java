package ru.job4j.cars.repository.owner;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.exception.RepositoryException;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Constantine on 05.03.2024
 */
@AllArgsConstructor
@Repository
public class OwnerRepositoryImpl implements OwnerRepository {

    private final CrudRepository crudRepository;

    /**
     * Найти всех владельцев.
     *
     * Как найти всех владельцев определенной машины?
     *
     * @return список владельцев машин
     */
    @Override
    public Collection<Owner> findAll() {
        String hql1 = """
                    SELECT DISTINCT owner
                    FROM Owner owner
                    JOIN FETCH owner.historyOwners
                    """;
        String hql = """
                    SELECT DISTINCT owner
                    FROM Owner owner
                    """;
        return crudRepository.query(hql, Owner.class);
    }

    /**
     * Сохранить владельца машины.
     *
     * @param owner владелец машины
     */
    @Override
    public Owner create(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    /**
     * Обновить владельца.
     */
    @Override
    public void updateOwner(Owner owner) {
        crudRepository.run(session -> session.update(owner));
    }

    @Override
    public Optional<Owner> findById(int id) throws RepositoryException {
        var ownerOptional = crudRepository.optional(
                "FROM Owner owner WHERE owner.id = :fId",
                Owner.class,
                Map.of("fId", id)
        );
        if (ownerOptional.isEmpty()) {
            throw new RepositoryException("Repository exception: cant find owner with ID = ".concat(String.valueOf(id)));
        }
        return ownerOptional;
    }
}
