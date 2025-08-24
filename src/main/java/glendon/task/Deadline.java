package glendon.task;

import glendon.GlendonException;
import glendon.Storage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDate date;
    public Deadline(String description, LocalDate date) throws GlendonException {
        super(description);
        this.date = date;
    }

    public LocalDate getDate() {
        return this.date;
    }

    private String getFormattedDate() {

        return this.date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.getFormattedDate() + ")";
    }

    @Override
    public String toPersistenceString() {
        return Storage.stringifyDeadline(this);
    }
}
