package glendon;

import glendon.task.Task;

import java.util.Scanner;

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
        }
    }

    public static void main(String[] args) throws GlendonException {
        new Glendon(tasksDataPath).run();
    }

    private void saveTasks() throws GlendonException {
        this.storage.saveTasks(this.taskList.getTasks());
    }

    private void handleListTasks() {
        Ui.displayTasks(this.taskList.getTasks());
    }

    private void handleMarkTask(int index) {
        Task task = this.taskList.markTask(index);
        Ui.displayTaskMarked(task);
    }

    private void handleUnmarkTask(int index) {
        Task task = this.taskList.unmarkTask(index);
        Ui.displayTaskUnmarked(task);
    }

    private void handleDeleteTask(int index) {
        Task deletedTask = this.taskList.deleteTask(index);
        Ui.displayTaskDeleted(deletedTask, this.taskList.getTasks());
    }

    private void handleAddTask(Task task) {
        this.taskList.addTask(task);
        Ui.displayTaskAdded(task, this.taskList.getTasks());
    }
}
