import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMM d yyyy HHmm");

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
        return "[E]" + super.toString() + " (from: " + this.start.format(dateTimeFormat) + " to: " +
                this.end.format(dateTimeFormat) + ")";
    }

    @Override
    public String toPersistenceString() {
        return Persistence.stringifyEvent(this);
    }
}
