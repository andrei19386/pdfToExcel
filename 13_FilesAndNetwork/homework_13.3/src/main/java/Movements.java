import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movements {

    private final List<Movement> movements = new ArrayList<>();
    private final Map<String,Double> expenses = new HashMap<>();

    public Movements(String pathMovementsCsv) {
        try {
            if(Path.of(pathMovementsCsv).toFile().exists()) {
                if (!Path.of(pathMovementsCsv).toFile().canRead()) {
                    throw new AccessDeniedException("The directory " + pathMovementsCsv + " cannot be read!");
                }
            } else {
                throw new IOException("The directory does not exist!");
            }
            List<String> lines = Files.readAllLines(Path.of(pathMovementsCsv));
            boolean isFirstLine = true;
            for(String line : lines){
                if(!isFirstLine) {
                    splitWithQuotes(line);
                }
                isFirstLine = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void splitWithQuotes(String line) throws IOException {
        String[] preFragments = line.split("[,]");
        ArrayList<String> fragments = new ArrayList<>();
        for(int i = 0; i < preFragments.length; i++) {
            if(countQuotes(preFragments[i]) % 2 == 0) {
                fragments.add(preFragments[i]);
            } else {
                if(preFragments[i].indexOf('\"') == 0) {
                    preFragments[i] = preFragments[i]
                            .substring(1);
                    preFragments[i+1] = preFragments[i+1]
                            .substring(0,preFragments[i+1]
                                    .lastIndexOf('\"') );
                }
                fragments.add(preFragments[i] + "." + preFragments[i+1]);
                i++;
            }
        }
        fragments.trimToSize();
        adding(fragments);
    }

    private void adding(List<String> fragments) throws IOException {
        if (fragments.size() != 8) {
            throw new IOException("Wrong number of arguments!");
        } else {
            Movement movement = new Movement(fragments);
            String[] words = fragments.get(5).split(" {3,}");
            String key = words[1];
            if(expenses.containsKey(key)){
                expenses.put(key,movement.getExpenseSum() + expenses.get(key));
            } else {
                expenses.put(key,movement.getExpenseSum());
            }
            movements.add(movement);
        }
    }

    private int countQuotes(String string) {
        char[] charArray = string.toCharArray();
        int count = 0;
        for (char c : charArray) {
            if (c == '\"') {
                count++;
            }
        }
        return count;
    }

    public double getExpenseSum() {
        double sum = 0.0;
        for(Movement movement : movements) {
            sum += movement.getExpenseSum();
        }
        return  sum;
    }

    public double getIncomeSum() {
        double sum = 0.0;
        for(Movement movement : movements) {
            sum += movement.getIncomeSum();
        }
        return  sum;
    }

    public void printMap() {
        System.out.println("Суммы расходов по организациям:");
        System.out.println();
        for (Map.Entry<String, Double> entry : expenses.entrySet()){
            String key = entry.getKey();
            Double value = entry.getValue();
            System.out.println(key + "  -  " + value + " руб.");
            System.out.println();
        }
    }
}

