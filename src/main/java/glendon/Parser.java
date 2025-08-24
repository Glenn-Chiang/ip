package glendon;

import glendon.task.Deadline;
import glendon.task.Event;
import glendon.task.Task;
import glendon.task.ToDo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Parser {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Extracts and returns the type of command from the input string.
     */
    public static Glendon.Command parseCommand(String input) {
        String commandKeyword = input.split(" ")[0];
        return Arrays.stream(Glendon.Command.values())
                .filter(cmd -> cmd.keyword.equals(commandKeyword))
                .findFirst()
                .orElse(null);
    }

    /**
     * Extracts and returns the specified index from the input string.
     * Commands MARK, UNMARK and DELETE require an index to be specified as the second argument in the input string.
     */
    public static int parseIndex(String input) {
        return Integer.parseInt(input.split(" ")[1]) - 1;
    }

    /**
     * Parses the input string into a Task and returns the Task.
     * @throws GlendonException If input is invalid.
     */
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
            try {
                LocalDate date = LocalDate.parse(dateStr, dateFormat);
                task = new Deadline(description, date);
            } catch (DateTimeParseException e) {
                throw new GlendonException("Invalid deadline date format");
            }
            break;
        case "event":
            if (!validateEventInput(input)) {
                throw new GlendonException("Invalid event format");
            }
            segments = input.split("\\s*(event |/from |/to )\\s*");
            description = segments[1];
            String startStr = segments[2];
            String endStr = segments[3];
            try {
                LocalDateTime start = LocalDateTime.parse(startStr, dateTimeFormat);
                LocalDateTime end = LocalDateTime.parse(endStr, dateTimeFormat);
                task = new Event(description, start, end);
            } catch (DateTimeParseException e) {
                throw new GlendonException("Invalid event start or end datetime format");
            }
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
        return validateInput(Pattern.compile("^todo\\s\\S.*$"), input);
    }

    private static boolean validateDeadlineInput(String input) {
        return validateInput(Pattern.compile("^deadline\\s+\\S.*\\s+/by\\s+\\S.*$"), input);
    }

    private static boolean validateEventInput(String input) {
        return validateInput(Pattern.compile("^event\\s+\\S.*\\s+/from\\s+\\S.*\\s+/to\\s+\\S.*$"), input);
    }
}
