package glados.task;

/**
 * Represents an event: a task that starts and ends at specific date/times.
 */
public class Event extends Task {
    protected TaskDateTime from;
    protected TaskDateTime to;

    /**
     * Creates an event with the given description, start, and end.
     *
     * @param description what the task is.
     * @param from when the event starts.
     * @param to when the event ends, not before it starts.
     */
    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event as a data file line,
     * e.g. "E | 0 | meeting | 2019-08-06 1400 | 2019-08-06 1600".
     */
    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from.toFileString() + " | " + to.toFileString();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
