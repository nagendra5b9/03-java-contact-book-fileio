package com.learning.contacts;

/**
 * Base custom checked exception for Contact Book operations.
 */
public class ContactException extends Exception {
    public ContactException(String message) {
        super(message);
    }
    public ContactException(String message, Throwable cause) {
        super(message, cause);
    }
}
