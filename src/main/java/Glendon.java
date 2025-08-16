import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Glendon {
    private static String name = "Glendon";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

        System.out.println("Hello! I'm " + Glendon.name);
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            if (input.equals("list")) {
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);
                    System.out.println((i + 1) + "." + task);
                }
                continue;
            }

            String command = input.split(" ")[0];

            if (command.equals("mark") || command.equals("unmark")) {
                int index = Integer.parseInt(input.split(" ")[1]) - 1;
                Task task = tasks.get(index);
                if (command.equals("mark")) {
                    task.mark();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(task);
                } else {
                    task.unmark();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(task);
                }
            } else {
                Task task = null;
                String description;
                String[] segments;
                switch (command) {
                    case "todo":
                        segments = input.split(" ");
                        description = String.join(" ",
                                Arrays.copyOfRange(segments, 1, segments.length));
                        task = new ToDo(description);
                        break;
                    case "deadline":
                        segments = input.split("\\s*(deadline |/by )\\s*");
                        description = segments[1];
                        String date = segments[2];
                        task = new Deadline(description, date);
                        break;
                    case "event":
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
        }

        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
