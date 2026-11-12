import java.util.List;
import java.util.Scanner;

/** Console menu for the Library Management System. */
public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Library library = new Library("books.txt");
        System.out.println("=== Library Management System ===");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Add book   2. List books   3. Search   4. Borrow");
            System.out.println("5. Return     6. Remove book  0. Exit");
            switch (ask("Choose: ")) {
                case "1" -> {
                    Book b = library.addBook(ask("Title: "), ask("Author: "));
                    System.out.println("Added: " + b);
                }
                case "2" -> printBooks(library.getAll());
                case "3" -> printBooks(library.search(ask("Keyword: ")));
                case "4" -> {
                    int id = askInt("Book ID: ");
                    String error = library.borrow(id, ask("Member name: "));
                    System.out.println(error == null ? "Borrowed! Due in 14 days." : error);
                }
                case "5" -> {
                    double fine = library.giveBack(askInt("Book ID: "));
                    if (fine < 0) System.out.println("That book is not currently borrowed.");
                    else if (fine == 0) System.out.println("Returned on time. Thank you!");
                    else System.out.printf("Returned late. Fine: $%.2f%n", fine);
                }
                case "6" -> System.out.println(library.removeBook(askInt("Book ID: ")) ? "Removed." : "Not found.");
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printBooks(List<Book> books) {
        if (books.isEmpty()) System.out.println("No books found.");
        else books.forEach(System.out::println);
    }

    private static String ask(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private static int askInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(ask(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}
