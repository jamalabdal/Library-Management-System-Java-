# Library Management System (Java)

A console application that manages a small library using core Object-Oriented Programming concepts. Data is saved to a text file, so books persist between runs.

## Features
- Add, list, search, and remove books
- Borrow and return books with a 14-day loan period
- Automatic late fines ($0.50 per day overdue)
- Persistent storage in `books.txt`
- Input validation for menu choices and IDs

## Tech Stack
Java 17+ (uses `java.time`, `java.nio.file`, switch expressions)

## How to Run
```bash
git clone https://github.com/jamalabdal/library-management-java.git
cd library-management-java
javac *.java
java Main
```

## Project Structure
| File | Responsibility |
|------|----------------|
| `Book.java` | Data model: one book, its borrower, and due date |
| `Library.java` | Business logic: search, borrow, return, fines, file storage |
| `Main.java` | Console menu and user input |

## Concepts Demonstrated
Encapsulation, separation of concerns, collections (`ArrayList`, `Optional`), file I/O, date handling.

## Ideas for Improvement
- Add a `Member` class and borrowing limits
- Replace the text file with a database using JDBC
