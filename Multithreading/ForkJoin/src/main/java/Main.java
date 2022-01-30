import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static final String FILE_NAME = "results.txt";


    public static void main(String[] args) {
        //String url = "https://lenta.ru";
        String url = "https://skillbox.ru";
        url = slashAdding(url);
       List<String> toFileString = new ForkJoinPool().invoke(new ReferenceFinder(url));

        toFileString.forEach(System.out::println);

        fileWriting(FILE_NAME,toFileString);

    }

    private static String slashAdding(String url) {
        if(url.charAt(url.length() - 1) == '/') {
            return url;
        } else {
            return url + "/";
        }
    }

    private static void fileWriting(String filename, List<String> toFileString) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for(String string : toFileString) {
                writer.append(string);
                writer.append(System.lineSeparator());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
