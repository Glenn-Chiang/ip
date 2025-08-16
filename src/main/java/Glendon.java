import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Glendon {
    private static String name = "Glendon";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> inputs = new ArrayList<>();

        System.out.println("Hello! I'm " + Glendon.name);
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            if (input.equals("list")) {
                for (int i = 0; i < inputs.size(); i++) {
                    System.out.println((i + 1) + ". " + inputs.get(i));
                }
            } else {
                inputs.add(input);
                System.out.println("added: " + input);
            }
        }

        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
