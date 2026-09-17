import java.util.Scanner;

/**
 * Runs GLaDOS, a command line chatbot that keeps a simple list of tasks.
 *
 * <p>Supports adding a todo, deadline, or event task, listing all tasks, and
 * marking a task as done or not done. The conversation ends when the user
 * enters the exit command.
 */
public class GLaDOS {

    /** Maximum number of tasks that can be stored. */
    private static final int MAX_TASKS = 100;

    /** Command that ends the conversation. */
    private static final String COMMAND_BYE = "bye";

    /** Command that lists every stored task. */
    private static final String COMMAND_LIST = "list";

    /** Command word that marks a task as done, e.g. "mark 2". */
    private static final String COMMAND_MARK = "mark";

    /** Command word that marks a task as not done, e.g. "unmark 2". */
    private static final String COMMAND_UNMARK = "unmark";

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

    /** Indentation placed before every line of GLaDOS's replies. */
    private static final String INDENT = "     ";

    /** Horizontal divider that wraps each block of replies. */
    private static final String DIVIDER =
            "    ____________________________________________________________";

    /** ASCII art banner shown at startup, already indented. */
    private static final String BANNER = "        ________          ____  ____  _____\n"
            + "       / ____/ /   ____ _/ __ \\/ __ \\/ ___/\n"
            + "      / / __/ /   / __ `/ / / / / / /\\__ \\ \n"
            + "     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / \n"
            + "     \\____/_____/\\__,_/_____/\\____//____/  \n";

    /**
     * Runs the chatbot until the user enters the exit command.
     *
     * @param args command line arguments, not used.
     */
    public static void main(String[] args) {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println(INDENT + "Hello, I'm GLaDOS nice to... Oh, it's you.");
        System.out.println(INDENT + "State your query. I have other tests to run.");
        System.out.println(DIVIDER);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        Scanner in = new Scanner(System.in);
        String input = in.nextLine();

        while (!input.equals(COMMAND_BYE)) {
            System.out.println(DIVIDER);

            if (input.equals(COMMAND_LIST)) {
                System.out.println(INDENT + "Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(INDENT + (i + 1) + "." + tasks[i]);
                }
            } else if (input.equals(COMMAND_MARK) || input.startsWith(COMMAND_MARK + " ")) {
                int index = parseTaskIndex(input, COMMAND_MARK, taskCount);
                if (index != -1) {
                    tasks[index].markAsDone();
                    System.out.println(INDENT + "Nice! I've marked this task as done:");
                    System.out.println(INDENT + "  " + tasks[index]);
                }
            } else if (input.equals(COMMAND_UNMARK) || input.startsWith(COMMAND_UNMARK + " ")) {
                int index = parseTaskIndex(input, COMMAND_UNMARK, taskCount);
                if (index != -1) {
                    tasks[index].markAsNotDone();
                    System.out.println(INDENT + "OK, I've marked this task as not done yet:");
                    System.out.println(INDENT + "  " + tasks[index]);
                }
            } else if (input.equals(COMMAND_TODO) || input.startsWith(COMMAND_TODO + " ")) {
                String description = input.substring(COMMAND_TODO.length()).trim();
                if (description.isEmpty()) {
                    System.out.println(INDENT
                            + "A todo with no description. Try again, with words this time.");
                } else {
                    taskCount = addTask(tasks, taskCount, new Todo(description));
                }
            } else if (input.equals(COMMAND_DEADLINE) || input.startsWith(COMMAND_DEADLINE + " ")) {
                Deadline deadline = parseDeadline(input);
                if (deadline != null) {
                    taskCount = addTask(tasks, taskCount, deadline);
                }
            } else if (input.equals(COMMAND_EVENT) || input.startsWith(COMMAND_EVENT + " ")) {
                Event event = parseEvent(input);
                if (event != null) {
                    taskCount = addTask(tasks, taskCount, event);
                }
            } else {
                System.out.println(INDENT + "I have no idea what that was. Try one of: "
                        + "list, todo, deadline, event, mark, unmark, bye.");
            }

            System.out.println(DIVIDER);
            input = in.nextLine();
        }

        System.out.println(DIVIDER);
        System.out.println(INDENT + "Test concluded. Try not to disappoint me next time.");
        System.out.println(DIVIDER);
    }

    /**
     * Parses the task number after a command word, e.g. "mark 2", into a 0-based index.
     * Prints an error and returns -1 if no number was given, it is not a number, or no
     * such task exists.
     */
    private static int parseTaskIndex(String input, String commandWord, int taskCount) {
        String argument = input.substring(commandWord.length()).trim();
        if (argument.isEmpty()) {
            System.out.println(INDENT + "Which task? Give me a number, like " + commandWord + " 2.");
            return -1;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "\"" + argument + "\" is not a task number.");
            return -1;
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            System.out.println(INDENT + "There is no task " + taskNumber
                    + ". Your list has " + taskCount + ".");
            return -1;
        }
        return taskNumber - 1;
    }

    /**
     * Stores the given task in the next free slot and prints the confirmation.
     *
     * @return the updated task count, since Java passes taskCount by value.
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) {
        tasks[taskCount] = task;
        taskCount++;
        System.out.println(INDENT + "Got it. I've added this task:");
        System.out.println(INDENT + "  " + task);
        System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
        return taskCount;
    }

    /**
     * Parses a deadline command into a Deadline task.
     * Prints an error and returns null if /by is missing, or either part around it is empty.
     */
    private static Deadline parseDeadline(String input) {
        String details = input.substring(COMMAND_DEADLINE.length()).trim();
        int byIndex = details.indexOf(DELIMITER_BY);
        if (byIndex == -1) {
            System.out.println(INDENT + "A deadline needs a /by. Try: deadline return book /by Sunday.");
            return null;
        }

        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + DELIMITER_BY.length()).trim();
        if (description.isEmpty()) {
            System.out.println(INDENT + "A deadline with no description. What am I meant to track?");
            return null;
        }
        if (by.isEmpty()) {
            System.out.println(INDENT + "You left the /by empty. When is this due?");
            return null;
        }
        return new Deadline(description, by);
    }

    /**
     * Parses an event command into an Event task.
     * Prints an error and returns null if /from or /to is missing or out of order, or any
     * part around them is empty.
     */
    private static Event parseEvent(String input) {
        String details = input.substring(COMMAND_EVENT.length()).trim();
        int fromIndex = details.indexOf(DELIMITER_FROM);
        int toIndex = details.indexOf(DELIMITER_TO);
        if (fromIndex == -1 || toIndex == -1) {
            System.out.println(INDENT + "An event needs both a /from and a /to. "
                    + "Try: event meeting /from Mon 2pm /to 4pm.");
            return null;
        }
        if (toIndex < fromIndex) {
            System.out.println(INDENT + "The /to has to come after the /from.");
            return null;
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + DELIMITER_FROM.length(), toIndex).trim();
        String to = details.substring(toIndex + DELIMITER_TO.length()).trim();
        if (description.isEmpty()) {
            System.out.println(INDENT + "An event with no description. What am I meant to track?");
            return null;
        }
        if (from.isEmpty() || to.isEmpty()) {
            System.out.println(INDENT + "An event needs both a start and an end time.");
            return null;
        }
        return new Event(description, from, to);
    }
}
