import java.util.Scanner;

/**
 * The Baemax chatbot entry point.
 */
public class Baemax {
    /** Maximum number of tasks the chatbot keeps during one run. */
    private static final int MAX_TASKS = 100;

    /** Stores tasks in the order the user entered them. */
    private static final Task[] tasks = new Task[MAX_TASKS];

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
        while (scanner.hasNextLine()) {
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
            } else if (command.startsWith("mark ")) {
                updateTaskStatus(command, true);
            } else if (command.startsWith("unmark ")) {
                updateTaskStatus(command, false);
            } else {
                addTask(command);
            }
            System.out.println(line);
        }
        scanner.close();
    }

    /** Displays every stored task with a one-based task number. */
    private static void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
    }

    /** Adds a new not-done task, provided the task limit has not been reached. */
    private static void addTask(String description) {
        if (taskCount >= MAX_TASKS) {
            System.out.println("Sorry, I cannot store more than " + MAX_TASKS + " tasks.");
            return;
        }

        tasks[taskCount] = new Task(description);
        taskCount++;
        System.out.println("added: " + description);
    }

    /**
     * Marks or unmarks a task selected by its one-based list number.
     *
     * @param command a command such as {@code mark 2} or {@code unmark 2}
     * @param completed whether the selected task should be marked done
     */
    private static void updateTaskStatus(String command, boolean completed) {
        String[] parts = command.split("\\s+");
        int taskNumber = Integer.parseInt(parts[1]);
        Task task = tasks[taskNumber - 1];

        if (completed) {
            task.markAsDone();
            System.out.println("Nice! I've marked this task as done:");
        } else {
            task.markAsUndone();
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }
}
