import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Baemax chatbot entry point.
 */
public class Baemax {
    /** Stores tasks in the order the user entered them. */
    private static final ArrayList<Task> tasks = new ArrayList<>(100);

    /** Reads and writes {@link #tasks} to disk so they persist between runs. */
    private static final Storage storage = new Storage("data/baemax.txt");

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
            try {
                processCommand(command);
            } catch (BaemaxException exception) {
                System.out.println(exception.getMessage());
            }
            System.out.println(line);
        }
        scanner.close();
    }

    /**
     * Executes one non-exit command.
     *
     * @param command the complete command entered by the user
     * @throws BaemaxException when the command is invalid
     */
    private static void processCommand(String command) throws BaemaxException {
        String trimmedCommand = command.trim();
        if (trimmedCommand.isEmpty()) {
            throw new BaemaxException(
                    "Baemax did not catch a command. Try todo, list, mark, delete, or bye.");
        }

        if (trimmedCommand.equals("list")) {
            displayTasks();
        } else if (trimmedCommand.equals("mark") || trimmedCommand.startsWith("mark ")) {
            updateTaskStatus(trimmedCommand, true);
        } else if (trimmedCommand.equals("unmark") || trimmedCommand.startsWith("unmark ")) {
            updateTaskStatus(trimmedCommand, false);
        } else if (trimmedCommand.equals("delete") || trimmedCommand.startsWith("delete ")) {
            deleteTask(trimmedCommand);
        } else {
            addTask(parseTask(trimmedCommand));
        }
    }

    /** Displays every stored task with a one-based task number. */
    private static void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /** Adds a new not-done task to the end of the collection and saves the list. */
    private static void addTask(Task task) {
        tasks.add(task);
        storage.save(tasks);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Converts a command into the appropriate task subtype.
     *
     * @param command a todo, deadline, or event command
     * @return the task represented by the command
     * @throws BaemaxException when the command is unknown or malformed
     */
    private static Task parseTask(String command) throws BaemaxException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new BaemaxException(
                        "Baemax needs a description for a todo. Try: todo <description>.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String details = command.substring("deadline".length()).trim();
            int byMarker = details.indexOf(" /by ");
            if (byMarker < 0) {
                throw new BaemaxException(
                        "A deadline needs a due time. Try: deadline <description> /by <date or time>.");
            }

            String description = details.substring(0, byMarker).trim();
            String by = details.substring(byMarker + " /by ".length()).trim();
            if (description.isEmpty() || by.isEmpty()) {
                throw new BaemaxException(
                        "A deadline needs both a description and a due time.");
            }
            return new Deadline(description, by);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            String details = command.substring("event".length()).trim();
            int fromMarker = details.indexOf(" /from ");
            if (fromMarker < 0) {
                throw new BaemaxException(
                        "An event needs a start and end time. Try: event <description> /from <start> /to <end>.");
            }

            String description = details.substring(0, fromMarker).trim();
            String timeRange = details.substring(fromMarker + " /from ".length()).trim();
            int toMarker = timeRange.indexOf(" /to ");
            if (toMarker < 0) {
                throw new BaemaxException(
                        "An event needs an end time. Add /to <end> after its start time.");
            }

            String from = timeRange.substring(0, toMarker).trim();
            String to = timeRange.substring(toMarker + " /to ".length()).trim();
            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new BaemaxException(
                        "An event needs a description, a start time, and an end time.");
            }
            return new Event(description, from, to);
        }

        throw new BaemaxException(
                "Baemax does not know that command yet. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /**
     * Marks or unmarks a task selected by its one-based list number.
     *
     * @param command a command such as {@code mark 2} or {@code unmark 2}
     * @param completed whether the selected task should be marked done
     * @throws BaemaxException when the command has no valid task number
     */
    private static void updateTaskStatus(String command, boolean completed) throws BaemaxException {
        int taskNumber = parseTaskNumber(command, completed ? "mark" : "unmark");
        Task task = tasks.get(taskNumber - 1);

        if (completed) {
            task.markAsDone();
            System.out.println("Nice! I've marked this task as done:");
        } else {
            task.markAsUndone();
            System.out.println("OK, I've marked this task as not done yet:");
        }
        storage.save(tasks);
        System.out.println("  " + task);
    }

    /** Removes a task selected by its one-based list number and saves the list. */
    private static void deleteTask(String command) throws BaemaxException {
        int taskNumber = parseTaskNumber(command, "delete");
        Task removedTask = tasks.remove(taskNumber - 1);
        storage.save(tasks);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Parses and validates the task number in a task-selection command. */
    private static int parseTaskNumber(String command, String action) throws BaemaxException {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new BaemaxException(
                    "Please include a task number, such as " + action + " 2.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new BaemaxException("Task numbers look like 1, 2, or 3—not words.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BaemaxException(
                    "That task number is out of range. Choose a number from 1 to " + tasks.size() + ".");
        }
        return taskNumber;
    }
}
