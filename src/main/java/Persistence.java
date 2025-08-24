import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Persistence {
    private final String dataPath;

    public Persistence(String dataPath) {
        this.dataPath = dataPath;
    }

    public List<Task> loadTasks() throws GlendonException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(dataPath);
        if (!file.exists()) {
            return tasks;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String taskStr = scanner.nextLine();
                try {
                    Task task = taskFromString(taskStr);
                    tasks.add(task);
                } catch (GlendonException err) {
                    throw new GlendonException("Error reading malformed task from file: " + err);
                }
            }
            return tasks;
        } catch (FileNotFoundException err) {
            throw new GlendonException(err.toString());
        }
    }

    private static Task taskFromString(String str) throws GlendonException {
        String[] components = str.split(" \\| "); // split on " | "
        String status = components[1];
        String description = components[2];

        Task task = null;
        switch (components.length) {
        case 3:
            // Todo
            task = new ToDo(description);
            break;
        case 4:
            // Deadline
            String date = components[3];
            task = new Deadline(description, date);
            break;
        case 5:
            // Event
            String start = components[3];
            String end = components[4];
            task = new Event(description, start, end);
            break;
        default:
            throw new GlendonException("Malformed task");
        }
        if (status.equals("1")) {
            task.mark();
        } else if (!status.equals("0")) {
            throw new GlendonException("Invalid status");
        }
        return task;
    }

    public void saveTasks(List<Task> tasks) throws GlendonException {
        Path path = Path.of(dataPath);
        try {
            Files.createDirectories(path.getParent());
            try (FileWriter writer = new FileWriter(path.toFile())) {
                int numTasks = tasks.size();
                for (int i = 0; i < numTasks; i++) {
                    writer.write(tasks.get(i).toPersistenceString());
                    if (i < numTasks - 1) {
                        writer.write(System.lineSeparator());
                    }
                }
            }
        } catch (IOException err) {
            throw new GlendonException("Error writing tasks to file: " + err);
        }
    }

    private static String statusToString(boolean status) {
        return status ? "1" : "0";
    }

    public static String stringifyTodo(ToDo todo) {
        return "T | " + statusToString(todo.getStatus()) + " | " + todo.getDescription();
    }

    public static String stringifyDeadline(Deadline deadline) {
        List<String> components = new ArrayList<>();
        components.add("D");
        components.add(statusToString(deadline.getStatus()));
        components.add(deadline.getDescription());
        components.add(deadline.getDate());
        return String.join(" | ", components);
    }

    public static String stringifyEvent(Event event) {
        List<String> components = new ArrayList<>();
        components.add("E");
        components.add(statusToString(event.getStatus()));
        components.add(event.getDescription());
        components.add(event.getStart());
        components.add(event.getEnd());
        return String.join(" | ", components);
    }
}
