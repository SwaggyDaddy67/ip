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

    /** How many lines the last load skipped because they were not in the expected format. */
    private int corruptedLineCount = 0;

    /**
     * Creates a storage that reads and writes the given file.
     *
     * @param filePath location of the data file, relative to the folder the program runs in.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the tasks saved in the data file.
     *
     * <p>If the file does not exist yet (e.g. the first time the program runs),
     * an empty list is returned. Blank lines are ignored, and lines not in the
     * expected format are skipped and counted, see {@link #getCorruptedLineCount()}.
     *
     * @return the loaded tasks, in file order.
     * @throws GLaDOSException if the file exists but could not be read.
     */
    public ArrayList<Task> load() throws GLaDOSException {
        corruptedLineCount = 0;
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new GLaDOSException("I couldn't read your saved tasks from " + filePath + ".");
        }

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseTask(line));
            } catch (GLaDOSException e) {
                corruptedLineCount++;
            }
        }
        return tasks;
    }

    /**
     * Returns how many lines the last call to {@link #load()} skipped as corrupted.
     */
    public int getCorruptedLineCount() {
        return corruptedLineCount;
    }

    /**
     * Turns one data file line, e.g. "D | 1 | return book | Sunday", back into a task.
     *
     * @throws GLaDOSException if the line is not in the expected format.
     */
    private static Task parseTask(String line) throws GLaDOSException {
        // split takes a regular expression, where | has a special meaning, so it is escaped.
        // The -1 keeps empty trailing fields, so "T | 0 | " is caught as having an empty description.
        String[] parts = line.split(" \\| ", -1);
        for (String part : parts) {
            if (part.isBlank()) {
                throw new GLaDOSException("Empty field in line: " + line);
            }
        }
        if (parts.length < 3) {
            throw new GLaDOSException("Too few fields in line: " + line);
        }

        String type = parts[0];
        String doneFlag = parts[1];
        String description = parts[2];

        Task task;
        if (type.equals("T") && parts.length == 3) {
            task = new Todo(description);
        } else if (type.equals("D") && parts.length == 4) {
            task = new Deadline(description, parts[3]);
        } else if (type.equals("E") && parts.length == 5) {
            task = new Event(description, parts[3], parts[4]);
        } else {
            throw new GLaDOSException("Unknown type or wrong number of fields in line: " + line);
        }

        if (doneFlag.equals("1")) {
            task.markAsDone();
        } else if (!doneFlag.equals("0")) {
            throw new GLaDOSException("Done flag is not 0 or 1 in line: " + line);
        }
        return task;
    }

    /**
     * Writes every task to the data file, replacing its previous contents.
     *
     * <p>Creates the file's folder first if it does not exist yet.
     *
     * @param tasks the task list.
     * @throws GLaDOSException if the file could not be written.
     */
    public void save(TaskList tasks) throws GLaDOSException {
        ArrayList<String> lines = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            lines.add(tasks.get(i).toFileString());
        }

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new GLaDOSException("I couldn't save your tasks to " + filePath + ".");
        }
    }
}
