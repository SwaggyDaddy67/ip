/**
 * Represents a single task in the task list.
 *
 * <p>A task holds its description, its type (todo, deadline, or event),
 * and whether it has been completed.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected String typeIcon;
    protected String by;
    protected String from;
    protected String to;

    /**
     * Creates a task, initially not done.
     *
     * @param description what the task is.
     * @param typeIcon "T" for todo, "D" for deadline, or "E" for event.
     * @param by when the task is due; only used when typeIcon is "D", otherwise null.
     * @param from when the event starts; only used when typeIcon is "E", otherwise null.
     * @param to when the event ends; only used when typeIcon is "E", otherwise null.
     */
    public Task(String description, String typeIcon, String by, String from, String to) {
        this.description = description;
        this.isDone = false;
        this.typeIcon = typeIcon;
        this.by = by;
        this.from = from;
        this.to = to;
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

    /**
     * Returns this task formatted as shown to the user, e.g. "[T][X] read book".
     */
    @Override
    public String toString() {
        String details = "";
        if (typeIcon.equals("D")) {
            details = " (by: " + by + ")";
        } else if (typeIcon.equals("E")) {
            details = " (from: " + from + " to: " + to + ")";
        }
        return "[" + typeIcon + "][" + getStatusIcon() + "] " + description + details;
    }
}
