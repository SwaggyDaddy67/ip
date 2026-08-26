import java.util.Scanner;

/**
 * Runs GLaDOS, a command line chatbot that keeps a simple list of tasks.
 *
 * <p>Supports adding a task, listing all tasks, and marking a task as done or
 * not done. The conversation ends when the user enters the exit command.
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
                    System.out.println(INDENT + (i + 1) + ".[" + tasks[i].getStatusIcon()
                            + "] " + tasks[i].getDescription());
                }
            } else if (input.startsWith(COMMAND_MARK)) {
                int index = Integer.parseInt(input.substring(COMMAND_MARK.length())) - 1;
                tasks[index].markAsDone();
                System.out.println(INDENT + "Nice! I've marked this task as done:");
                System.out.println(INDENT + "  [X] " + tasks[index].getDescription());
            } else if (input.startsWith(COMMAND_UNMARK)) {
                int index = Integer.parseInt(input.substring(COMMAND_UNMARK.length())) - 1;
                tasks[index].markAsNotDone();
                System.out.println(INDENT + "OK, I've marked this task as not done yet:");
                System.out.println(INDENT + "  [ ] " + tasks[index].getDescription());
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println(INDENT + "added: " + input);
            }

            System.out.println(DIVIDER);
            input = in.nextLine();
        }

        System.out.println(DIVIDER);
        System.out.println(INDENT + "Test concluded. Try not to disappoint me next time.");
        System.out.println(DIVIDER);
    }
}
