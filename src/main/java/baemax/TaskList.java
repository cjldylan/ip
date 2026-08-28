package baemax;

import java.util.ArrayList;
import java.util.List;

/**
 * The list of tasks the user is tracking, together with the operations the
 * chatbot performs on it. Wrapping the {@link ArrayList} lets callers work in
 * terms of tasks rather than list mechanics.
 */
public class TaskList {
    /** Tasks in the order the user entered them. */
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list seeded with existing tasks, such as those loaded
     * from the save file.
     *
     * @param initialTasks the tasks to start with
     */
    public TaskList(List<Task> initialTasks) {
        this.tasks = new ArrayList<>(initialTasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at the given one-based position.
     *
     * @param oneBasedIndex the task number shown to the user
     * @return the removed task
     */
    public Task remove(int oneBasedIndex) {
        return tasks.remove(oneBasedIndex - 1);
    }

    /**
     * Returns the task at the given one-based position.
     *
     * @param oneBasedIndex the task number shown to the user
     * @return the task at that position
     */
    public Task get(int oneBasedIndex) {
        return tasks.get(oneBasedIndex - 1);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks as a list for reading, such as when saving or
     * displaying them. The returned list is the backing list and is not meant
     * to be modified directly.
     *
     * @return the tasks in list order
     */
    public List<Task> asList() {
        return tasks;
    }
}
