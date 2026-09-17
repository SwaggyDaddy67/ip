package glados.task;

/**
 * Represents a deadline: a task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a deadline with the given description and due date.
     *
     * @param description what the task is.
     * @param by when the task is due.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline as a data file line, e.g. "D | 0 | return book | Sunday".
     */
    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
