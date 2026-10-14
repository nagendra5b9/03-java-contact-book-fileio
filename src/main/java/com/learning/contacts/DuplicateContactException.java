package com.learning.contacts;

/**
 * Thrown when trying to insert a contact with an already registered email or phone.
 */
public class DuplicateContactException extends ContactException {
    public DuplicateContactException(String field, String value) {
        super(String.format("Duplicate contact detected! A contact with %s '%s' already exists.", field, value));
    }
}
