package ru.job4j.cars.exception;

/**
 * @author Constantine on 29.04.2024
 */
public class SimpleServiceException extends Exception {

    public SimpleServiceException(String message, Exception base) {
        super(message, base);
    }

    public SimpleServiceException(String message) {
        super(message);
    }
}
