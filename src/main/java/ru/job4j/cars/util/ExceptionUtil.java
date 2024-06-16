package ru.job4j.cars.util;

/**
 * @author Constantine on 11.05.2024
 */
public class ExceptionUtil {

    /**
     * Метод ищет корневую причину
     * выхода исключения.
     *
     * Вызывается рекурсивно.
     *
     * @param e исключение, которое выпало
     *          во время работы метода.
     * @return исключение, которое
     * было первопричиной.
     */
    public static Throwable getRootCause(Throwable e) {
        if (e.getCause() == null) {
            return e;
        }
        return getRootCause(e.getCause());
    }
}
