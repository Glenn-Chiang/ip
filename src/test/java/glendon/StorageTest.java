package glendon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import glendon.task.Deadline;
import glendon.task.Event;
import glendon.task.ToDo;

/**
 * Unit tests for the serialization methods in the Storage class.
 */
public class StorageTest {
    /**
     * Tests that a completed ToDo task is serialized into the expected storage format.
     */
    @Test
    public void serializeTodo_done_success() throws Exception {
        ToDo todo = new ToDo("write tests");
        todo.mark();
        assertEquals("T | 1 | write tests", Storage.serializeTodo(todo));
    }

    /**
     * Tests that a completed Deadline task is serialized into the expected storage format,
     * including the deadline date.
     */
    @Test
    public void serializeDeadline_done_success() throws Exception {
        Deadline deadline = new Deadline("sign up for cca",
                LocalDate.parse("2025-08-24", DateTimeFormatter.ISO_LOCAL_DATE));
        deadline.mark();
        assertEquals("D | 1 | sign up for cca | 2025-08-24", Storage.serializeDeadline(deadline));
    }

    /**
     * Tests that a completed Event task is serialized into the expected storage format,
     * including both start and end date-times.
     */
    @Test
    public void serializeEvent_done_success() throws Exception {
        Event event = new Event("SLF",
                LocalDateTime.parse("2025-08-13T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                LocalDateTime.parse("2025-08-14T17:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        event.mark();
        assertEquals("E | 1 | SLF | 2025-08-13T12:00:00 | 2025-08-14T17:00:00", Storage.serializeEvent(event));
    }
}
