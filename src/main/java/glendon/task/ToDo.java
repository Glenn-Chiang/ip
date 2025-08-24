package glendon.task;

import glendon.GlendonException;
import glendon.Storage;

public class ToDo extends Task {
    public ToDo(String description) throws GlendonException {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Converts the ToDo into a string format for file storage.
     */
    @Override
    public String toStorageString() {
        return Storage.serializeTodo(this);
    }
}
