package ru.job4j.cars.repository.owner;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Collection;

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
        String hql = """
                    SELECT DISTINCT owner
                    FROM Owner owner
                    JOIN FETCH owner.cars
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
}
