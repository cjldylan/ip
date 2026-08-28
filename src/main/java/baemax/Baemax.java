package baemax;

import java.util.List;

/**
 * The Baemax chatbot: a command-line to-do assistant that remembers its tasks
 * between runs. This class wires together the {@link Ui}, {@link Storage}, and
 * {@link TaskList} collaborators and runs the command loop.
 */
public class Baemax {
    /** Reads tasks from and writes tasks to the save file. */
    private final Storage storage;

    /** The tasks the user is tracking. */
    private final TaskList tasks;

    /** Handles reading commands from and printing responses to the user. */
    private final Ui ui;

    /**
     * Creates a chatbot that loads from and saves to the given file.
     *
     * @param filePath the save file path, relative to the project root
     */
    public Baemax(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Starts the chatbot.
     *
     * @param args command-line arguments, which this chatbot does not use
     */
    public static void main(String[] args) {
        new Baemax("data/baemax.txt").run();
    }

    /** Greets the user, then handles commands until the user says goodbye. */
    public void run() {
        ui.showWelcome();

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
    private void processCommand(String command) throws BaemaxException {
        String trimmedCommand = command.trim();
        if (trimmedCommand.isEmpty()) {
            throw new BaemaxException(
                    "Baemax did not catch a command. Try todo, list, find, mark, delete, or bye.");
        }

        if (trimmedCommand.equals("list")) {
            displayTasks();
        } else if (trimmedCommand.equals("mark") || trimmedCommand.startsWith("mark ")) {
            updateTaskStatus(trimmedCommand, true);
        } else if (trimmedCommand.equals("unmark") || trimmedCommand.startsWith("unmark ")) {
            updateTaskStatus(trimmedCommand, false);
        } else if (trimmedCommand.equals("delete") || trimmedCommand.startsWith("delete ")) {
            deleteTask(trimmedCommand);
        } else if (trimmedCommand.equals("find") || trimmedCommand.startsWith("find ")) {
            findTasks(trimmedCommand);
        } else {
            addTask(Parser.parseTask(trimmedCommand));
        }
    }

    /** Displays every stored task with a one-based task number. */
    private void displayTasks() {
        ui.show("Here are the tasks in your list:");
        List<Task> all = tasks.asList();
        for (int i = 0; i < all.size(); i++) {
            ui.show((i + 1) + ". " + all.get(i));
        }
    }

    /**
     * Lists the tasks whose description contains the keyword given after
     * {@code find}, numbered from one.
     *
     * @param command a command such as {@code find book}
     * @throws BaemaxException when no keyword follows {@code find}
     */
    private void findTasks(String command) throws BaemaxException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new BaemaxException("Baemax needs a keyword to search for. Try: find <keyword>.");
        }

        List<Task> matches = tasks.find(keyword);
        ui.show("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            ui.show((i + 1) + ". " + matches.get(i));
        }
    }

    /**
     * Adds a new not-done task to the end of the list and saves it.
     *
     * @param task the task to add
     */
    private void addTask(Task task) {
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
    private void updateTaskStatus(String command, boolean completed) throws BaemaxException {
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

    /**
     * Removes a task selected by its one-based list number and saves the list.
     *
     * @param command a command such as {@code delete 2}
     * @throws BaemaxException when the command has no valid task number
     */
    private void deleteTask(String command) throws BaemaxException {
        int taskNumber = Parser.parseTaskNumber(command, "delete", tasks.size());
        Task removedTask = tasks.remove(taskNumber);
        storage.save(tasks.asList());
        ui.show("Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list.");
    }
}
