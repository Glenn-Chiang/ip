import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Parser {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public static Glendon.Command parseCommand(String input) {
        String commandKeyword = input.split(" ")[0];
        return Arrays.stream(Glendon.Command.values())
                .filter(cmd -> cmd.keyword.equals(commandKeyword))
                .findFirst()
                .orElse(null);
    }

    public static int parseIndex(String input) {
        return Integer.parseInt(input.split(" ")[1]) - 1;
    }

    public static Task parseTask(String input) throws GlendonException {
        Task task = null;
        String taskType = input.split(" ")[0];
        String description;
        String[] segments;
        switch (taskType) {
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
            String dateStr = segments[2];
            LocalDate date = LocalDate.parse(dateStr, dateFormat);
            task = new Deadline(description, date);
            break;
        case "event":
            if (!validateEventInput(input)) {
                throw new GlendonException("Invalid event format");
            }
            segments = input.split("\\s*(event |/from |/to )\\s*");
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
