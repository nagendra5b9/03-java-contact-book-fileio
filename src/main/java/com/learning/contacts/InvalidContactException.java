package com.learning.contacts;

/**
 * Thrown when validation rules fail (format, empty name, etc).
 */
public class InvalidContactException extends ContactException {
    public InvalidContactException(String message) {
        super(message);
    }
}
