import java.util.Scanner;

public class Glendon {
    private static final String name = "Glendon";

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
            System.out.println("Error loading tasks. Using empty task list.");
            this.taskList = new TaskList();
        }
    }

    public void run() throws GlendonException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! I'm " + Glendon.name);
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();
            Command command = Parser.parseCommand(input);

            if (command == null) {
                System.out.println("Unknown command");
                continue;
            }

            switch (command) {
            case BYE:
                System.out.println("Bye. Hope to see you again soon!");
                scanner.close();
                return;
            case LIST:
                handleList();
                break;
            case MARK:
                handleMark(Parser.parseIndex(input));
                saveTasks();
                break;
            case UNMARK:
                handleUnmark(Parser.parseIndex(input));
                saveTasks();
                break;
            case DELETE:
                handleDelete(Parser.parseIndex(input));
                saveTasks();
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
                handleAddTask(Parser.parseTask(input));
                saveTasks();
                break;
            default:
                System.out.println("Unknown command");
            }
        }
    }

    public static void main(String[] args) throws GlendonException {
        new Glendon(tasksDataPath).run();
    }

    private void saveTasks() throws GlendonException {
        this.storage.saveTasks(this.taskList.getTasks());
    }

    private void handleList() {
        this.taskList.list();
    }

    private void handleMark(int index) {
        Task task = this.taskList.markTask(index);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    private void handleUnmark(int index) {
        Task task = this.taskList.unmarkTask(index);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    private void handleDelete(int index) {
        Task deletedTask = this.taskList.deleteTask(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println(deletedTask);
        System.out.println("Now you have " + this.taskList.getCount() + " tasks in the list.");
    }

    private void handleAddTask(Task task) {
        this.taskList.addTask(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + this.taskList.getCount() + " tasks in the list.");
    }
}
