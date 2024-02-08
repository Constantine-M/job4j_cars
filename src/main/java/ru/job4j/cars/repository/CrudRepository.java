package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Данный класс описывает шаблон проектирования
 * Command - его цель: инкапсулировать запрос в объект.
 *
 * То есть общее поведение CRUD-методов
 * вынесено в данный класс и выполняется
 * одной абстрактной командой.
 *
 * Метод принимает некую "команду", открывает сессию,
 * начинает транзакцию и выполняет эту команду.
 * Команда принимается в виде объекта функционального
 * интерфейса, благодаря чему достигается
 * абстрактность операции.
 *
 * @author Constantine on 05.02.2024
 */
@AllArgsConstructor
public class CrudRepository {

    private final SessionFactory sessionFactory;

    public void run(Consumer<Session> command) {
        tx(session -> {
                    command.accept(session);
                    return null;
                }
        );
    }

    /**
     * Метод принимает запрос и карту,
     * в которой ключ - это имя аттрибута,
     * а значения - это то, что передаем в
     * качестве аттрибута. Благодаря Map мы
     * можем подставить какие угодно
     * значения в запрос.
     *
     * Метод ничего не возвращает. Подходит
     * для операции удаления например.
     *
     * @param query запрос
     * @param args параметры запроса
     */
    public void run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            var sq = session
                    .createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            sq.executeUpdate();
        };
        run(command);
    }

    /**
     * Метод принимает запрос, класс, к
     * которому мы будем приводить результат
     * запроса, и карту для подстановка параметров
     * в зпрос.
     *
     * Метод подходит для операции нахождения
     * требуемого объекта - например, найти
     * по ID.
     *
     * @param query запрос
     * @param cl результирующий класс
     * @param args параметры запроса
     * @return {@link Optional}
     */
    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sq = session
                    .createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.uniqueResultOptional();
        };
        return tx(command);
    }

    /**
     * Метод принимает запрос и класс, к
     * которому мы будем приводить результат
     * запроса.
     *
     * Метод подходит, когда результатом
     * поискового запроса является список.
     *
     * @param query запрос
     * @param cl результирующий класс
     * @return список объектов T
     */
    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session -> session
                .createQuery(query, cl)
                .list();
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sq = session
                    .createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.list();
        };
        return tx(command);
    }

    /**
     * Данный метод является главным.
     * Именно он выполняет ту самую абстрактную операцию
     */
    public <T> T tx(Function<Session, T> command) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}
