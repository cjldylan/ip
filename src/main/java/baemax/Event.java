package baemax;

import java.time.LocalDate;

/**
 * A task that spans a start date and an end date.
 */
public class Event extends Task {
    /** The date the event starts. */
    private final LocalDate from;

    /** The date the event ends. */
    private final LocalDate to;

    /**
     * Creates a pending event task.
     *
     * @param description the event description
     * @param from the start date
     * @param to the end date
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Formats the event with its type, completion status, and date range.
     *
     * @return the formatted event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + Dates.format(from) + " to: " + Dates.format(to) + ")";
    }

    /**
     * Prefixes the shared fields with the event type tag {@code E} and appends
     * the start and end dates in {@code yyyy-MM-dd} form so they can be read
     * back.
     *
     * @return the save-file representation of this event
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from + " | " + to;
    }
}
