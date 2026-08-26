/**
 * Represents a single task in the task list.
 *
 * <p>A task holds its description and whether it has been completed.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description, initially not done.
     *
     * @param description what the task is.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of this task: "X" when done, a blank otherwise.
     */
    public String getStatusIcon() {
        if (isDone) {
            return "X";
        }
        return " ";
    }

    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }
}
