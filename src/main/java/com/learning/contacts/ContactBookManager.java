package com.learning.contacts;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Core Service demonstrating Exception Handling and File IO:
 * - try-with-resources pattern
 * - BufferedReader and BufferedWriter
 * - Custom Checked Exceptions
 * - In-memory collection syncing with physical CSV storage
 */
public class ContactBookManager {
    private final Map<String, Contact> contactsMap = new LinkedHashMap<>();
    private final String storageFilePath;

    public ContactBookManager(String storageFilePath) {
        this.storageFilePath = storageFilePath;
    }

    public synchronized void addContact(Contact contact) throws DuplicateContactException, InvalidContactException {
        Contact.validate(contact.getFullName(), contact.getPhoneNumber(), contact.getEmailAddress());

        for (Contact existing : contactsMap.values()) {
            if (existing.getEmailAddress().equalsIgnoreCase(contact.getEmailAddress())) {
                throw new DuplicateContactException("Email", contact.getEmailAddress());
            }
            if (existing.getPhoneNumber().equals(contact.getPhoneNumber())) {
                throw new DuplicateContactException("Phone", contact.getPhoneNumber());
            }
        }

        contactsMap.put(contact.getId(), contact);
    }

    public Contact getContactById(String id) throws ContactNotFoundException {
        Contact contact = contactsMap.get(id);
        if (contact == null) {
            throw new ContactNotFoundException(id);
        }
        return contact;
    }

    public synchronized void deleteContact(String id) throws ContactNotFoundException {
        if (!contactsMap.containsKey(id)) {
            throw new ContactNotFoundException(id);
        }
        contactsMap.remove(id);
    }

    public List<Contact> getAllContacts() {
        return new ArrayList<>(contactsMap.values());
    }

    public List<Contact> searchByName(String query) {
        List<Contact> results = new ArrayList<>();
        for (Contact c : contactsMap.values()) {
            if (c.getFullName().toLowerCase().contains(query.toLowerCase())) {
                results.add(c);
            }
        }
        return results;
    }

    /**
     * File I/O: Writes all contacts to CSV using Try-With-Resources.
     */
    public synchronized void saveToFile() throws FilePersistenceException {
        File file = new File(storageFilePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,fullName,phoneNumber,emailAddress,category");
            writer.newLine();

            for (Contact contact : contactsMap.values()) {
                writer.write(contact.toCsvLine());
                writer.newLine();
            }
            System.out.printf(">> [File I/O Success] Saved %d contacts to: %s\n", contactsMap.size(), storageFilePath);
        } catch (IOException e) {
            throw new FilePersistenceException("Failed to persist contacts to file: " + storageFilePath, e);
        }
    }

    /**
     * File I/O: Loads contacts from CSV using Try-With-Resources and BufferedReader.
     */
    public synchronized void loadFromFile() throws FilePersistenceException {
        Path path = Paths.get(storageFilePath);
        if (!Files.exists(path)) {
            System.out.println(">> No existing storage file found. Starting with empty address book.");
            return;
        }

        contactsMap.clear();
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(storageFilePath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (line.trim().isEmpty()) continue;
                try {
                    Contact contact = Contact.fromCsvLine(line);
                    contactsMap.put(contact.getId(), contact);
                } catch (InvalidContactException ice) {
                    System.err.printf(">> Warning: Skipping corrupt line #%d: %s (%s)\n", lineCount, line, ice.getMessage());
                }
            }
            System.out.printf(">> [File I/O Success] Loaded %d contacts from file.\n", contactsMap.size());
        } catch (IOException e) {
            throw new FilePersistenceException("Failed reading contacts from: " + storageFilePath, e);
        }
    }
}
