package glendon;

import java.util.ArrayList;
import java.util.List;

import glendon.task.Task;

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

    /**
     * Adds the given task to the list.
     *
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Marks the specified task as done.
     * @param index The display index of the task to be marked.
     */
    public Task markTask(int index) {
        Task task = this.tasks.get(index);
        task.mark();
        return task;
    }

    /**
     * Marks the specified task as not done.
     * @param index The display index of the task to be unmarked.
     */
    public Task unmarkTask(int index) {
        Task task = this.tasks.get(index);
        task.unmark();
        return task;
    }

    /**
     * Deletes the specified task from the task list.
     * @param index The display index of the task to be deleted.
     */
    public Task deleteTask(int index) {
        Task deletedTask = this.tasks.get(index);
        this.tasks.remove(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println(deletedTask);
        System.out.println("Now you have " + this.tasks.size() + " tasks in the list.");
        return deletedTask;
    }
}
