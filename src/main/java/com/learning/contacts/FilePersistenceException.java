package com.learning.contacts;

/**
 * Thrown when file IO encounters errors during reading or writing.
 */
public class FilePersistenceException extends ContactException {
    public FilePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
