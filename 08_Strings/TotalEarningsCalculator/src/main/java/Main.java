public class Main {

  public static boolean isDigits(String substring){
    for(int i = 0; i < substring.length(); i++){
      if( !Character.isDigit(substring.charAt(i)) ) {
        return false;
      }
    }
    return true;
  }

  public static boolean spaceExists(String string) {
    char symbol;
    for(int i = 0; i < string.trim().length(); i++){
      symbol = string.trim().charAt(i);
      if( symbol == ' ' ) {
        return true;
      }
    }
    return false;
  }

  public static int onlyOneWordAnalise(String string) {//Анализ единственного слова
    String substring = string.trim().substring(0, string.trim().length());
    if( isDigits(substring) ) {
      return Integer.parseInt(substring);
    }
    return 0;
  }

  public static int firstWordAnalise(String string) {//Анализ первого слова на случай, если их несколько
    int firstSpaceIndex = string.trim().indexOf(' ');
    String substring = string.trim().substring(0, firstSpaceIndex);
    if( isDigits(substring) ) {
      return Integer.parseInt(substring);
    }
    return 0;
  }


  public static int lastWordAnalise(String string) {//Анализ последнего слова на случай, если их несколько
    int lastSpaceIndex = string.trim().lastIndexOf(' ');
    String substring = string.trim().substring(lastSpaceIndex + 1, string.trim().length());
    if( isDigits(substring) ) {
      return Integer.parseInt(substring);
    }
    return 0;
  }

  public static int middleWordsAnalise(String string) { //Анализ промежуточных слов на случай, если слов больше двух
    int firstSpaceIndex = string.trim().indexOf(' ', 0);
    int secondSpaceIndex;
    int earning = 0;
    int lastSpaceIndex = string.trim().lastIndexOf(' ');
    while(firstSpaceIndex != lastSpaceIndex) {
      secondSpaceIndex = string.trim().indexOf(' ', firstSpaceIndex + 1);
      String substring = string.trim().substring(firstSpaceIndex + 1, secondSpaceIndex);
      if( isDigits(substring) ) {
        earning += Integer.parseInt(substring);
      }
      firstSpaceIndex = secondSpaceIndex;
    }
    return earning;
  }


  public static void main(String[] args) {

    String text = "Вася заработал 5000 рублей, Петя - 7563 рубля, а Маша - 30000 рублей";

    int earning = 0;

    if( text.trim().isEmpty() ) { //Если строка пустая, заработок равен 0
      System.out.println("Суммарный заработок равен: " + earning);
      return;
    }

    if(!spaceExists(text) ) { //Если нет пробелов, анализируем единственное слово
      earning += onlyOneWordAnalise(text);
      System.out.println("Суммарный заработок равен: " + earning);
      return;
    }

    //Если есть хотя бы один пробел (хотя бы два слова, анализируем первое и последнее)
    earning += firstWordAnalise(text);
    earning += lastWordAnalise(text);
    if(text.trim().indexOf(' ') != text.trim().lastIndexOf(' ')) {
      //Кроме того, если больше одного пробела, анализируем слова в середине
      earning += middleWordsAnalise(text);
    }
    System.out.println(earning);

  }

}