import java.util.ArrayList;
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
            } else if (input.split(" ")[0].equals("mark") || input.split(" ")[0].equals("unmark")) {
                int index = Integer.parseInt(input.split(" ")[1]) - 1;
                String command = input.split(" ")[0];
                Task task = tasks.get(index);
                if (command.equals("mark")) {
                    task.mark();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(task);
                } else if (command.equals("unmark")) {
                    task.unmark();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(task);
                }
            } else {
                tasks.add(new Task(input));
                System.out.println("added: " + input);
            }
        }

        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
