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
        return deletedTask;
    }

    /**
     * Searches the task list for tasks whose description contains the given keyword, then returns those tasks.
     *
     * @param keyword The keyword to search for.
     * @return The list of tasks whose description contains the given keyword.
     */
    public List<Task> findTask(String keyword) {
        List<Task> results = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getDescription().contains(keyword)) {
                results.add(task);
            }
        }
        return results;
    }
}
