package glados;

import glados.task.Deadline;
import glados.task.Event;
import glados.task.Task;
import glados.task.Todo;

import java.nio.file.Path;

/**
 * Runs GLaDOS, a command line chatbot that keeps a simple list of tasks.
 *
 * <p>Supports adding a todo, deadline, or event task, listing all tasks,
 * marking a task as done or not done, and deleting a task. The task list is
 * loaded from disk at startup and saved after every change. The conversation
 * ends when the user enters the exit command.
 */
public class GLaDOS {

    /**
     * Where the task list is saved: data/glados.txt, relative to the folder the program runs in.
     * Path.of joins the parts with the separator of the current OS, so this works on any OS.
     */
    private static final Path DATA_FILE = Path.of("data", "glados.txt");

    /** Command that ends the conversation. */
    private static final String COMMAND_BYE = "bye";

    /** Command that lists every stored task. */
    private static final String COMMAND_LIST = "list";

    /** Command word that marks a task as done, e.g. "mark 2". */
    private static final String COMMAND_MARK = "mark";

    /** Command word that marks a task as not done, e.g. "unmark 2". */
    private static final String COMMAND_UNMARK = "unmark";

    /** Command word that removes a task from the list, e.g. "delete 2". */
    private static final String COMMAND_DELETE = "delete";

    /** Command word that adds a todo task, e.g. "todo read book". */
    private static final String COMMAND_TODO = "todo";

    /** Command word that adds a deadline task, e.g. "deadline return book /by Sunday". */
    private static final String COMMAND_DEADLINE = "deadline";

    /** Command word that adds an event task, e.g. "event meeting /from Mon 2pm /to 4pm". */
    private static final String COMMAND_EVENT = "event";

    /** Separates a deadline's description from its due date. */
    private static final String DELIMITER_BY = "/by";

    /** Separates an event's description from its start time. */
    private static final String DELIMITER_FROM = "/from";

    /** Separates an event's start time from its end time. */
    private static final String DELIMITER_TO = "/to";

    /** Separates fields in the data file, so it cannot appear inside a task. */
    private static final String RESERVED_CHARACTER = "|";

    /**
     * Runs the chatbot until the user enters the exit command.
     *
     * @param args command line arguments, not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(DATA_FILE);
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (GLaDOSException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new TaskList();
        }
        if (storage.getCorruptedLineCount() > 0) {
            ui.showCorruptedLineWarning(storage.getCorruptedLineCount());
        }

        String input = ui.readCommand();

        while (!input.equals(COMMAND_BYE)) {
            ui.showLine();

            try {
                if (input.equals(COMMAND_LIST)) {
                    ui.showTaskList(tasks);
                } else if (input.equals(COMMAND_MARK) || input.startsWith(COMMAND_MARK + " ")) {
                    Task task = tasks.get(parseTaskIndex(input, COMMAND_MARK, tasks.size()));
                    task.markAsDone();
                    ui.showTaskMarked(task);
                    storage.save(tasks);
                } else if (input.equals(COMMAND_UNMARK) || input.startsWith(COMMAND_UNMARK + " ")) {
                    Task task = tasks.get(parseTaskIndex(input, COMMAND_UNMARK, tasks.size()));
                    task.markAsNotDone();
                    ui.showTaskUnmarked(task);
                    storage.save(tasks);
                } else if (input.equals(COMMAND_DELETE) || input.startsWith(COMMAND_DELETE + " ")) {
                    Task task = tasks.delete(parseTaskIndex(input, COMMAND_DELETE, tasks.size()));
                    ui.showTaskDeleted(task, tasks.size());
                    storage.save(tasks);
                } else if (input.equals(COMMAND_TODO) || input.startsWith(COMMAND_TODO + " ")) {
                    addTask(tasks, parseTodo(input), ui);
                    storage.save(tasks);
                } else if (input.equals(COMMAND_DEADLINE)
                        || input.startsWith(COMMAND_DEADLINE + " ")) {
                    addTask(tasks, parseDeadline(input), ui);
                    storage.save(tasks);
                } else if (input.equals(COMMAND_EVENT) || input.startsWith(COMMAND_EVENT + " ")) {
                    addTask(tasks, parseEvent(input), ui);
                    storage.save(tasks);
                } else {
                    throw new GLaDOSException("I have no idea what that was. Try one of: "
                            + "list, todo, deadline, event, mark, unmark, delete, bye.");
                }
            } catch (GLaDOSException e) {
                ui.showError(e.getMessage());
            }

            ui.showLine();
            input = ui.readCommand();
        }

        ui.showGoodbye();
    }

    /**
     * Parses the task number after a command word, e.g. "mark 2", into a 0-based index.
     *
     * @param input the full command entered by the user.
     * @param commandWord the command word to strip off, e.g. "mark".
     * @param taskCount how many tasks exist, used to check the number is in range.
     * @throws GLaDOSException if no number was given, it is not a number, or no such task exists.
     */
    private static int parseTaskIndex(String input, String commandWord, int taskCount)
            throws GLaDOSException {
        String argument = input.substring(commandWord.length()).trim();
        if (argument.isEmpty()) {
            throw new GLaDOSException("Which task? Give me a number, like " + commandWord + " 2.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new GLaDOSException("\"" + argument + "\" is not a task number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new GLaDOSException("There is no task " + taskNumber
                    + ". Your list has " + taskCount + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Rejects task input containing the character used to separate fields in the data file.
     *
     * @throws GLaDOSException if input contains that character.
     */
    private static void checkNoReservedCharacter(String input) throws GLaDOSException {
        if (input.contains(RESERVED_CHARACTER)) {
            throw new GLaDOSException("The " + RESERVED_CHARACTER
                    + " character is reserved for my records. Leave it out.");
        }
    }

    /**
     * Parses a todo command into a Todo task.
     *
     * @param input the full command, e.g. "todo read book".
     * @throws GLaDOSException if no description was given, or it contains a reserved character.
     */
    private static Todo parseTodo(String input) throws GLaDOSException {
        checkNoReservedCharacter(input);
        String description = input.substring(COMMAND_TODO.length()).trim();
        if (description.isEmpty()) {
            throw new GLaDOSException("A todo with no description. Try again, with words this time.");
        }
        return new Todo(description);
    }

    /**
     * Appends the given task to the list and shows the confirmation.
     */
    private static void addTask(TaskList tasks, Task task, Ui ui) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Parses a deadline command into a Deadline task.
     *
     * @param input the full command, e.g. "deadline return book /by Sunday".
     * @throws GLaDOSException if the /by is missing, either part around it is empty,
     *         or the input contains a reserved character.
     */
    private static Deadline parseDeadline(String input) throws GLaDOSException {
        checkNoReservedCharacter(input);
        String details = input.substring(COMMAND_DEADLINE.length()).trim();
        int byIndex = details.indexOf(DELIMITER_BY);
        if (byIndex == -1) {
            throw new GLaDOSException("A deadline needs a /by. "
                    + "Try: deadline return book /by Sunday.");
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + DELIMITER_BY.length()).trim();
        if (description.isEmpty()) {
            throw new GLaDOSException("A deadline with no description. What am I meant to track?");
        }
        if (by.isEmpty()) {
            throw new GLaDOSException("You left the /by empty. When is this due?");
        }
        return new Deadline(description, by);
    }

    /**
     * Parses an event command into an Event task.
     *
     * @param input the full command, e.g. "event meeting /from Mon 2pm /to 4pm".
     * @throws GLaDOSException if /from or /to is missing or out of order, any part is empty,
     *         or the input contains a reserved character.
     */
    private static Event parseEvent(String input) throws GLaDOSException {
        checkNoReservedCharacter(input);
        String details = input.substring(COMMAND_EVENT.length()).trim();
        int fromIndex = details.indexOf(DELIMITER_FROM);
        int toIndex = details.indexOf(DELIMITER_TO);
        if (fromIndex == -1 || toIndex == -1) {
            throw new GLaDOSException("An event needs both a /from and a /to. "
                    + "Try: event meeting /from Mon 2pm /to 4pm.");
        }
        if (toIndex < fromIndex) {
            throw new GLaDOSException("The /to has to come after the /from.");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + DELIMITER_FROM.length(), toIndex).trim();
        String to = details.substring(toIndex + DELIMITER_TO.length()).trim();
        if (description.isEmpty()) {
            throw new GLaDOSException("An event with no description. What am I meant to track?");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new GLaDOSException("An event needs both a start and an end time.");
        }
        return new Event(description, from, to);
    }
}
