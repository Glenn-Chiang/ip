public class Deadline extends Task {
    private String date;
    public Deadline(String description, String date) throws GlendonException {
        super(description);
        if (date == null || date.isBlank()) {
            throw new GlendonException("Deadline date is required");
        }
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.date + ")";
    }

    @Override
    public String toPersistenceString() {
        return Persistence.stringifyDeadline(this);
    }
}
