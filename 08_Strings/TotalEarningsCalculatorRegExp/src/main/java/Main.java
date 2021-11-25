import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {

  public static void main(String[] args) {
    String text = "Вася заработал 5000 рублей, Петя - 7563 рубля, а Маша - 30000 рублей";
    int earning = calculateSalarySum(text);
    System.out.println(earning);
  }

  public static int calculateSalarySum(String text){
    String regex = "[0-9]+";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    int earning = 0;
    int start;
    int end;
    while (matcher.find()){
      start = matcher.start();
      end = matcher.end();
      earning += Integer.parseInt(text.substring(start, end));
    }
    return earning;
  }

}