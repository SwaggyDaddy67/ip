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

    /** Prefix of the command that marks a task as done, e.g. "mark 2". */
    private static final String COMMAND_MARK = "mark ";

    /** Prefix of the command that marks a task as not done, e.g. "unmark 2". */
    private static final String COMMAND_UNMARK = "unmark ";

    /** Prefix of the command that adds a todo task. */
    private static final String COMMAND_TODO = "todo ";

    /** Prefix of the command that adds a deadline task. */
    private static final String COMMAND_DEADLINE = "deadline ";

    /** Prefix of the command that adds an event task. */
    private static final String COMMAND_EVENT = "event ";

    /** Separates a deadline's description from its due date, e.g. "return book /by Sunday". */
    private static final String DELIMITER_BY = " /by ";

    /** Separates an event's description from its start time, e.g. "meeting /from Mon 2pm". */
    private static final String DELIMITER_FROM = " /from ";

    /** Separates an event's start time from its end time, e.g. "Mon 2pm /to 4pm". */
    private static final String DELIMITER_TO = " /to ";

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
            } else if (input.startsWith(COMMAND_MARK)) {
                int index = Integer.parseInt(input.substring(COMMAND_MARK.length())) - 1;
                tasks[index].markAsDone();
                System.out.println(INDENT + "Nice! I've marked this task as done:");
                System.out.println(INDENT + "  " + tasks[index]);
            } else if (input.startsWith(COMMAND_UNMARK)) {
                int index = Integer.parseInt(input.substring(COMMAND_UNMARK.length())) - 1;
                tasks[index].markAsNotDone();
                System.out.println(INDENT + "OK, I've marked this task as not done yet:");
                System.out.println(INDENT + "  " + tasks[index]);
            } else if (input.startsWith(COMMAND_TODO)) {
                String description = input.substring(COMMAND_TODO.length());
                tasks[taskCount] = new Todo(description);
                taskCount++;
                System.out.println(INDENT + "Got it. I've added this task:");
                System.out.println(INDENT + "  " + tasks[taskCount - 1]);
                System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
            } else if (input.startsWith(COMMAND_DEADLINE)) {
                String details = input.substring(COMMAND_DEADLINE.length());
                int byIndex = details.indexOf(DELIMITER_BY);
                String description = details.substring(0, byIndex);
                String by = details.substring(byIndex + DELIMITER_BY.length());
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                System.out.println(INDENT + "Got it. I've added this task:");
                System.out.println(INDENT + "  " + tasks[taskCount - 1]);
                System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
            } else if (input.startsWith(COMMAND_EVENT)) {
                String details = input.substring(COMMAND_EVENT.length());
                int fromIndex = details.indexOf(DELIMITER_FROM);
                int toIndex = details.indexOf(DELIMITER_TO);
                String description = details.substring(0, fromIndex);
                String from = details.substring(fromIndex + DELIMITER_FROM.length(), toIndex);
                String to = details.substring(toIndex + DELIMITER_TO.length());
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                System.out.println(INDENT + "Got it. I've added this task:");
                System.out.println(INDENT + "  " + tasks[taskCount - 1]);
                System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list.");
            }

            System.out.println(DIVIDER);
            input = in.nextLine();
        }

        System.out.println(DIVIDER);
        System.out.println(INDENT + "Test concluded. Try not to disappoint me next time.");
        System.out.println(DIVIDER);
    }
}
