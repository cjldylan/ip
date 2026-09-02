package baemax;

/**
 * Interprets the raw text a user types into something the chatbot can act on:
 * the task described by an add command, or the task number named by a
 * mark, unmark, or delete command.
 */
public class Parser {

    /** Prevents instantiation; this class only exposes static helpers. */
    private Parser() {
    }

    /**
     * Reads an add command ({@code todo}, {@code deadline}, or {@code event})
     * and builds the matching task.
     *
     * @param command the full command text, already trimmed
     * @return the task the command describes
     * @throws BaemaxException when the command word is unknown or a required
     *     part of the command is missing
     */
    public static Task parseTask(String command) throws BaemaxException {
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
                        "A deadline needs a due date. Try: deadline <description> /by <date> [time].");
            }

            String description = details.substring(0, byMarker).trim();
            String by = details.substring(byMarker + " /by ".length()).trim();
            if (description.isEmpty() || by.isEmpty()) {
                throw new BaemaxException(
                        "A deadline needs both a description and a due date.");
            }
            return new Deadline(description, TaskDate.parse(by));
        }

        if (command.equals("event") || command.startsWith("event ")) {
            String details = command.substring("event".length()).trim();
            int fromMarker = details.indexOf(" /from ");
            if (fromMarker < 0) {
                throw new BaemaxException(
                        "An event needs a start and end date. Try: "
                        + "event <description> /from <date> [time] /to <date> [time].");
            }

            String description = details.substring(0, fromMarker).trim();
            String timeRange = details.substring(fromMarker + " /from ".length()).trim();
            int toMarker = timeRange.indexOf(" /to ");
            if (toMarker < 0) {
                throw new BaemaxException(
                        "An event needs an end date. Add /to <date> [time] after its start date.");
            }

            String from = timeRange.substring(0, toMarker).trim();
            String to = timeRange.substring(toMarker + " /to ".length()).trim();
            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new BaemaxException(
                        "An event needs a description, a start date, and an end date.");
            }
            return new Event(description, TaskDate.parse(from), TaskDate.parse(to));
        }

        throw new BaemaxException(
                "Baemax does not know that command yet. "
                + "Try todo, deadline, event, list, find, mark, unmark, delete, or bye.");
    }

    /**
     * Reads the task number from a selection command such as {@code mark 2}.
     *
     * @param command the full command text
     * @param action the command word, used in the error message
     * @param taskCount the number of tasks currently in the list
     * @return the one-based task number
     * @throws BaemaxException when the number is missing, not a number, or
     *     outside the range of the list
     */
    public static int parseTaskNumber(String command, String action, int taskCount) throws BaemaxException {
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

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BaemaxException(
                    "That task number is out of range. Choose a number from 1 to " + taskCount + ".");
        }
        return taskNumber;
    }
}
