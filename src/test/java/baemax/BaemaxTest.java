package baemax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests for {@link Baemax#getResponse(String)}, the reply path shared by the CLI and the GUI. */
public class BaemaxTest {

    @TempDir
    Path tempDir;

    private Baemax freshBaemax() {
        return new Baemax(tempDir.resolve("tasks.txt").toString());
    }

    @Test
    public void getResponse_todo_confirmsAndCounts() {
        Baemax baemax = freshBaemax();
        String response = baemax.getResponse("todo read book");
        assertTrue(response.contains("[T][ ] read book"));
        assertTrue(response.contains("Now you have 1 tasks in the list."));
    }

    @Test
    public void getResponse_list_numbersTasksFromOne() {
        Baemax baemax = freshBaemax();
        baemax.getResponse("todo a");
        baemax.getResponse("todo b");
        String response = baemax.getResponse("list");
        assertEquals("Here are the tasks in your list:\n1. [T][ ] a\n2. [T][ ] b", response);
    }

    @Test
    public void getResponse_unknownCommand_returnsErrorMessage() {
        assertEquals(
                "Baemax does not know that command yet. "
                        + "Try todo, deadline, event, list, find, mark, unmark, delete, or bye.",
                freshBaemax().getResponse("sing a song"));
    }

    @Test
    public void getResponse_bye_returnsFarewell() {
        assertEquals("Bye! Baemax is powering down. Have a lovely day!",
                freshBaemax().getResponse("bye"));
    }

    @Test
    public void getResponse_find_listsMatchesOnly() {
        Baemax baemax = freshBaemax();
        baemax.getResponse("todo read book");
        baemax.getResponse("todo buy milk");
        String response = baemax.getResponse("find book");
        assertTrue(response.contains("read book"));
        assertTrue(!response.contains("buy milk"));
    }
}
