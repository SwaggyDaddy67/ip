package glados;

import glados.task.Deadline;
import glados.task.Event;
import glados.task.Todo;

/**
 * Makes sense of the commands the user enters.
 *
 * <p>Works out which command was entered, and turns its arguments into task
 * numbers or new tasks, rejecting input that is not in the expected format.
 */
public class Parser {

    /** Command that ends the conversation. */
    public static final String COMMAND_BYE = "bye";

    /** Command that lists every stored task. */
    public static final String COMMAND_LIST = "list";

    /** Command word that marks a task as done, e.g. "mark 2". */
    public static final String COMMAND_MARK = "mark";

    /** Command word that marks a task as not done, e.g. "unmark 2". */
    public static final String COMMAND_UNMARK = "unmark";

    /** Command word that removes a task from the list, e.g. "delete 2". */
    public static final String COMMAND_DELETE = "delete";

    /** Command word that adds a todo task, e.g. "todo read book". */
    public static final String COMMAND_TODO = "todo";

    /** Command word that adds a deadline task, e.g. "deadline return book /by Sunday". */
    public static final String COMMAND_DEADLINE = "deadline";

    /** Command word that adds an event task, e.g. "event meeting /from Mon 2pm /to 4pm". */
    public static final String COMMAND_EVENT = "event";

    /** Command words that are followed by arguments, in the order they are checked. */
    private static final String[] COMMANDS_WITH_ARGUMENTS = {
        COMMAND_MARK, COMMAND_UNMARK, COMMAND_DELETE, COMMAND_TODO, COMMAND_DEADLINE, COMMAND_EVENT
    };

    /** Separates a deadline's description from its due date. */
    private static final String DELIMITER_BY = "/by";

    /** Separates an event's description from its start time. */
    private static final String DELIMITER_FROM = "/from";

    /** Separates an event's start time from its end time. */
    private static final String DELIMITER_TO = "/to";

    /** Separates fields in the data file, so it cannot appear inside a task. */
    private static final String RESERVED_CHARACTER = "|";

    /**
     * Returns true if the input is the command that ends the conversation.
     */
    public static boolean isExit(String input) {
        return input.equals(COMMAND_BYE);
    }

    /**
     * Returns the command word of the given input, e.g. "mark" for "mark 2".
     *
     * <p>"list" must be entered on its own. Other command words must be the whole
     * input or be followed by a space, so "todox" is not taken as "todo".
     *
     * @param input the full command entered by the user.
     * @return one of the COMMAND_ constants, other than COMMAND_BYE.
     * @throws GLaDOSException if the input does not start with a known command.
     */
    public static String parseCommandWord(String input) throws GLaDOSException {
        if (input.equals(COMMAND_LIST)) {
            return COMMAND_LIST;
        }
        for (String commandWord : COMMANDS_WITH_ARGUMENTS) {
            if (input.equals(commandWord) || input.startsWith(commandWord + " ")) {
                return commandWord;
            }
        }
        throw new GLaDOSException("I have no idea what that was. Try one of: "
                + "list, todo, deadline, event, mark, unmark, delete, bye.");
    }

    /**
     * Parses the task number after a command word, e.g. "mark 2", into a 0-based index.
     *
     * @param input the full command entered by the user.
     * @param commandWord the command word to strip off, e.g. "mark".
     * @param taskCount how many tasks exist, used to check the number is in range.
     * @return the 0-based index of the task.
     * @throws GLaDOSException if no number was given, it is not a number, or no such task exists.
     */
    public static int parseTaskIndex(String input, String commandWord, int taskCount)
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
     * Parses a todo command into a Todo task.
     *
     * @param input the full command, e.g. "todo read book".
     * @return the new todo.
     * @throws GLaDOSException if no description was given, or it contains a reserved character.
     */
    public static Todo parseTodo(String input) throws GLaDOSException {
        checkNoReservedCharacter(input);
        String description = input.substring(COMMAND_TODO.length()).trim();
        if (description.isEmpty()) {
            throw new GLaDOSException("A todo with no description. Try again, with words this time.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline command into a Deadline task.
     *
     * @param input the full command, e.g. "deadline return book /by Sunday".
     * @return the new deadline.
     * @throws GLaDOSException if the /by is missing, either part around it is empty,
     *         or the input contains a reserved character.
     */
    public static Deadline parseDeadline(String input) throws GLaDOSException {
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
     * @return the new event.
     * @throws GLaDOSException if /from or /to is missing or out of order, any part is empty,
     *         or the input contains a reserved character.
     */
    public static Event parseEvent(String input) throws GLaDOSException {
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
}
