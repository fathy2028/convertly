package com.convertly.demo.exception;

/**
 * Custom exception thrown when an invalid unit is provided for conversion.
 */
public class InvalidUnitException extends RuntimeException {

    public InvalidUnitException(String message) {
        super(message);
    }

    public InvalidUnitException(String message, Throwable cause) {
        super(message, cause);
    }
}
