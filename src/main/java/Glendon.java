import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Glendon {
    private static String name = "Glendon";
    private static List<Task> tasks = new ArrayList<>();

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

    public static void main(String[] args) throws GlendonException {
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
                throw new GlendonException("Unknown command");
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
                    break;
                case UNMARK:
                    handleUnmark(Integer.parseInt(input.split(" ")[1]) - 1);
                    break;
                case DELETE:
                    handleDelete(Integer.parseInt(input.split(" ")[1]) - 1);
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    handleAddTask(input, commandKeyword);
                    break;
                default:
                    throw new GlendonException("Unknown command");
            }
        }
    }

    private static void handleList() {
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + "." + task);
        }
    }

    private static void handleMark(int index) {
        Task task = tasks.get(index);
        task.mark();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    private static void handleUnmark(int index) {
        Task task = tasks.get(index);
        task.unmark();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    private static void handleDelete(int index) {
        Task deletedTask = tasks.get(index);
        tasks.remove(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println(deletedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void handleAddTask(String input, String command) throws GlendonException {
        Task task = null;
        String description;
        String[] segments;
        switch (command) {
            case "todo":
                if (!validateTodoInput(input)) {
                    throw new GlendonException("Invalid todo format");
                }
                segments = input.split(" ");
                description = String.join(" ",
                        Arrays.copyOfRange(input.split(" "), 1, segments.length));
                task = new ToDo(description);
                break;
            case "deadline":
                if (!validateDeadlineInput(input)) {
                    throw new GlendonException("Invalid deadline format");
                }
                segments = input.split("\\s*(deadline |/by )\\s*");
                description = segments[1];
                String date = segments[2];
                task = new Deadline(description, date);
                break;
            case "event":
                if (!validateEventInput(input)) {
                    throw new GlendonException("Invalid event format");
                }
                segments = input.split("\\s*(event |/from |/to )\\s*");
                description = segments[1];
                String from = segments[2];
                String to = segments[3];
                task = new Event(description, from, to);
                break;

        }

        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
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
