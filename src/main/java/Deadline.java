import java.time.LocalDate;

/**
 * A task that should be completed by a specified date.
 */
public class Deadline extends Task {
    /** The date this task is due. */
    protected LocalDate by;

    /**
     * Creates a pending deadline task.
     *
     * @param description the deadline description
     * @param by the date the task is due
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Formats the deadline with its type, completion status, and due date.
     *
     * @return the formatted deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + Dates.format(by) + ")";
    }

    /**
     * Prefixes the shared fields with the deadline type tag {@code D} and
     * appends the due date in {@code yyyy-MM-dd} form so it can be read back.
     *
     * @return the save-file representation of this deadline
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }
}
