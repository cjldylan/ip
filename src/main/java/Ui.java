import java.util.Scanner;

/**
 * Handles all interaction with the user: reading commands from standard input
 * and printing the chatbot's responses to standard output. Keeping this in one
 * class means the rest of the code never touches {@code System.in} or
 * {@code System.out} directly.
 */
public class Ui {
    /** Divider printed above and below each response. */
    private static final String DIVIDER = "__________________________________________";

    /** Source of user commands. */
    private final Scanner scanner;

    /** Creates a Ui that reads commands from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Prints the startup banner and greeting. */
    public void showWelcome() {
        String banner = "╔════════════════╗\n"
                + "║     Baemax     ║\n"
                + "╚════════════════╝\n";
        System.out.println(banner);
        System.out.println("Hello, I am Baemax ✨");
        System.out.println("What can I do for you?");
    }

    /** Prints the farewell shown when the user says goodbye. */
    public void showGoodbye() {
        System.out.println("Bye! Baemax is powering down. Have a lovely day!");
    }

    /** Prints the divider that frames each response. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Reports whether the user has entered another command.
     *
     * @return {@code true} while standard input has another line to read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the raw command text, without the trailing newline
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints one or more response lines in order.
     *
     * @param lines the lines to print
     */
    public void show(String... lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }

    /**
     * Prints an error message for a command that could not be carried out.
     *
     * @param message the explanation to show the user
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Releases the input resource. */
    public void close() {
        scanner.close();
    }
}
