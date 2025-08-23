public class ToDo extends Task {
    public ToDo(String description) throws GlendonException {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toPersistenceString() {
        return Persistence.stringifyTodo(this);
    }
}
