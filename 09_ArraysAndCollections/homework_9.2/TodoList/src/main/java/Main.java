import java.util.Scanner;

public class Main {
    private static TodoList todoList = new TodoList();


    public static void executionOfAction(ActionType action, String string) {
        String data;
        ParseIndex parseIndex;
        switch (action) {
            case ADD_INDEX:
                data = string.replaceFirst("[A][D][D] ", "");
                parseIndex = new ParseIndex(data);
                todoList.add(parseIndex.getIndex(), parseIndex.getTodo());
                break;

            case ADD:
                 data = string.replaceFirst("[A][D][D] ", "");
                todoList.add(data);
                break;

            case EDIT:
                data = string.replaceFirst("[E][D][I][T] ", "");
                parseIndex = new ParseIndex(data);
                todoList.edit(parseIndex.getTodo(), parseIndex.getIndex());
                break;

            case DELETE:
                data = string.replaceFirst("[D][E][L][E][T][E] ", "");
                todoList.delete(Integer.parseInt(data));
                break;

            case LIST:
                todoList.getTodos();
                break;
            default:
                System.out.println("I don't know this command!");
                break;
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("0")) {
                break;
            }

            ActionType action = input.matches("[A][D][D] .{1,}") ? ActionType.ADD : ActionType.NOTHING_TODO;
            action = input.matches("[A][D][D] [0-9]{1,} .{1,}") ? ActionType.ADD_INDEX : action;
            // Порядок важен! ADD_INDEX следует за ADD
            action = input.matches("[L][I][S][T]") ? ActionType.LIST : action;
            action = input.matches("[D][E][L][E][T][E] [0-9]{1,}") ? ActionType.DELETE : action;
            action = input.matches("[E][D][I][T] [0-9]{1,} .{1,}") ? ActionType.EDIT : action;

            executionOfAction(action, input);

        }
    }
}