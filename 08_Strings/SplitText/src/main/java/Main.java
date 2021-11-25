import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {
    String text = "Mary was 20 years old, George was 40 years old";
   // System.out.println(splitTextIntoWords(text));
  }

  public static String splitTextIntoWords(String text) {
    String regex = "[A-z’]+";
    boolean firstTime = true;
    String result = "";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    int start;
    int end;
    while (matcher.find()){
      if(firstTime){
        firstTime = false;
      } else {
        result = result.concat("\n");
      }
      start = matcher.start();
      end = matcher.end();
      result = result.concat(text.substring(start, end));
     }
    return result;
  }

}