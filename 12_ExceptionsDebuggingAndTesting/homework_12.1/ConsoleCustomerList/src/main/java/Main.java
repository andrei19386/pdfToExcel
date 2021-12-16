import java.util.Scanner;

public class Main {
    private static final String ADD_COMMAND = "add Василий Петров " +
            "vasily.petrov@gmail.com +79215637722";
    private static final String COMMAND_EXAMPLES = "\t" + ADD_COMMAND + "\n" +
            "\tlist\n\tcount\n\tremove Василий Петров";
    private static final String COMMAND_ERROR = "Wrong command! Available command examples: \n" +
            COMMAND_EXAMPLES;
    private static final String HELP_TEXT = "Command examples:\n" + COMMAND_EXAMPLES;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerStorage executor = new CustomerStorage();

        while (true) {
            try {
                String command = scanner.nextLine();
                String[] tokens = command.split("\\s+", 2);

                if (tokens[0].equals("add")) {
                    extractedExceptionAdd(tokens);
                    executor.addCustomer(tokens[1]);
                } else if (tokens[0].equals("list")) {
                    executor.listCustomers();
                } else if (tokens[0].equals("remove")) {
                    extractedExceptionRemove(tokens);
                    executor.removeCustomer(tokens[1]);
                } else if (tokens[0].equals("count")) {
                    System.out.println("There are " + executor.getCount() + " customers");
                } else if (tokens[0].equals("help")) {
                    System.out.println(HELP_TEXT);
                } else {
                    System.out.println(COMMAND_ERROR);
                }
            } catch (IllegalArgumentException exception) {
                exception.getMessage();
            }
        }
    }

    private static void extractedExceptionRemove(String[] tokens) {
        if (tokens.length == 1) {
            throw new IllegalArgumentException("You should input the second argument to remove!");
        }
    }

    private static void extractedExceptionAdd(String[] tokens) {
        if (tokens.length == 1) {
            throw new IllegalArgumentException("You should input the second argument to add!");
        }
    }
}
