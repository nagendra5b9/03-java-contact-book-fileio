package com.learning.contacts;

/**
 * Thrown when attempting to query or mutate a non-existent contact.
 */
public class ContactNotFoundException extends ContactException {
    public ContactNotFoundException(String identifier) {
        super("Contact not found matching identifier: " + identifier);
    }
}
