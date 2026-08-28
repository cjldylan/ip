import java.util.List;

/**
 * The Baemax chatbot entry point.
 */
public class Baemax {
    /** The tasks the user is tracking. */
    private static TaskList tasks = new TaskList();

    /** Reads and writes {@link #tasks} to disk so they persist between runs. */
    private static final Storage storage = new Storage("data/baemax.txt");

    /** Handles reading commands from and printing responses to the user. */
    private static final Ui ui = new Ui();

    /**
     * Runs the chatbot and responds to user commands until the user says goodbye.
     *
     * @param args command-line arguments, which this chatbot does not use
     */
    public static void main(String[] args) {
        ui.showWelcome();

        tasks = new TaskList(storage.load());

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();

            if (command.equals("bye")) {
                ui.showLine();
                ui.showGoodbye();
                ui.showLine();
                break;
            }

            ui.showLine();
            try {
                processCommand(command);
            } catch (BaemaxException exception) {
                ui.showError(exception.getMessage());
            }
            ui.showLine();
        }
        ui.close();
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
            addTask(Parser.parseTask(trimmedCommand));
        }
    }

    /** Displays every stored task with a one-based task number. */
    private static void displayTasks() {
        ui.show("Here are the tasks in your list:");
        List<Task> all = tasks.asList();
        for (int i = 0; i < all.size(); i++) {
            ui.show((i + 1) + ". " + all.get(i));
        }
    }

    /** Adds a new not-done task to the end of the list and saves it. */
    private static void addTask(Task task) {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.show("Got it. I've added this task:",
                "  " + task,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Marks or unmarks a task selected by its one-based list number.
     *
     * @param command a command such as {@code mark 2} or {@code unmark 2}
     * @param completed whether the selected task should be marked done
     * @throws BaemaxException when the command has no valid task number
     */
    private static void updateTaskStatus(String command, boolean completed) throws BaemaxException {
        int taskNumber = Parser.parseTaskNumber(command, completed ? "mark" : "unmark", tasks.size());
        Task task = tasks.get(taskNumber);

        if (completed) {
            task.markAsDone();
            ui.show("Nice! I've marked this task as done:");
        } else {
            task.markAsUndone();
            ui.show("OK, I've marked this task as not done yet:");
        }
        storage.save(tasks.asList());
        ui.show("  " + task);
    }

    /** Removes a task selected by its one-based list number and saves the list. */
    private static void deleteTask(String command) throws BaemaxException {
        int taskNumber = Parser.parseTaskNumber(command, "delete", tasks.size());
        Task removedTask = tasks.remove(taskNumber);
        storage.save(tasks.asList());
        ui.show("Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list.");
    }
}
