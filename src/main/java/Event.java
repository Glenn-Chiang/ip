public class Event extends Task {
    private final String start;
    private final String end;

    public Event(String description, String from, String to) throws GlendonException {
        super(description);
        if (from == null || from.isBlank()) {
            throw new GlendonException("Event start time is required");
        }
        if (to == null || to.isBlank()) {
            throw new GlendonException("Event end time is required");
        }
        this.start = from;
        this.end = to;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
    }

    @Override
    public String toPersistenceString() {
        return Persistence.stringifyEvent(this);
    }
}
