package glendon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;


/**
 * Unit tests for the Parser class.
 *
 */
public class ParserTest {
    /**
     * Tests that a valid ToDo command is parsed into a ToDo task successfully.
     */
    @Test
    public void parseTask_validTodo_success() throws Exception {
        assertEquals("[T][ ] buy new keyboard", Parser.parseTask("todo buy new keyboard").toString());
    }

    /**
     * Tests that an invalid ToDo command (missing description) throws an exception
     * with the expected error message.
     */
    @Test
    public void parseTask_invalidTodo_exceptionThrown() {
        try {
            Parser.parseTask("todo  ");
            fail();
        } catch (Exception e) {
            assertEquals("Invalid todo format", e.getMessage());
        }
    }

    /**
     * Tests that a valid Deadline command with a properly formatted date is parsed
     * into a Deadline task successfully.
     */
    @Test
    public void parseTask_validDeadline_success() throws Exception {
        assertEquals("[D][ ] submit assignment (by: Aug 24 2025)",
                Parser.parseTask("deadline submit assignment /by 2025-08-24").toString());
    }

    /**
     * Tests that a Deadline command missing the required "/by" segment throws an
     * exception with the expected error message.
     */
    @Test
    public void parseTask_invalidDeadline_exceptionThrown() {
        try {
            Parser.parseTask("deadline submit assignment ");
            fail();
        } catch (Exception e) {
            assertEquals("Invalid deadline format", e.getMessage());
        }
    }

    /**
     * Tests that a Deadline command with an incorrectly formatted date string
     * throws an exception with the expected error message.
     */
    @Test
    public void parseTask_invalidDeadlineDateFormat_exceptionThrown() {
        try {
            Parser.parseTask("deadline submit assignment /by 25 August 2025");
            fail();
        } catch (Exception e) {
            assertEquals("Invalid deadline date format", e.getMessage());
        }
    }

    /**
     * Tests that a valid Event command with properly formatted start and end date
     * times is parsed into an Event task successfully.
     */
    @Test
    public void parseTask_validEvent_success() throws Exception {
        assertEquals("[E][ ] workout (from: Aug 24 2025 1400 to: Aug 24 2025 1600)",
                Parser.parseTask("event workout /from 2025-08-24 1400 /to 2025-08-24 1600").toString());
    }

    /**
     * Tests that an Event command missing required date-time components throws an
     * exception with the expected error message.
     */
    @Test
    public void parseTask_invalidEvent_exceptionThrown() {
        try {
            Parser.parseTask("event workout /from");
            fail();
        } catch (Exception e) {
            assertEquals("Invalid event format", e.getMessage());
        }
    }

    /**
     * Tests that an Event command with incorrectly formatted start and end
     * date-time strings throws an exception with the expected error message.
     */
    @Test
    public void parseTask_invalidEventDateTimeFormat_exceptionThrown() {
        try {
            Parser.parseTask("event workout /from sometime /to someday");
            fail();
        } catch (Exception e) {
            assertEquals("Invalid event start or end datetime format", e.getMessage());
        }
    }
}
