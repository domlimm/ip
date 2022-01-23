import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    private static final String BOT_ART =
            "     /\\   | |   | |          \n" +
            "    /  \\  | |__ | |__  _   _ \n" +
            "   / /\\ \\ | '_ \\| '_ \\| | | |\n" +
            "  / ____ \\| |_) | |_) | |_| |\n" +
            " /_/    \\_\\_.__/|_.__/ \\__, |\n" +
            "                        __/ |\n" +
            "                       |___/ ";
    private static final String BOT_NAME = "Abby";
    private static final String UNKNOWN_MSG =
            "OOPS!!! I'm sorry, but I don't know what that means :-(";
    private static final String INVALID_INDEX_MSG =
            "OOPS!!! I can't find that in your task list :-(";
    private static final String BROKEN_MSG =
            "YOU BROKE ME :-(";
    private static final String INVALID_MARK_MSG =
            "OOPS!!! I need a number to update that task :-(";
    private static final String INVALID_DELETE_MSG =
            "OOPS!!! I need a number to delete that task :-(";
    private static final String INVALID_DATE_MSG =
            "OOPS!!! I cannot recognise that date format. :-("
                    + "\nAlso, in this date format yyyy-mm-dd please!";
    private static final String NO_TASK_SEARCH_MSG =
            "I can't find any tasks of that date. :-(";

    private ArrayList<Task> tasks;
    
    public Duke() {
        this.tasks = new ArrayList<>();
    }

    public void output(String output) {
        String result =
                "____________________________________________________________\n\n" +
                output +
                "\n____________________________________________________________\n\n";
        System.out.printf("%s", result);
    }

    public String bye() {
        return "Bye. Hope to see you again soon!";
    }

    public void add(String task) {
        String[] taskArr = null;
        String type = "";
        String result = "Got it. I've added this task:\n";

        try {
            taskArr = task.split(" ", 2);
            type = taskArr[0];

            if (type.equalsIgnoreCase("deadline")) {
                String[] taskData = taskArr[1].split(" /by ");

                System.out.println(taskData[1]);

                LocalDate by = LocalDate.parse(taskData[1]);

                this.tasks.add(new Deadline(taskData[0], by, 'D'));
            } else if (type.equalsIgnoreCase("event")) {
                String[] taskData = taskArr[1].split(" /at ");

                System.out.println(taskData[1]);

                LocalDate at = LocalDate.parse(taskData[1]);

                this.tasks.add(new Event(taskData[0], at, 'E'));
            } else if (type.equalsIgnoreCase("todo")) {
                if (taskArr[1].trim().length() == 0) {
                    throw new IndexOutOfBoundsException();
                }

                this.tasks.add(new Todo(taskArr[1], 'T'));
            } else {
                throw new IndexOutOfBoundsException();
            }

            int noOfTasks = this.tasks.size();
            String pluralTask = noOfTasks > 1 ? "tasks" : "task";

            result += "  " + this.tasks.get(noOfTasks - 1).toString() + "\n";
            result += "Now you have " + noOfTasks + " " + pluralTask + " in the list.";

            output(result);
        } catch (IndexOutOfBoundsException e) {
            if (isValidType(type)) {
                if (isMissingData(taskArr)) {
                    output("OOPS!!! Some data of your " + type + " task is missing. :-(");
                    return;
                }

                output("OOPS!!! The description of a " + type + " cannot be empty. :-(");
            } else {
                output(UNKNOWN_MSG);
            }
        } catch (DateTimeParseException e) {
            output(INVALID_DATE_MSG);
        }
    }

    public boolean isValidType(String type) {
        if (type.equalsIgnoreCase("todo") ||
                type.equalsIgnoreCase("event") ||
                type.equalsIgnoreCase("deadline")) {
            return true;
        }

        return false;
    }

    public boolean isMissingData(String[] taskArr) {
        if (taskArr.length < 3 && !taskArr[0].equalsIgnoreCase("todo")) {
            return true;
        }

        return false;
    }

    public void list() {
        int length = this.tasks.size();
        StringBuilder sb = new StringBuilder();

        if (length == 0) {
            output("No tasks found! Quit lazing around!");
            return;
        }

        sb.append("Here are the tasks in your list:\n");

        for (int i = 0; i < length; ++i) {
            sb.append(i + 1 + ". " + this.tasks.get(i).toString());

            if (i + 1 != length) {
                sb.append("\n");
            }
        }

        output(sb.toString());
    }

    public void toggleCompleted(boolean isMark, int index) {
        try {
            this.tasks.get(--index).setCompleted(isMark);

            String output = isMark ?
                    "Nice! I've marked this task as done:\n" :
                    "OK, I've marked this task as not done yet:\n";
            String task = "  " + this.tasks.get(index).toString();

            output(output + task);
        } catch (IndexOutOfBoundsException e) {
            output(INVALID_INDEX_MSG);
        }
    }

    public void delete(int index) {
        try {
            int noOfTasks = this.tasks.size();
            String task = this.tasks.get(index - 1).toString();

            this.tasks.remove(--index);

            noOfTasks = this.tasks.size();

            String result = "Noted. I've removed this task:\n";
            String pluralTask = noOfTasks > 1 ? "tasks" : "task";

            result += noOfTasks > 0 ?
                    "  " + task +
                    "\nNow you have " + noOfTasks + " " + pluralTask + " in the list." :
                    "  " + task +
                    "\nNow you have no task left.";

            output(result);
        } catch (IndexOutOfBoundsException e) {
            output(INVALID_INDEX_MSG);
        } catch (NumberFormatException e) {
            output(INVALID_INDEX_MSG);
        }
    }

    public boolean isNumeric(String arg) {
        if (arg == null) {
            output(INVALID_INDEX_MSG);
            return false;
        }

        try {
            Integer.parseInt(arg);
        } catch (NumberFormatException nfe) {
            output(INVALID_INDEX_MSG);
            return false;
        }

        return true;
    }

    public void search(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            int index = 0;
            int length = this.tasks.size();
            StringBuilder sb = new StringBuilder();

            if (length == 0) {
                output("No tasks found based on given date! Also, quit lazing around!");
                return;
            }

            sb.append("Here are the tasks with date, "
                    + date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                    + ", in your list:\n");

            for (int i = 0; i < length; ++i) {
                Task task = this.tasks.get(i);

                if (task.getType() == 'D') {
                    Deadline deadline = (Deadline) this.tasks.get(i);

                    if (deadline.getDate().isEqual(date)) {
                        sb.append(++index + ". " + deadline.toString());
                    }
                } else if (task.getType() == 'E') {
                    Event event = (Event) this.tasks.get(i);

                    if (event.getDate().isEqual(date)) {
                        sb.append(++index + ". " + event.toString());
                    }
                }

                if (i + 1 != length) {
                    sb.append("\n");
                }
            }

            output(index > 0 ? sb.toString() : NO_TASK_SEARCH_MSG);
        } catch (DateTimeParseException e) {
            output(INVALID_DATE_MSG);
        }
    }

    public void start() {
        Input input = null;
        Scanner sc = new Scanner(System.in);

        output(BOT_ART + "\nHello! I'm " + BOT_NAME + "\nWhat can I do for you?\n");
            
        while (true) {
            try {
                input = new Input(sc.nextLine());

                switch (input.getStates()) {
                case ECHO:
                    output(input.getInput());

                    break;
                case BYE:
                    output(bye());

                    break;
                case ADD:
                    add(input.getInput());

                    break;
                case LIST:
                    list();

                    break;
                case TOGGLE:
                    boolean isMark =
                            input.getInput().split(" ")[0].equalsIgnoreCase("mark");

                    if (input.getArgs().length() == 0) {
                        output(INVALID_MARK_MSG);
                        continue;
                    } else if (isNumeric(input.getArgs())) {
                        toggleCompleted(isMark, Integer.parseInt(input.getArgs()));
                    } else {
                        continue;
                    }

                    break;
                case DELETE:
                    if (input.getArgs().length() == 0) {
                        output(INVALID_DELETE_MSG);
                        continue;
                    } else if (isNumeric(input.getArgs())) {
                        delete(Integer.parseInt(input.getArgs()));
                    } else {
                        continue;
                    }

                    break;
                case SEARCH:
                    search(input.getArgs());

                    break;
                default:
                    throw new DukeException(UNKNOWN_MSG);
                }

                if (input.getStates() == Input.States.BYE) {
                    break;
                }
            } catch (DukeException e) {
                output(UNKNOWN_MSG);
            } catch (IllegalArgumentException e) {
                output(BROKEN_MSG + " " + e.getMessage());
            }
        }

        sc.close(); 
    }

    public static void main(String[] args) {
        Duke abby = new Duke();

        abby.start();
    }
}