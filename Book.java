import java.time.LocalDate;

/** Represents one book in the library. */
public class Book {
    private final int id;
    private final String title;
    private final String author;
    private String borrower;     // empty string means "available"
    private LocalDate dueDate;   // null when not borrowed

    public Book(int id, String title, String author) {
        this(id, title, author, "", null);
    }

    public Book(int id, String title, String author, String borrower, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.borrower = borrower;
        this.dueDate = dueDate;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getBorrower() { return borrower; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isAvailable() { return borrower.isEmpty(); }

    public void checkOut(String member, LocalDate due) {
        this.borrower = member;
        this.dueDate = due;
    }

    public void checkIn() {
        this.borrower = "";
        this.dueDate = null;
    }

    /** Converts the book to one line of text for saving: id|title|author|borrower|dueDate */
    public String toRecord() {
        return id + "|" + title + "|" + author + "|" + borrower + "|" + (dueDate == null ? "" : dueDate);
    }

    /** Rebuilds a book from a saved line. */
    public static Book fromRecord(String line) {
        String[] p = line.split("\\|", -1);
        LocalDate due = p[4].isEmpty() ? null : LocalDate.parse(p[4]);
        return new Book(Integer.parseInt(p[0]), p[1], p[2], p[3], due);
    }

    @Override
    public String toString() {
        String status = isAvailable() ? "Available" : "Borrowed by " + borrower + " (due " + dueDate + ")";
        return String.format("#%-3d %-30s %-20s %s", id, title, author, status);
    }
}
