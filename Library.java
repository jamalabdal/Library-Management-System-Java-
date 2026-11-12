import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/** Holds all books and the rules for borrowing, returning, searching, and saving. */
public class Library {
    private static final int LOAN_DAYS = 14;
    private static final double FINE_PER_DAY = 0.50;

    private final List<Book> books = new ArrayList<>();
    private final Path dataFile;
    private int nextId = 1;

    public Library(String fileName) {
        this.dataFile = Paths.get(fileName);
        load();
    }

    public Book addBook(String title, String author) {
        Book book = new Book(nextId++, title.replace("|", "/"), author.replace("|", "/"));
        books.add(book);
        save();
        return book;
    }

    public boolean removeBook(int id) {
        boolean removed = books.removeIf(b -> b.getId() == id);
        if (removed) save();
        return removed;
    }

    public Optional<Book> findById(int id) {
        return books.stream().filter(b -> b.getId() == id).findFirst();
    }

    public List<Book> getAll() { return books; }

    /** Case-insensitive search in title or author. */
    public List<Book> search(String keyword) {
        String k = keyword.toLowerCase();
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(k) || b.getAuthor().toLowerCase().contains(k)) {
                results.add(b);
            }
        }
        return results;
    }

    /** Returns an error message, or null if the checkout succeeded. */
    public String borrow(int id, String member) {
        Optional<Book> book = findById(id);
        if (book.isEmpty()) return "No book with ID " + id + ".";
        if (!book.get().isAvailable()) return "That book is already borrowed.";
        book.get().checkOut(member, LocalDate.now().plusDays(LOAN_DAYS));
        save();
        return null;
    }

    /** Returns the fine owed (0 if on time), or -1 if the book could not be returned. */
    public double giveBack(int id) {
        Optional<Book> book = findById(id);
        if (book.isEmpty() || book.get().isAvailable()) return -1;
        long daysLate = ChronoUnit.DAYS.between(book.get().getDueDate(), LocalDate.now());
        book.get().checkIn();
        save();
        return daysLate > 0 ? daysLate * FINE_PER_DAY : 0;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Book b : books) lines.add(b.toRecord());
        try {
            Files.write(dataFile, lines);
        } catch (IOException e) {
            System.out.println("Warning: could not save data: " + e.getMessage());
        }
    }

    private void load() {
        if (!Files.exists(dataFile)) return;
        try {
            for (String line : Files.readAllLines(dataFile)) {
                if (line.isBlank()) continue;
                Book b = Book.fromRecord(line);
                books.add(b);
                nextId = Math.max(nextId, b.getId() + 1);
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Warning: could not read saved data: " + e.getMessage());
        }
    }
}
