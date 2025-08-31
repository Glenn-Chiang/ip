package glendon.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import glendon.GlendonException;
import glendon.Storage;

public class Event extends Task {
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMM d yyyy HHmm");
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Event(String description, LocalDateTime start, LocalDateTime end) throws GlendonException {
        super(description);
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start.format(dateTimeFormat)
                + " to: " + this.end.format(dateTimeFormat) + ")";
    }

    /**
     * Converts the Event into a string format for file storage.
     */
    @Override
    public String toStorageString() {
        return Storage.serializeEvent(this);
    }
}
