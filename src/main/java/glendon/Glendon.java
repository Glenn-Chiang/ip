package glendon;

import java.util.List;

import glendon.task.Deadline;
import glendon.task.Task;

/**
 * Chatbot that manages tasks
 */
public class Glendon {
    public enum Command {
        BYE("bye"),
        LIST("list"),
        MARK("mark"),
        UNMARK("unmark"),
        DELETE("delete"),
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        FIND("find"),

        SORT_DEADLINES("sort_d");

        public final String keyword;

        Command(String keyword) {
            this.keyword = keyword;
        }
    }

    private final Storage storage;
    private TaskList taskList;

    private static final String greetingMessage = "Hello! I'm Glendon. What can I do for you?";
    private static final String unknownCommandMessage = "Unknown command";

    public Glendon(String storagePath) throws GlendonException {
        storage = new Storage(storagePath);
        this.taskList = new TaskList(this.storage.loadTasks());
    }

    /**
     * Returns greeting message
     */
    public String getGreeting() {
        return greetingMessage;
    }

    /**
     * Reads user commands and executes responses, then returns a response message.
     *
     * @param input The raw input entered by the user.
     * @return The response message.
     */
    public String getResponse(String input) {
        Command command = Parser.parseCommand(input);

        if (command == null) {
            return unknownCommandMessage;
        }

        String response = null;

        try {
            switch (command) {
            case BYE:
                System.exit(0);
                break;
            case LIST:
                response = handleListTasks();
                break;
            case MARK:
                response = handleMarkTask(Parser.parseIndex(input));
                saveTasks();
                break;
            case UNMARK:
                response = handleUnmarkTask(Parser.parseIndex(input));
                saveTasks();
                break;
            case DELETE:
                response = handleDeleteTask(Parser.parseIndex(input));
                saveTasks();
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
                response = handleAddTask(Parser.parseTask(input));
                saveTasks();
                break;
            case FIND:
                response = handleFindTask(Parser.parseSearchKey(input));
                break;
            case SORT_DEADLINES:
                response = handleSortDeadlines();
                break;
            default:
                response = unknownCommandMessage;
            }
        } catch (GlendonException e) {
            response = e.getMessage();
        }

        return response;
    }

    /**
     * Returns list of sorted deadlines as a formatted string.
     */
    private String handleSortDeadlines() {
        List<Deadline> tasks = this.taskList.sortDeadlines();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            res.append((i + 1)).append(".").append(task).append("\n");
        }
        return res.toString();
    }

    /**
     * Writes all tasks to storage file.
     *
     * @throws GlendonException if IO error occurs
     */
    private void saveTasks() throws GlendonException {
        this.storage.saveTasks(this.taskList.getTasks());
    }

    /**
     * Returns list of tasks as a formatted string.
     */
    private String handleListTasks() {
        List<Task> tasks = this.taskList.getTasks();

        if (tasks.isEmpty()) {
            return "No tasks have been added";
        }

        StringBuilder res = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            res.append((i + 1)).append(".").append(task).append("\n");
        }
        return res.toString();
    }

    /**
     * Searches the task list for tasks whose description contains the given keyword, then prints those tasks.
     *
     * @param keyword The keyword to search for.
     */
    private String handleFindTask(String keyword) {
        List<Task> results = this.taskList.findTask(keyword);
        if (results.isEmpty()) {
            return "No matches found";
        }
        return "Here are the matching tasks in your list:\n" + results;
    }

    /**
     * Marks the specified task as done.
     *
     * @param index The display index of the task to be marked.
     */
    private String handleMarkTask(int index) throws GlendonException {
        Task task = this.taskList.markTask(index);
        return "Nice! I've marked this task as done:\n" + task;
    }

    /**
     * Marks the specified task as not done.
     *
     * @param index The display index of the task to be unmarked.
     */
    private String handleUnmarkTask(int index) throws GlendonException {
        Task task = this.taskList.unmarkTask(index);
        return "OK, I've marked this task as not done yet:\n" + task;
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param index The display index of the task to be deleted.
     */
    private String handleDeleteTask(int index) throws GlendonException {
        Task deletedTask = this.taskList.deleteTask(index);
        assert taskList.getCount() == taskList.getTasks().size() : "TaskList count mismatch after modification";
        return "Noted. I've removed this task:\n" + deletedTask
                + "\nNow you have " + this.taskList.getCount() + " tasks in the list.";
    }

    /**
     * Adds the given task to the task list.
     */
    private String handleAddTask(Task task) {
        this.taskList.addTask(task);
        assert taskList.getCount() == taskList.getTasks().size() : "TaskList count mismatch after modification";
        return "Got it. I've added this task:\n" + task
                + "\nNow you have " + this.taskList.getCount() + " tasks in the list.";
    }
}
