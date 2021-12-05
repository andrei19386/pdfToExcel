import java.util.*;

public class Main {
    public static final String NUMBER = "Г057АЕ480"; // Константа для поиска заведомо отсутствующего номера
    /*
    TODO:
     - реализовать методы класса CoolNumbers
     - посчитать время поиска введимого номера в консоль в каждой из структуры данных
     - проанализоровать полученные данные
     */

    public static void main(String[] args) {
        List<String> coolNumbers = CoolNumbers.generateCoolNumbers();
        String searchNumber = coolNumbers.get(9_000_000); // Поиск заведомо присутствующего номера
        HashSet<String> uniqueNumbers = CoolNumbers.getUniqueNumbers();
        TreeSet<String> sortedUniqueNumbers = CoolNumbers.getSortedUniqueNumbers();

        long beforeSearch = System.currentTimeMillis();
        boolean result = CoolNumbers.bruteForceSearchInList(coolNumbers, searchNumber);
        //boolean result = CoolNumbers.bruteForceSearchInList(coolNumbers, NUMBER);
        long afterSearch = System.currentTimeMillis();
        long resultTime = afterSearch - beforeSearch;

        Collections.sort(coolNumbers);

        System.out.println("Результат поиска - " + (result ? "Номер найден" : "Номер не найден") );
        System.out.println("Для поиска методом полного перебора время поиска составило " + resultTime + " мс");

        beforeSearch = System.currentTimeMillis();
        result = CoolNumbers.binarySearchInList(coolNumbers, searchNumber);
        //result = CoolNumbers.binarySearchInList(coolNumbers, NUMBER);
        afterSearch = System.currentTimeMillis();
        resultTime = afterSearch - beforeSearch;
        System.out.println("Результат поиска - " + (result ? "Номер найден" : "Номер не найден") );
        System.out.println("Для бинарного поиска время поиска составило " + resultTime + " мс");

        beforeSearch = System.currentTimeMillis();
        result = CoolNumbers.searchInHashSet(uniqueNumbers, searchNumber);
        //result = CoolNumbers.searchInHashSet(uniqueNumbers, NUMBER);

        afterSearch = System.currentTimeMillis();
        resultTime = afterSearch - beforeSearch;
        System.out.println("Результат поиска - " + (result ? "Номер найден" : "Номер не найден") );
        System.out.println("Для поиска в HashSet время поиска составило " + resultTime + " мс");


        beforeSearch = System.currentTimeMillis();
        result = CoolNumbers.searchInTreeSet(sortedUniqueNumbers, searchNumber);
        //result = CoolNumbers.searchInTreeSet(sortedUniqueNumbers, NUMBER);

        afterSearch = System.currentTimeMillis();
        resultTime = afterSearch - beforeSearch;
        System.out.println("Результат поиска - " + (result ? "Номер найден" : "Номер не найден") );
        System.out.println("Для поиска в HashSet время поиска составило " + resultTime + " мс");
    }
}
