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

    private Task taskFromString(String str) throws GlendonException {
        // TODO:
        return null;
    }

    private String taskToString(String str) {
        //TODO:
        return "";
    }

    public void saveTasks(List<Task> tasks) throws GlendonException {
        Path path = Path.of(dataPath);
        try {
            Files.createDirectories(path.getParent());
            try (FileWriter writer = new FileWriter(path.toFile())) {
                int numTasks = tasks.size();
                for (int i = 0; i < numTasks; i++) {
                    writer.write(tasks.get(i).toString());
                    if (i < numTasks - 1) {
                        writer.write(System.lineSeparator());
                    }
                }
            }
        } catch (IOException err) {
            throw new GlendonException("Error writing tasks to file: " + err);
        }
    }
}
