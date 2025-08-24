package glendon;

import glendon.task.Task;

import java.util.List;

public class Ui {
    public static void intro(String name) {
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
    }

    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public static void displayUnknown() {
        System.out.println("Unknown command");
    }

    public static void displayLoadingError() {
        System.out.println("Error loading tasks. Using empty task list.");
    }

    public static void displayTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + "." + task);
        }
    }

    public static void displayTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    public static void displayTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    public static void displayTaskDeleted(Task deletedTask, List<Task> tasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(deletedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    public static void displayTaskAdded(Task task, List<Task> tasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }
}
