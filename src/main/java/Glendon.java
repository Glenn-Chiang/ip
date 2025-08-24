import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Glendon {
    private static final String name = "Glendon";

    private enum Command {
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

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private final Storage storage;
    private List<Task> tasks;

    public Glendon(String storagePath) {
        storage = new Storage(storagePath);
        try {
            this.tasks = this.storage.loadTasks();
        } catch (GlendonException err) {
            System.out.println("Error loading tasks. Using empty task list.");
            this.tasks = new ArrayList<>();
        }
    }

    public void run() throws GlendonException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! I'm " + Glendon.name);
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();
            String commandKeyword = input.split(" ")[0];
            Command command = Arrays.stream(Command.values())
                    .filter(cmd -> cmd.keyword.equals(commandKeyword))
                    .findFirst()
                    .orElse(null);

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
                handleMark(Integer.parseInt(input.split(" ")[1]) - 1);
                saveTasks();
                break;
            case UNMARK:
                handleUnmark(Integer.parseInt(input.split(" ")[1]) - 1);
                saveTasks();
                break;
            case DELETE:
                handleDelete(Integer.parseInt(input.split(" ")[1]) - 1);
                saveTasks();
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
                handleAddTask(input);
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
        this.storage.saveTasks(this.tasks);
    }

    private void handleList() {
        for (int i = 0; i < this.tasks.size(); i++) {
            Task task = this.tasks.get(i);
            System.out.println((i + 1) + "." + task);
        }
    }

    private void handleMark(int index) {
        Task task = this.tasks.get(index);
        task.mark();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    private void handleUnmark(int index) {
        Task task = this.tasks.get(index);
        task.unmark();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    private void handleDelete(int index) {
        Task deletedTask = this.tasks.get(index);
        this.tasks.remove(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println(deletedTask);
        System.out.println("Now you have " + this.tasks.size() + " tasks in the list.");
    }

    private void handleAddTask(String input) throws GlendonException {
        try {
            Task task = taskFromString(input);
            this.tasks.add(task);
            System.out.println("Got it. I've added this task:");
            System.out.println(task);
            System.out.println("Now you have " + this.tasks.size() + " tasks in the list.");
        } catch (GlendonException e) {
            System.out.println(e.toString());
        }
    }

    private Task taskFromString(String str) throws GlendonException {
        Task task = null;
        String taskType = str.split(" ")[0];
        String description;
        String[] segments;
        switch (taskType) {
        case "todo":
            if (!validateTodoInput(str)) {
                throw new GlendonException("Invalid todo format");
            }
            segments = str.split(" ");
            description = String.join(" ",
                    Arrays.copyOfRange(str.split(" "), 1, segments.length));
            task = new ToDo(description);
            break;
        case "deadline":
            if (!validateDeadlineInput(str)) {
                throw new GlendonException("Invalid deadline format");
            }
            segments = str.split("\\s*(deadline |/by )\\s*");
            description = segments[1];
            String dateStr = segments[2];
            LocalDate date = LocalDate.parse(dateStr, dateFormat);
            task = new Deadline(description, date);
            break;
        case "event":
            if (!validateEventInput(str)) {
                throw new GlendonException("Invalid event format");
            }
            segments = str.split("\\s*(event |/from |/to )\\s*");
            description = segments[1];
            String startStr = segments[2];
            String endStr = segments[3];
            LocalDateTime start = LocalDateTime.parse(startStr, dateTimeFormat);
            LocalDateTime end = LocalDateTime.parse(endStr, dateTimeFormat);
            task = new Event(description, start, end);
            break;
        default:
            throw new GlendonException("Invalid task format");
        }
        return task;
    }

    private static boolean validateInput(Pattern pattern, String input) {
        return pattern.matcher(input).matches();
    }

    private static boolean validateTodoInput(String input) {
        return validateInput(Pattern.compile("^todo\\s+.+$"), input);
    }

    private static boolean validateDeadlineInput(String input) {
        return validateInput(Pattern.compile("^deadline\\s+.+\\s+/by\\s+.+$"), input);
    }

    private static boolean validateEventInput(String input) {
        return validateInput(Pattern.compile("^event\\s+.+\\s+/from\\s+.+\\s+/to\\s+.+$"), input);
    }
}
