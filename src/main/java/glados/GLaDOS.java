package glados;

import glados.task.Task;

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

        while (!Parser.isExit(input)) {
            ui.showLine();

            try {
                String commandWord = Parser.parseCommandWord(input);
                if (commandWord.equals(Parser.COMMAND_LIST)) {
                    ui.showTaskList(tasks);
                } else if (commandWord.equals(Parser.COMMAND_MARK)) {
                    Task task = tasks.get(Parser.parseTaskIndex(input, commandWord, tasks.size()));
                    task.markAsDone();
                    ui.showTaskMarked(task);
                    storage.save(tasks);
                } else if (commandWord.equals(Parser.COMMAND_UNMARK)) {
                    Task task = tasks.get(Parser.parseTaskIndex(input, commandWord, tasks.size()));
                    task.markAsNotDone();
                    ui.showTaskUnmarked(task);
                    storage.save(tasks);
                } else if (commandWord.equals(Parser.COMMAND_DELETE)) {
                    Task task = tasks.delete(Parser.parseTaskIndex(input, commandWord, tasks.size()));
                    ui.showTaskDeleted(task, tasks.size());
                    storage.save(tasks);
                } else if (commandWord.equals(Parser.COMMAND_TODO)) {
                    addTask(tasks, Parser.parseTodo(input), ui);
                    storage.save(tasks);
                } else if (commandWord.equals(Parser.COMMAND_DEADLINE)) {
                    addTask(tasks, Parser.parseDeadline(input), ui);
                    storage.save(tasks);
                } else if (commandWord.equals(Parser.COMMAND_EVENT)) {
                    addTask(tasks, Parser.parseEvent(input), ui);
                    storage.save(tasks);
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
     * Appends the given task to the list and shows the confirmation.
     */
    private static void addTask(TaskList tasks, Task task, Ui ui) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }
}
