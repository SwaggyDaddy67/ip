package glados;

import glados.task.Deadline;
import glados.task.Event;
import glados.task.Task;
import glados.task.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves the task list to a text file on disk, one task per line, and loads it back.
 *
 * <p>Each line uses the format produced by {@link Task#toFileString()},
 * e.g. "D | 0 | return book | Sunday".
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage that reads and writes the given file.
     *
     * @param filePath location of the data file, relative to the folder the program runs in.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Fills tasks with the tasks saved in the data file.
     *
     * <p>If the file does not exist yet (e.g. the first time the program runs),
     * nothing is loaded and the list starts empty.
     *
     * @param tasks the array to fill, starting from index 0.
     * @return how many tasks were loaded.
     * @throws GLaDOSException if the file exists but could not be read.
     */
    public int load(Task[] tasks) throws GLaDOSException {
        if (!Files.exists(filePath)) {
            return 0;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new GLaDOSException("I couldn't read your saved tasks from " + filePath + ".");
        }

        int taskCount = 0;
        for (String line : lines) {
            tasks[taskCount] = parseTask(line);
            taskCount++;
        }
        return taskCount;
    }

    /**
     * Turns one data file line, e.g. "D | 1 | return book | Sunday", back into a task.
     */
    private static Task parseTask(String line) {
        // split takes a regular expression, where | has a special meaning, so it is escaped.
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        String description = parts[2];

        Task task;
        if (type.equals("T")) {
            task = new Todo(description);
        } else if (type.equals("D")) {
            task = new Deadline(description, parts[3]);
        } else {
            task = new Event(description, parts[3], parts[4]);
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Writes every task to the data file, replacing its previous contents.
     *
     * <p>Creates the file's folder first if it does not exist yet.
     *
     * @param tasks the task list.
     * @param taskCount how many entries of tasks are in use.
     * @throws GLaDOSException if the file could not be written.
     */
    public void save(Task[] tasks, int taskCount) throws GLaDOSException {
        ArrayList<String> lines = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            lines.add(tasks[i].toFileString());
        }

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new GLaDOSException("I couldn't save your tasks to " + filePath + ".");
        }
    }
}
