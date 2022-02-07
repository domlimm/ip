package duke.commands;

import java.io.IOException;

import duke.common.DukeException;
import duke.common.Utils;
import duke.constants.Constants;
import duke.storage.Storage;
import duke.task.TaskList;

/**
 * Deletes task from list based on specified index by user.
 */
public class DeleteCommand extends Command {
    private final String args;

    /**
     * Creates new DeleteCommand object that receives the index by user.
     * 
     * @param args args holds the task number to be deleted.
     */
    public DeleteCommand(String args) {
        assert args != null : "DeleteCommand[DeleteCommand] args cannot be null.";
        assert args.length() > 0 : "DeleteCommand[DeleteCommand] args must contain data.";

        this.args = args;
    }

    /**
     * Executes the delete command.
     * 
     * @return Output message for GUI.
     */
    @Override
    public String execute(TaskList taskList, Storage storage) throws DukeException {
        assert taskList != null : "DeleteCommand[execute] taskList cannot be null.";
        assert storage != null : "DeleteCommand[execute] storage cannot be null.";

        try {
            if (this.args.length() == 0) {
                throw new IllegalArgumentException();
            }

            if (!Utils.isNumeric(this.args)) {
                throw new NumberFormatException();
            }

            int index = Integer.parseInt(this.args);
            int noOfTasks = taskList.size();
            String task = taskList.getTasks().get(index - 1).toString();

            taskList.delete(--index);
            storage.writeToFile("", index, true);
            noOfTasks = taskList.size();

            String result = "Noted. I've removed this task:\n";
            String pluralTask = (noOfTasks > 1) ? "tasks" : "task";

            result += (noOfTasks > 0)
                    ? "  " + task + "\nNow you have " + noOfTasks + " " + pluralTask + " in the list."
                    : "  " + task + "\nNow you have no task left.";

            return result;
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException(Constants.INVALID_INDEX_MSG);
        } catch (IllegalArgumentException e) {
            throw new DukeException(Constants.INVALID_DELETE_MSG);
        } catch (IOException e) {
            throw new DukeException(Constants.STORAGE_DELETE_MSG);
        }
    }
}
