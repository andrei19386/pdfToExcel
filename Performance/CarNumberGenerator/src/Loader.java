//import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Loader {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        //FileOutputStream writer = new FileOutputStream("res/numbers.txt");
        //PrintWriter writer = new PrintWriter("res/numbers.txt");
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        PrintWriter[] writers = new PrintWriter[numberOfThreads];
        for(int i = 0; i < numberOfThreads; i++) {
           writers[i] = new PrintWriter("res/numbers" + i + ".txt");
        }

        char letters[] = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};

        for (int regionCode = 1; regionCode < 100; regionCode++) {
            StringBuilder builder = new StringBuilder();
            for (int number = 1; number < 1000; number++) {
                for (char firstLetter : letters) {
                    for (char secondLetter : letters) {
                        for (char thirdLetter : letters) {

                            builder.append(firstLetter);
                            builder.append(padNumber(number, 3));
                            builder.append(secondLetter);
                            builder.append(thirdLetter);
                            builder.append(padNumber(regionCode, 2));
                            builder.append("\n");
                        }
                    }
                }
            }
            int thread = regionCode % numberOfThreads;
            new Thread(() -> {
                    writers[thread].write(builder.toString());
            }).start();
        }

        for(int i = 0; i < numberOfThreads; i++) {
            writers[i].flush();
            writers[i].close();
        }

        System.out.println((System.currentTimeMillis() - start) + " ms");
    }

    private static String padNumber(int number, int numberLength) {
        StringBuilder builder = new StringBuilder();
        int len;
        if (number < 10) {
            len = 1;
        } else {
          len = number < 100 ? 2 : 3;
        }
        int padSize = numberLength - len;

        for (int i = 0; i < padSize; i++) {
            builder.append('0');
        }
        builder.append(number);
        return builder.toString();
    }
}
