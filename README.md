# Project 3: Exception Handling & File I/O — Contact Book Management

## Overview
Demonstrates robust error handling and stream-based persistence in Java:
- **Custom Checked Exceptions Hierarchy**: `ContactException` extends `Exception`, subclassed by `ContactNotFoundException`, `DuplicateContactException`, `InvalidContactException`, and `FilePersistenceException`.
- **Try-With-Resources**: Auto-closable `BufferedReader` and `BufferedWriter` streams guaranteeing leak-free I/O operations even during IO errors.
- **Data Validation & Sanitization**: Regex validation for email addresses and telephone formats before state mutations.
- **CSV Persistence**: Serializes contact models to comma-separated values with escaping and deserializes them on startup.

## Directory Layout
```
03_exceptions_fileio_contact_book/
├── src/main/java/com/learning/contacts/
│   ├── Contact.java
│   ├── ContactException.java
│   ├── ContactNotFoundException.java
│   ├── DuplicateContactException.java
│   ├── InvalidContactException.java
│   ├── FilePersistenceException.java
│   ├── ContactBookManager.java
│   └── ContactBookApplication.java
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── app.js
└── README.md
```

## Running the Java Application
```bash
javac -d bin src/main/java/com/learning/contacts/*.java
java -cp bin com.learning.contacts.ContactBookApplication
```
