package ru.job4j.cars.repository.passport;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPassport;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Map;

/**
 * @author Constantine on 14.04.2024
 */
@AllArgsConstructor
@Repository
public class AutoPassportRepositoryImpl  implements AutoPassportRepository {

    private CrudRepository crudRepository;

    @Override
    public void save(AutoPassport autoPassport) {
        crudRepository.run(session -> session.save(autoPassport));
    }

    /**
     * найти паспорт транспортоного средства
     * по ID.
     *
     * Т.к. у нас ограничение в БД (NOT NULL),
     * то проверку на пустой Optional не делаю.
     * Паспорт всегда должен быть.
     *
     * @param id идентификатор
     * @return паспорт ТС
     */
    @Override
    public AutoPassport findById(int id) {
        String hql = """
                    FROM AutoPassport passport
                    WHERE passport.id = :fId
                    """;
        var passportOpt = crudRepository.optional(hql, AutoPassport.class,
                Map.of("fId", id));
        return passportOpt.get();
    }

    /**
     * Обновить данные в ПТС.
     */
    @Override
    public void update(AutoPassport autoPassport) {
        crudRepository.run(session -> session.update(autoPassport));
    }
}
