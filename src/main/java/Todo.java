/**
 * Represents a todo: a task with no date or time attached to it.
 */
public class Todo extends Task {

    /**
     * Creates a todo with the given description.
     *
     * @param description what the task is.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
