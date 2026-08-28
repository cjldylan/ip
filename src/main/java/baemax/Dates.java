package baemax;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Converts between the date text a user types and {@link LocalDate}, so the
 * chatbot uses one input format ({@code yyyy-MM-dd}) and one display format
 * ({@code MMM dd yyyy}) everywhere.
 */
public class Dates {
    /** Format accepted from the user and stored in the save file, e.g. {@code 2019-10-15}. */
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** Format shown back to the user, e.g. {@code Oct 15 2019}. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /** Prevents instantiation; this class only exposes static helpers. */
    private Dates() {
    }

    /**
     * Parses a date written as {@code yyyy-MM-dd}.
     *
     * @param raw the date text, e.g. {@code 2019-10-15}
     * @return the parsed date
     * @throws BaemaxException when the text is not a valid {@code yyyy-MM-dd} date
     */
    public static LocalDate parse(String raw) throws BaemaxException {
        try {
            return LocalDate.parse(raw.trim(), INPUT_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new BaemaxException(
                    "Baemax reads dates as yyyy-MM-dd, e.g. 2019-10-15. Got: " + raw);
        }
    }

    /**
     * Formats a date for display as {@code MMM dd yyyy}.
     *
     * @param date the date to format
     * @return the display text, e.g. {@code Oct 15 2019}
     */
    public static String format(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }
}
