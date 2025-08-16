import java.util.Scanner;

public class Glendon {
    private static String name = "Glendon";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! I'm " + Glendon.name);
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            System.out.println(input);
        }

        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
