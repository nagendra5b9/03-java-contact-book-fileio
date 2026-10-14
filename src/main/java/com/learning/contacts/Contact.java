package com.learning.contacts;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Model representing an address book contact.
 */
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_REGEX = Pattern.compile("^\\+?[0-9\\s-]{7,15}$");

    private String id;
    private String fullName;
    private String phoneNumber;
    private String emailAddress;
    private String category; // e.g. Work, Personal, Family

    public Contact(String id, String fullName, String phoneNumber, String emailAddress, String category) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.category = category != null ? category : "General";
    }

    public static void validate(String name, String phone, String email) throws InvalidContactException {
        if (name == null || name.trim().length() < 2) {
            throw new InvalidContactException("Contact full name must be at least 2 characters long.");
        }
        if (phone == null || !PHONE_REGEX.matcher(phone.trim()).matches()) {
            throw new InvalidContactException("Invalid phone number format. Must contain 7-15 digits: " + phone);
        }
        if (email == null || !EMAIL_REGEX.matcher(email.trim()).matches()) {
            throw new InvalidContactException("Invalid email format: " + email);
        }
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailAddress() { return emailAddress; }
    public String getCategory() { return category; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public void setCategory(String category) { this.category = category; }

    public String toCsvLine() {
        return String.format("%s,%s,%s,%s,%s", escapeCsv(id), escapeCsv(fullName), escapeCsv(phoneNumber), escapeCsv(emailAddress), escapeCsv(category));
    }

    public static Contact fromCsvLine(String line) throws InvalidContactException {
        String[] parts = line.split(",", -1);
        if (parts.length < 5) {
            throw new InvalidContactException("Malformed CSV line: expected 5 fields, got " + parts.length);
        }
        return new Contact(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
    }

    private static String escapeCsv(String str) {
        return str != null ? str.replace(",", " ") : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return Objects.equals(emailAddress.toLowerCase(), contact.emailAddress.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("Contact[ID=%s, Name='%s', Phone='%s', Email='%s', Category='%s']",
                id, fullName, phoneNumber, emailAddress, category);
    }
}
