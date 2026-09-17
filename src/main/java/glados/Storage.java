package glados;

import glados.task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Saves the task list to a text file on disk, one task per line.
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
