package duke.commands;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import duke.common.DukeException;
import duke.common.Utils;
import duke.constants.Constants;
import duke.storage.Storage;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Marks whether a task is done.
 */
public class ToggleCommand extends Command {
    private String args;
    private boolean isMark;

    /**
     * Creates new ToggleCommand object that receives user input.
     * @param args args is used to check for user input.
     * @param isMark isMark indicates if a user has done the task.
     */
    public ToggleCommand(String args, boolean isMark) {
        this.args = args;
        this.isMark = isMark;
    }

    /**
     * Executes the toggle command.
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        try {
            if (this.args.length() == 0) {
                throw new IllegalArgumentException();
            }

            if (!Utils.isNumeric(this.args, ui)) {
                throw new NumberFormatException();
            }

            int index = Integer.parseInt(this.args);
            Task updatedTask = taskList.toggleCompleted(this.isMark, --index);
            String taskString = updatedTask.toString();
            String updateString = "";

            if (taskString.charAt(1) == 'D') {
                String date = ((Deadline) updatedTask)
                        .getDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                updateString = String.format(
                        "D | %s | %s | %s",
                        (updatedTask.getCompleted()) ? "1" : "0",
                        updatedTask.getDescription(),
                        date);
            } else if (taskString.charAt(1) == 'E') {
                String date = ((Event) updatedTask)
                        .getDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                updateString = String.format(
                        "E | %s | %s | %s",
                        (updatedTask.getCompleted()) ? "1" : "0",
                        updatedTask.getDescription(),
                        date);
            } else {
                updateString = String.format(
                        "T | %s | %s",
                        (updatedTask.getCompleted()) ? "1" : "0",
                        updatedTask.getDescription());
            }
            
            storage.writeToFile(updateString, index, false);

            String output = (isMark)
                    ? "Nice! I've marked this task as done:\n"
                    : "OK, I've marked this task as not done yet:\n";

            ui.output(output + taskString);
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException(Constants.INVALID_INDEX_MSG);
        } catch (IOException e) {
            throw new DukeException(Constants.STORAGE_UPDATE_MSG);
        } catch (IllegalArgumentException e) {
            throw new DukeException(Constants.INVALID_MARK_MSG);
        }
    }
}
