/**
 * A task that starts and ends at specified date or time text.
 */
public class Event extends Task {
    /** User-provided event start text. */
    protected String from;

    /** User-provided event end text. */
    protected String to;

    /**
     * Creates a pending event task.
     *
     * @param description the event description
     * @param from the user-provided start text
     * @param to the user-provided end text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Formats the event with its type, completion status, and time range.
     *
     * @return the formatted event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Prefixes the shared fields with the event type tag {@code E} and
     * appends the start and end text.
     *
     * @return the save-file representation of this event
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from + " | " + to;
    }
}
