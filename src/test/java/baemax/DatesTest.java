package baemax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/** Tests for {@link Dates}, which parses and formats task dates. */
public class DatesTest {

    @Test
    public void parse_validIsoDate_returnsLocalDate() throws BaemaxException {
        assertEquals(LocalDate.of(2019, 10, 15), Dates.parse("2019-10-15"));
    }

    @Test
    public void parse_surroundingWhitespace_trimmedAndParsed() throws BaemaxException {
        assertEquals(LocalDate.of(2019, 10, 15), Dates.parse("  2019-10-15\t"));
    }

    @Test
    public void parse_nonDateText_throwsBaemaxException() {
        assertThrows(BaemaxException.class, () -> Dates.parse("tomorrow"));
    }

    @Test
    public void parse_wrongFieldOrder_throwsBaemaxException() {
        assertThrows(BaemaxException.class, () -> Dates.parse("15-10-2019"));
    }

    @Test
    public void parse_wrongSeparators_throwsBaemaxException() {
        assertThrows(BaemaxException.class, () -> Dates.parse("2019/10/15"));
    }

    @Test
    public void parse_monthOutOfRange_throwsBaemaxException() {
        assertThrows(BaemaxException.class, () -> Dates.parse("2019-13-01"));
    }

    @Test
    public void parse_trailingCharacters_throwsBaemaxException() {
        assertThrows(BaemaxException.class, () -> Dates.parse("2019-10-15pm"));
    }

    @Test
    public void parse_emptyString_throwsBaemaxException() {
        assertThrows(BaemaxException.class, () -> Dates.parse(""));
    }

    @Test
    public void format_typicalDate_usesMonthDayYear() {
        assertEquals("Oct 15 2019", Dates.format(LocalDate.of(2019, 10, 15)));
    }

    @Test
    public void format_singleDigitDay_zeroPadsDay() {
        assertEquals("Jan 05 2020", Dates.format(LocalDate.of(2020, 1, 5)));
    }

    @Test
    public void formatThenParse_roundTripsToSameDate() throws BaemaxException {
        LocalDate original = LocalDate.of(2023, 3, 9);
        assertEquals(original, Dates.parse(original.toString()));
        assertEquals("Mar 09 2023", Dates.format(original));
    }
}
