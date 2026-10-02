package glados;

import glados.task.Task;

import java.util.ArrayList;

/**
 * Holds the user's tasks in order, with operations to add, remove, and look them up.
 *
 * <p>Tasks are stored in an ArrayList, which grows as needed, so there is no
 * fixed task limit or separate count to track. Indexes are 0-based.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list holding the given tasks, e.g. ones loaded from the data file.
     *
     * @param tasks the tasks to start with, in order.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at the given index and returns it.
     * Every later task moves up by one.
     *
     * @param index 0-based position of the task.
     * @return the task removed.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given 0-based index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns how many tasks are in the list.
     */
    public int size() {
        return tasks.size();
    }
}
