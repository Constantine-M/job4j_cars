package ru.job4j.cars.exception;

/**
 * Данный класс описывает исключения на уровне
 * персистенции.
 *
 * То есть, когда требуется получить
 * более полную информацию об ошибке, мы
 * ловим конкретные исключения, описываем их
 * и прикладываем stack trace.
 *
 * Таким образом, будет гораздо легче понять,
 * на каком этапе (в каком слое) возникла
 * проблема.
 *
 * @author Constantine on 09.03.2024
 */
public class RepositoryException extends Exception {

    public RepositoryException(String message, Exception base) {
        super(message, base);
    }

    /**
     * Перегрузил конструктор, т.к. имеются случаи,
     * когда результат вычисления оборачивается в
     * {@link java.util.Optional}.
     *
     * @param message сообщение, которое будет записано в лог.
     */
    public RepositoryException(String message) {
        super(message);
    }
}
