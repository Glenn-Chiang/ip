package glendon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import glendon.task.Deadline;
import glendon.task.Event;
import glendon.task.Task;
import glendon.task.ToDo;

public class Storage {
    private final String dataPath;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Storage(String dataPath) {
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
                // glendon.task.Deadline
                String dateStr = components[3];
                LocalDate date = LocalDate.parse(dateStr, dateFormat);
                task = new Deadline(description, date);
                break;
            case 5:
                // glendon.task.Event
                String startStr = components[3];
                String endStr = components[4];
                LocalDateTime start = LocalDateTime.parse(startStr, dateTimeFormat);
                LocalDateTime end = LocalDateTime.parse(endStr, dateTimeFormat);
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
                    writer.write(tasks.get(i).toStorageString());
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

    public static String serializeTodo(ToDo todo) {
        return "T | " + statusToString(todo.getStatus()) + " | " + todo.getDescription();
    }

    public static String serializeDeadline(Deadline deadline) {
        List<String> components = new ArrayList<>();
        components.add("D");
        components.add(statusToString(deadline.getStatus()));
        components.add(deadline.getDescription());
        components.add(deadline.getDate().format(dateFormat));
        return String.join(" | ", components);
    }

    public static String serializeEvent(Event event) {
        List<String> components = new ArrayList<>();
        components.add("E");
        components.add(statusToString(event.getStatus()));
        components.add(event.getDescription());
        components.add(event.getStart().format(dateTimeFormat));
        components.add(event.getEnd().format(dateTimeFormat));
        return String.join(" | ", components);
    }
}
