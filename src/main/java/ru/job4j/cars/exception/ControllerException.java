package ru.job4j.cars.exception;

/**
 * @author Constantine on 10.05.2024
 */
public class ControllerException extends Exception {

    public ControllerException(String message, Exception base) {
        super(message, base);
    }

    public ControllerException(String message) {
        super(message);
    }
}
