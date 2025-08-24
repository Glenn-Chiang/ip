package glendon;

import glendon.task.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this(new ArrayList<>());
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public int getCount() {
        return this.tasks.size();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public Task markTask(int index) {
        Task task = this.tasks.get(index);
        task.mark();
        return task;
    }

    public Task unmarkTask(int index) {
        Task task = this.tasks.get(index);
        task.unmark();
        return task;
    }

    public Task deleteTask(int index) {
        Task deletedTask = this.tasks.get(index);
        this.tasks.remove(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println(deletedTask);
        System.out.println("Now you have " + this.tasks.size() + " tasks in the list.");
        return deletedTask;
    }
}
