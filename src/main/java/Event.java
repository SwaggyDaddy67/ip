/**
 * Represents an event: a task that starts and ends at specific date/times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event with the given description, start time, and end time.
     *
     * @param description what the task is.
     * @param from when the event starts.
     * @param to when the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
