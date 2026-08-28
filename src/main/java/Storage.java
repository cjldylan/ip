import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Persists the task list to a plain-text file so tasks survive between runs
 * of the chatbot. The file path is relative to the project root and is stored
 * in an OS-independent way.
 */
public class Storage {
    /** Location of the save file, relative to the project root. */
    private final Path file;

    /**
     * Creates a storage handler for the given file path.
     *
     * @param filePath path to the save file, relative to the project root,
     *     using {@code /} as the separator (e.g. {@code data/baemax.txt})
     */
    public Storage(String filePath) {
        this.file = Path.of(filePath);
    }

    /**
     * Writes every task to disk, one task per line, creating the data folder
     * and file if they do not exist yet. A failure to write is reported to the
     * user but does not stop the chatbot.
     *
     * @param tasks the tasks to save, in list order
     */
    public void save(List<Task> tasks) {
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            StringBuilder content = new StringBuilder();
            for (Task task : tasks) {
                content.append(task.toFileFormat()).append(System.lineSeparator());
            }
            Files.writeString(file, content.toString());
        } catch (IOException exception) {
            System.out.println("Baemax could not save your tasks: " + exception.getMessage());
        }
    }
}
