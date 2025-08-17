public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) throws GlendonException {
        super(description);
        if (from == null || from.isBlank()) {
            throw new GlendonException("Event start time is required");
        }
        if (to == null || to.isBlank()) {
            throw new GlendonException("Event end time is required");
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }
}
