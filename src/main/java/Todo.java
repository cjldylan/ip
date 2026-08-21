/**
 * A task without an attached date or time.
 */
public class Todo extends Task {
    /**
     * Creates a pending todo task.
     *
     * @param description the todo description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Formats the todo with its type and completion status.
     *
     * @return the formatted todo
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
