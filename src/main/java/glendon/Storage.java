package glendon;

import glendon.task.Deadline;
import glendon.task.Event;
import glendon.task.Task;
import glendon.task.ToDo;

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

public class Storage {
    private final String dataPath;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Storage(String dataPath) {
        this.dataPath = dataPath;
    }

    /**
     * Reads list of tasks from file and returns the list.
     *
     * @return Saved list of tasks, or empty list if no tasks were previously saved.
     * @throws GlendonException If path of storage file is invalid or the file is in an invalid format.
     */
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

    /**
     * Parses string stored in file into a ToDo, Deadline or Event Task and returns the Task.
     *
     * @param str The string to be parsed.
     * @throws GlendonException If string is not in a valid format.
     */
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
            String dateStr = components[3];
            LocalDate date = LocalDate.parse(dateStr, dateFormat);
            task = new Deadline(description, date);
            break;
        case 5:
            // Event
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

    /**
     * Writes the given list of tasks into storage file.
     *
     * @param tasks The list of tasks to be saved.
     * @throws GlendonException If IO error occurs.
     */
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

    /**
     * Converts the given boolean status into a string format to store in file.
     *
     * @param status Whether the task is done.
     * @return 1 for true and 0 for false.
     */
    private static String statusToString(boolean status) {
        return status ? "1" : "0";
    }

    /**
     * Converts a ToDo into a string format for file storage.
     *
     * @return The serialized ToDo.
     */
    public static String serializeTodo(ToDo todo) {
        return "T | " + statusToString(todo.getStatus()) + " | " + todo.getDescription();
    }

    /**
     * Converts a Deadline into a string format for file storage.
     *
     * @return The serialized Deadline.
     */
    public static String serializeDeadline(Deadline deadline) {
        List<String> components = new ArrayList<>();
        components.add("D");
        components.add(statusToString(deadline.getStatus()));
        components.add(deadline.getDescription());
        components.add(deadline.getDate().format(dateFormat));
        return String.join(" | ", components);
    }

    /**
     * Converts an Event into a string format for file storage.
     *
     * @return The serialized Event.
     */
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
