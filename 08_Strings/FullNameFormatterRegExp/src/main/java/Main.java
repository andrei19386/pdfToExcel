import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    while (true) {
      String input = scanner.nextLine();
      if (input.equals("0")) {
        break;
      }
      String regex = "[А-яЁё-]+";
      String regexSecond = regex + " " + regex + " " + regex;
      if(input.matches(regexSecond)) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        int start;
        int end;
        String[] words = {"","",""};
        int i = 0;
        while (matcher.find()){
          start = matcher.start();
          end = matcher.end();
          words[i] = input.substring(start, end);
          i++;
        }
        String surname = words[0];
        String name = words[1];
        String patronymic = words[2];
        System.out.println("Фамилия: " + surname + "\nИмя: " + name + "\nОтчество: " + patronymic);
      } else {
        System.out.println("Введенная строка не является ФИО");
      }
    }
  }

}