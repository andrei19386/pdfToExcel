import java.util.Scanner;

public class Main {
//    public static int spaceCount(String string) {
//        int count = 0;
//        for(int i = 0; i < string.length(); i++) {
//            if(string.charAt(i) == ' ') {
//                count++;
//            }
//        }
//        return count;
//    }

  public static boolean isCyrillicText(String string) {
    char symbol;
    for(int i = 0; i < string.length(); i++) {
      symbol = string.charAt(i);
      if( (int)symbol >= 1040 && (int)symbol <= 1103 ) {
        continue;
      }
      switch (symbol) {
        case ' ' : continue;
        case '-' :  continue;
        case 'ё' :  continue;
        case 'Ё' :  continue;
        default : return false;
      }
    }
    return true;
  }

//    public static String getSurname(String string) {
//        int firstSpaceIndex = string.indexOf("\s", 0);
//        return string.substring(0,firstSpaceIndex);
//    }
//
//    public static String getName(String string) {
//        int firstSpaceIndex = string.indexOf("\s", 0);
//        int secondSpaceIndex = string.lastIndexOf("\s");
//        return string.substring(firstSpaceIndex + 1,secondSpaceIndex);
//    }
//
//
//    public static String gerPatronymic(String string) {
//        int secondSpaceIndex = string.lastIndexOf("\s");
//        return string.substring(secondSpaceIndex + 1, string.length());
//    }


  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      String input = scanner.nextLine();
      if (input.equals("0")) {
        break;
      }

      String[] words = input.trim().split(" ");
      if (words.length != 3 || !isCyrillicText(input.trim())) {
        System.out.println("Введенная строка не является ФИО");
        continue;
      }

      String surname = words[0];
      String name = words[1];
      String patronymic = words[2];

      System.out.println("Фамилия: " + surname + "\nИмя: " + name + "\nОтчество: " + patronymic);
    }
  }
}
