import java.util.Scanner;

public class Main {

    private static EmailList emailList = new EmailList();

    public static void executionOfAction(ActionType action, String string) {
        String data;
        switch (action) {
            case ADD:
                data = string.replaceFirst("[A][D][D] ", "");
                emailList.add(data);
                break;
            case LIST:
                emailList.getSortedEmails();
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
            action = input.matches("[L][I][S][T]") ? ActionType.LIST : action;
            executionOfAction(action, input);
            }

        System.out.println();
    }
}
