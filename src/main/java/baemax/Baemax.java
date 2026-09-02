package baemax;

import java.util.List;

/**
 * The Baemax chatbot: a to-do assistant that remembers its tasks between runs.
 * This class wires together the {@link Storage} and {@link TaskList}
 * collaborators and turns each user command into a response. It can be driven
 * from the command line via {@link #run()} or from a GUI via
 * {@link #getResponse(String)}.
 */
public class Baemax {
    /** Reads tasks from and writes tasks to the save file. */
    private final Storage storage;

    /** The tasks the user is tracking. */
    private final TaskList tasks;

    /** Handles reading commands from and printing responses to the console. */
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
     * Starts the chatbot on the command line.
     *
     * @param args command-line arguments, which this chatbot does not use
     */
    public static void main(String[] args) {
        new Baemax("data/baemax.txt").run();
    }

    /**
     * Returns the greeting shown when the chatbot starts.
     *
     * @return the welcome message
     */
    public String getWelcomeMessage() {
        return "Hello, I am Baemax!\nWhat can I do for you?";
    }

    /**
     * Handles one line of user input and returns Baemax's reply.
     *
     * @param input the raw command entered by the user
     * @return the reply to show the user
     */
    public String getResponse(String input) {
        if (input.trim().equals("bye")) {
            return "Bye! Baemax is powering down. Have a lovely day!";
        }
        try {
            return processCommand(input);
        } catch (BaemaxException exception) {
            return exception.getMessage();
        }
    }

    /** Greets the user, then handles commands until the user says goodbye. */
    public void run() {
        ui.showBanner();
        ui.show(getWelcomeMessage());

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showLine();
            ui.show(getResponse(command));
            ui.showLine();
            isExit = command.trim().equals("bye");
        }
        ui.close();
    }

    /**
     * Executes one non-exit command and returns its response text.
     *
     * @param command the complete command entered by the user
     * @return the response to show the user
     * @throws BaemaxException when the command is invalid
     */
    private String processCommand(String command) throws BaemaxException {
        String trimmedCommand = command.trim();
        if (trimmedCommand.isEmpty()) {
            throw new BaemaxException(
                    "Baemax did not catch a command. Try todo, list, find, mark, delete, or bye.");
        }

        if (trimmedCommand.equals("list")) {
            return displayTasks();
        } else if (trimmedCommand.equals("mark") || trimmedCommand.startsWith("mark ")) {
            return updateTaskStatus(trimmedCommand, true);
        } else if (trimmedCommand.equals("unmark") || trimmedCommand.startsWith("unmark ")) {
            return updateTaskStatus(trimmedCommand, false);
        } else if (trimmedCommand.equals("delete") || trimmedCommand.startsWith("delete ")) {
            return deleteTask(trimmedCommand);
        } else if (trimmedCommand.equals("find") || trimmedCommand.startsWith("find ")) {
            return findTasks(trimmedCommand);
        } else {
            return addTask(Parser.parseTask(trimmedCommand));
        }
    }

    /** Returns every stored task, numbered from one. */
    private String displayTasks() {
        return numberedList("Here are the tasks in your list:", tasks.asList());
    }

    /**
     * Returns the tasks whose description contains the keyword given after
     * {@code find}, numbered from one.
     *
     * @param command a command such as {@code find book}
     * @return the matching tasks, numbered
     * @throws BaemaxException when no keyword follows {@code find}
     */
    private String findTasks(String command) throws BaemaxException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new BaemaxException("Baemax needs a keyword to search for. Try: find <keyword>.");
        }
        return numberedList("Here are the matching tasks in your list:", tasks.find(keyword));
    }

    /**
     * Adds a new not-done task to the end of the list and saves it.
     *
     * @param task the task to add
     * @return the confirmation message
     */
    private String addTask(Task task) {
        tasks.add(task);
        storage.save(tasks.asList());
        return lines("Got it. I've added this task:",
                "  " + task,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Marks or unmarks a task selected by its one-based list number.
     *
     * @param command a command such as {@code mark 2} or {@code unmark 2}
     * @param completed whether the selected task should be marked done
     * @return the confirmation message
     * @throws BaemaxException when the command has no valid task number
     */
    private String updateTaskStatus(String command, boolean completed) throws BaemaxException {
        int taskNumber = Parser.parseTaskNumber(command, completed ? "mark" : "unmark", tasks.size());
        Task task = tasks.get(taskNumber);

        String heading;
        if (completed) {
            task.markAsDone();
            heading = "Nice! I've marked this task as done:";
        } else {
            task.markAsUndone();
            heading = "OK, I've marked this task as not done yet:";
        }
        storage.save(tasks.asList());
        return lines(heading, "  " + task);
    }

    /**
     * Removes a task selected by its one-based list number and saves the list.
     *
     * @param command a command such as {@code delete 2}
     * @return the confirmation message
     * @throws BaemaxException when the command has no valid task number
     */
    private String deleteTask(String command) throws BaemaxException {
        int taskNumber = Parser.parseTaskNumber(command, "delete", tasks.size());
        Task removedTask = tasks.remove(taskNumber);
        storage.save(tasks.asList());
        return lines("Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Formats a heading followed by the given tasks, each on its own line and numbered from one. */
    private static String numberedList(String heading, List<Task> items) {
        StringBuilder builder = new StringBuilder(heading);
        for (int i = 0; i < items.size(); i++) {
            builder.append("\n").append(i + 1).append(". ").append(items.get(i));
        }
        return builder.toString();
    }

    /**
     * Joins the given lines with newlines into a single response block.
     *
     * @param lines the lines to join, in order
     * @return the lines separated by {@code \n}
     */
    private static String lines(String... lines) {
        return String.join("\n", lines);
    }
}
