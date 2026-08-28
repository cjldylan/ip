package baemax;

/**
 * Represents one task stored by the Baemax chatbot.
 */
public class Task {
    /** The text entered by the user for this task. */
    private String description;

    /** Whether the user has marked this task as done. */
    private boolean isDone;

    /**
     * Creates a new task that starts in the not-done state.
     *
     * @param description the task text
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return {@code X} for a done task or a space for a pending task
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsUndone() {
        isDone = false;
    }

    /**
     * Formats this task with its status icon and description.
     *
     * @return the task display text
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Encodes the fields shared by every task for the save file as
     * {@code <status> | <description>}, where status is {@code 1} when the
     * task is done and {@code 0} otherwise. Subtypes prepend a type tag and
     * append their own fields.
     *
     * @return the save-file representation of this task
     */
    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
