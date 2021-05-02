package it.ovi.demo.errors;

/**
 * Thrown when the request is invalid.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
