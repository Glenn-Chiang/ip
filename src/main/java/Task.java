public abstract class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) throws GlendonException {
        if (description == null || description.isBlank()) {
            throw new GlendonException("Description is required");
        }
        this.description = description;
        this.isDone = false;
    }

    private String getStatusIcon() {
        return this.isDone ? "X" : " ";
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}
