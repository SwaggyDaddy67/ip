package glados;

import glados.task.Task;

import java.util.Scanner;

/**
 * Handles all interaction with the user: reading commands and printing replies.
 *
 * <p>Every reply is indented and printed between divider lines, so keeping all
 * printing here means that formatting is decided in one place.
 */
public class Ui {

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

    private final Scanner in = new Scanner(System.in);

    /**
     * Shows the banner and greeting, wrapped in divider lines.
     */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        showMessage("Hello, I'm GLaDOS nice to... Oh, it's you.");
        showMessage("State your query. I have other tests to run.");
        showLine();
    }

    /**
     * Shows the farewell message, wrapped in divider lines.
     */
    public void showGoodbye() {
        showLine();
        showMessage("Test concluded. Try not to disappoint me next time.");
        showLine();
    }

    /**
     * Returns the next line the user enters.
     */
    public String readCommand() {
        return in.nextLine();
    }

    /**
     * Shows the divider line that separates blocks of replies.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Shows one line of reply, indented.
     */
    public void showMessage(String message) {
        System.out.println(INDENT + message);
    }

    /**
     * Shows an error message, e.g. explaining why a command was rejected.
     */
    public void showError(String message) {
        showMessage(message);
    }

    /**
     * Shows that the saved tasks could not be loaded, wrapped in divider lines.
     *
     * @param message explanation of what went wrong.
     */
    public void showLoadingError(String message) {
        showLine();
        showError(message);
        showLine();
    }

    /**
     * Warns that some lines of the save file were unreadable and were skipped,
     * wrapped in divider lines.
     *
     * @param corruptedLineCount how many lines were skipped.
     */
    public void showCorruptedLineWarning(int corruptedLineCount) {
        showLine();
        showMessage("Your save file is damaged. I skipped "
                + corruptedLineCount + " unreadable line(s).");
        showMessage("They will be gone for good the next time I save.");
        showLine();
    }

    /**
     * Shows every task, numbered from 1.
     */
    public void showTaskList(TaskList tasks) {
        showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Shows that a task was added, and how many tasks the list now holds.
     *
     * @param task the task added.
     * @param taskCount how many tasks are in the list after adding it.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:");
        showMessage("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Shows that a task was removed, and how many tasks the list now holds.
     *
     * @param task the task removed.
     * @param taskCount how many tasks are in the list after removing it.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showMessage("Noted. I've removed this task:");
        showMessage("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Shows that a task was marked as done.
     */
    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done:");
        showMessage("  " + task);
    }

    /**
     * Shows that a task was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet:");
        showMessage("  " + task);
    }

    private void showTaskCount(int taskCount) {
        showMessage("Now you have " + taskCount + " tasks in the list.");
    }
}
