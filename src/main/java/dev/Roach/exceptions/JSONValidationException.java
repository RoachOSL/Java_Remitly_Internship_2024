package dev.Roach.exceptions;

public class JSONValidationException extends Exception {

    public JSONValidationException(String message) {
        super(message);
    }

    public JSONValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
