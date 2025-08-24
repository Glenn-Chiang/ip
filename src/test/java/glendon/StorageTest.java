package glendon;

import glendon.task.Deadline;
import glendon.task.Event;
import glendon.task.ToDo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class StorageTest {
    @Test
    public void serializeTodo_done_success() throws Exception {
        ToDo todo = new ToDo("write tests");
        todo.mark();
        assertEquals("T | 1 | write tests", Storage.serializeTodo(todo));
    }

    @Test
    public void serializeDeadline_done_success() throws Exception {
        Deadline deadline = new Deadline("sign up for cca",
                LocalDate.parse("2025-08-24", DateTimeFormatter.ISO_LOCAL_DATE));
        deadline.mark();
        assertEquals("D | 1 | sign up for cca | 2025-08-24", Storage.serializeDeadline(deadline));
    }

    @Test
    public void serializeEvent_done_success() throws Exception {
        Event event = new Event("SLF",
                LocalDateTime.parse("2025-08-13T12:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                LocalDateTime.parse("2025-08-14T17:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        event.mark();
        assertEquals("E | 1 | SLF | 2025-08-13T12:00:00 | 2025-08-14T17:00:00", Storage.serializeEvent(event));
    }
}
