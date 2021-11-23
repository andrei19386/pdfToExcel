import java.util.Scanner;

public class Main {

  public static int spaceCount(String string) {
    int count = 0;
    for(int i = 0; i < string.length(); i++) {
      if(string.charAt(i) == ' ') {
        count++;
      }
    }
    return count;
  }

  public static boolean isCyrillicText(String string) {
    char symbol;
    for(int i = 0; i < string.length(); i++) {
      symbol = string.charAt(i);
      if( (int)symbol >= 1040 && (int)symbol <= 1103 ) {
        continue;
      }
      switch (symbol) {
        case ' ' :  continue;
        case '-' :  continue;
        case 'ё' :  continue;
        case 'Ё' :  continue;
        default :  return false;
      }
    }
    return true;
  }

  public static String getSurname(String string) {
    int firstSpaceIndex = string.indexOf(' ', 0);
    return string.substring(0,firstSpaceIndex);
  }

  public static String getName(String string) {
    int firstSpaceIndex = string.indexOf(' ', 0);
    int secondSpaceIndex = string.lastIndexOf(' ');
    return string.substring(firstSpaceIndex + 1,secondSpaceIndex);
  }


  public static String gerPatronymic(String string) {
    int secondSpaceIndex = string.lastIndexOf(' ');
    return string.substring(secondSpaceIndex + 1, string.length());
  }


  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    while (true) {
      String input = scanner.nextLine();
      if (input.equals("0")) {
        break;
      }

      if( spaceCount(input.trim()) != 2 || !isCyrillicText(input.trim()) ) {
        System.out.println("Введенная строка не является ФИО");
        return;
      }
      String surname = getSurname(input.trim());
      String name = getName(input.trim());
      String patronymic = gerPatronymic(input.trim());

      System.out.println("Фамилия: " + surname + "\nИмя: " + name + "\nОтчество: " + patronymic);
    }
  }

}