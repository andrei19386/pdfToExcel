public class ReverseArray {

    public static String[] reverse (String[] strings){
        String word = "";
        for(int i = 0; i < strings.length / 2; i++){
            word = strings[i];
            strings[i] = strings[strings.length - 1 - i];
            strings[strings.length - 1 - i] = word;
        }
        return strings;
    }
}
