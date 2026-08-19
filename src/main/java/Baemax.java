/**
 * The Baemax chatbot entry point.
 */
import java.util.Scanner;
public class Baemax {
    /** Maximum number of tasks the chatbot keeps during one run. */
    private static final int MAX_TASKS = 100;

    /** Stores tasks in the order the user entered them. */
    private static final String[] tasks = new String[MAX_TASKS];

    /** Number of occupied entries in {@link #tasks}. */
    private static int taskCount = 0;

    /**
     * Runs the chatbot and responds to user commands until the user says goodbye.
     *
     * @param args command-line arguments, which this chatbot does not use
     */
    public static void main(String[] args) {
        String banner = "╔════════════════╗\n"
                + "║     Baemax     ║\n"
                + "╚════════════════╝\n";
        System.out.println(banner);

        System.out.println("Hello, I am Baemax ✨");
        System.out.println("What can I do for you?");
        String line = "__________________________________________";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println(line);
                System.out.println("Bye! Baemax is powering down. Have a lovely day!");
                System.out.println(line);
                break;
            }

            System.out.println(line);
            if (command.equals("list")) {
                displayTasks();
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            } else {
                System.out.println("Sorry, I cannot store more than " + MAX_TASKS + " tasks.");
            }
            System.out.println(line);
        }
        scanner.close();
    }

    /** Displays every stored task with a one-based task number. */
    private static void displayTasks() {
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
    }
}
