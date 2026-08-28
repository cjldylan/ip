/**
 * A task that should be completed by a specified date or time.
 */
public class Deadline extends Task {
    /** The user-provided deadline text, kept as a string at this level. */
    protected String by;

    /**
     * Creates a pending deadline task.
     *
     * @param description the deadline description
     * @param by the user-provided date or time text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Formats the deadline with its type, completion status, and due text.
     *
     * @return the formatted deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Prefixes the shared fields with the deadline type tag {@code D} and
     * appends the due text.
     *
     * @return the save-file representation of this deadline
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }
}
