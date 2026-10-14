package com.learning.contacts;

import java.util.UUID;

/**
 * Driver testing Exception Handling and File IO capabilities.
 */
public class ContactBookApplication {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   EXCEPTION HANDLING & FILE IO: CONTACT BOOK     ");
        System.out.println("==================================================");

        String sampleFilePath = "data/contacts.csv";
        ContactBookManager manager = new ContactBookManager(sampleFilePath);

        // 1. Adding valid contacts
        try {
            System.out.println("1. Adding initial contacts...");
            manager.addContact(new Contact("C-101", "Emma Watson", "+1-555-0192", "emma.w@example.com", "Work"));
            manager.addContact(new Contact("C-102", "Liam Neeson", "+1-555-0144", "liam.n@example.com", "Personal"));
            manager.addContact(new Contact("C-103", "Sophia Chen", "+1-555-0188", "sophia.c@example.com", "Family"));
            System.out.println(">> Added 3 contacts successfully.");
        } catch (ContactException e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        // 2. Exception Handling: Duplicate Email
        System.out.println("\n2. Testing DuplicateContactException...");
        try {
            manager.addContact(new Contact("C-104", "Emma Clone", "+1-555-9999", "emma.w@example.com", "Work"));
        } catch (DuplicateContactException e) {
            System.out.println(">> CAUGHT EXPECTED EXCEPTION: " + e.getMessage());
        } catch (ContactException e) {
            System.out.println("Caught general: " + e.getMessage());
        }

        // 3. Exception Handling: Invalid Email Format
        System.out.println("\n3. Testing InvalidContactException...");
        try {
            manager.addContact(new Contact("C-105", "Bad Email User", "+1-555-1234", "not-an-email", "Work"));
        } catch (InvalidContactException e) {
            System.out.println(">> CAUGHT EXPECTED EXCEPTION: " + e.getMessage());
        } catch (ContactException e) {
            System.out.println("Caught general: " + e.getMessage());
        }

        // 4. Exception Handling: ContactNotFoundException
        System.out.println("\n4. Testing ContactNotFoundException...");
        try {
            manager.getContactById("NON-EXISTENT-ID");
        } catch (ContactNotFoundException e) {
            System.out.println(">> CAUGHT EXPECTED EXCEPTION: " + e.getMessage());
        }

        // 5. File IO: Saving to CSV
        System.out.println("\n5. Persisting Contacts to CSV file...");
        try {
            manager.saveToFile();
        } catch (FilePersistenceException e) {
            System.err.println("File persistence failed: " + e.getMessage());
        }

        // 6. File IO: Reloading from CSV
        System.out.println("\n6. Simulating reload from storage file into new manager instance...");
        ContactBookManager reloadedManager = new ContactBookManager(sampleFilePath);
        try {
            reloadedManager.loadFromFile();
            System.out.println("All contacts loaded into memory:");
            for (Contact c : reloadedManager.getAllContacts()) {
                System.out.println(" - " + c);
            }
        } catch (FilePersistenceException e) {
            System.err.println("File loading failed: " + e.getMessage());
        }

        System.out.println("\nAll Exception Handling & File IO tests completed successfully!");
    }
}
