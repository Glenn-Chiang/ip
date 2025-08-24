package glendon;

import java.util.Scanner;

import glendon.task.Task;

public class Glendon {

    public enum Command {
        BYE("bye"),
        LIST("list"),
        MARK("mark"),
        UNMARK("unmark"),
        DELETE("delete"),
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event");

        public final String keyword;

        Command(String keyword) {
            this.keyword = keyword;
        }
    }

    private static final String tasksDataPath = "./data/tasks.txt";

    private final Storage storage;
    private TaskList taskList;

    public Glendon(String storagePath) {
        storage = new Storage(storagePath);
        try {
            this.taskList = new TaskList(this.storage.loadTasks());
        } catch (GlendonException err) {
            Ui.displayLoadingError();
            this.taskList = new TaskList();
        }
    }

    /**
     * Reads user commands and executes responses.
     * @throws GlendonException
     */
    public void run() throws GlendonException {
        Ui.intro("Glendon");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            Command command = Parser.parseCommand(input);

            if (command == null) {
                Ui.displayUnknown();
                continue;
            }

            try {
                switch (command) {
                    case BYE:
                        Ui.exit();
                        scanner.close();
                        return;
                    case LIST:
                        handleListTasks();
                        break;
                    case MARK:
                        handleMarkTask(Parser.parseIndex(input));
                        saveTasks();
                        break;
                    case UNMARK:
                        handleUnmarkTask(Parser.parseIndex(input));
                        saveTasks();
                        break;
                    case DELETE:
                        handleDeleteTask(Parser.parseIndex(input));
                        saveTasks();
                        break;
                    case TODO:
                    case DEADLINE:
                    case EVENT:
                        handleAddTask(Parser.parseTask(input));
                        saveTasks();
                        break;
                    default:
                        Ui.displayUnknown();
                }
            } catch (GlendonException e) {
                Ui.display(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws GlendonException {
        new Glendon(tasksDataPath).run();
    }

    /**
     * Writes all tasks to storage file.
     *
     * @throws GlendonException
     */
    private void saveTasks() throws GlendonException {
        this.storage.saveTasks(this.taskList.getTasks());
    }

    /**
     * Displays list of tasks.
     */
    private void handleListTasks() {
        Ui.displayTasks(this.taskList.getTasks());
    }

    /**
     * Marks the specified task as done.
     * @param index The display index of the task to be marked.
     */
    private void handleMarkTask(int index) {
        Task task = this.taskList.markTask(index);
        Ui.displayTaskMarked(task);
    }

    /**
     * Marks the specified task as not done.
     * @param index The display index of the task to be unmarked.
     */
    private void handleUnmarkTask(int index) {
        Task task = this.taskList.unmarkTask(index);
        Ui.displayTaskUnmarked(task);
    }

    /**
     * Deletes the specified task from the task list.
     * @param index The display index of the task to be deleted.
     */
    private void handleDeleteTask(int index) {
        Task deletedTask = this.taskList.deleteTask(index);
        Ui.displayTaskDeleted(deletedTask, this.taskList.getTasks());
    }

    /**
     * Adds the given task to the task list.
     */
    private void handleAddTask(Task task) {
        this.taskList.addTask(task);
        Ui.displayTaskAdded(task, this.taskList.getTasks());
    }
}
