import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    while (true) {
      String input = scanner.nextLine();
      if (input.equals("0")) {
        break;
      }
      String output;
      String regex = "[^0-9]";
      output = input.replaceAll(regex,"");
      String regexSecond = "[7-8][0-9]{10}";
      String regexThird = "[0-9]{10}";
      if ( output.matches(regexSecond) ) {
        String regexFourth = "[0-9]";
        output = output.replaceFirst(regexFourth, "7");
        System.out.println(output);
      } else if (output.matches(regexThird)) {
        output = "7" + output;
        System.out.println(output);
      } else {
        System.out.println("Неверный формат номера");
      }
    }
  }

}
