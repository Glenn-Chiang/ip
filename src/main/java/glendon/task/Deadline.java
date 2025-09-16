package glendon.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import glendon.GlendonException;
import glendon.Storage;

/**
 * A Task containing a date.
 */
public class Deadline extends Task implements Comparable<Deadline> {
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

    /**
     * Converts the Deadline into a string format for file storage.
     */
    @Override
    public String toStorageString() {
        return Storage.serializeDeadline(this);
    }

    /**
     * Compares this deadline with another deadline using the date.
     */
    @Override
    public int compareTo(Deadline other) {
        return this.date.compareTo(other.date);
    }
}
