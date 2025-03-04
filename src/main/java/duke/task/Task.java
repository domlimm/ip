package duke.task;

/**
 * Represents a parent task for all child tasks.
 * With its description, completion status and its type i.e. Todo, Deadline or Event.
 */
public class Task {
    private final String description;
    private boolean isCompleted;
    private final char type;

    /**
     * Creates a Task object.
     *
     * @param description description of the task.
     * @param type type of the task.
     */
    public Task(String description, char type) {
        assert description != null : "Task[Task] description cannot be null.";
        assert description.length() > 0 : "Task[Task] description must contain data.";

        this.description = description;
        this.isCompleted = false;
        this.type = type;
    }

    /**
     * Gets description of task.
     *
     * @return Description of task.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Checks if task is completed.
     *
     * @return Task completed.
     */
    public boolean getCompleted() {
        return this.isCompleted;
    }

    /**
     * Gets the representation of whether task is completed.
     *
     * @return X marks task is completed.
     */
    public String getStatusIcon() {
        return (this.isCompleted) ? "X" : " ";
    }

    /**
     * Gets the type of task.
     *
     * @return Either Todo, Deadline or Event.
     */
    public char getType() {
        return this.type;
    }

    /**
     * Updates that task is completed.
     *
     * @param setCompleted setCompleted is whether task is done.
     */
    public void setCompleted(boolean setCompleted) {
        this.isCompleted = setCompleted;
    }

    /**
     * Returns a formatted string of a general task.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.getDescription());
    }
}
