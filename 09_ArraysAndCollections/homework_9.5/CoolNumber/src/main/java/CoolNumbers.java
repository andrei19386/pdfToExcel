import java.util.*;

public class CoolNumbers {

    private static HashSet<String> uniqueNumbers = new HashSet<>();
    private static TreeSet<String> sortedUniqueNumbers = new TreeSet<>();
    public static final int TOTAL_NUMBERS = 10_000_000;

    public static HashSet<String> getUniqueNumbers() {
        return uniqueNumbers;
    }

    public static TreeSet<String> getSortedUniqueNumbers() {
        return sortedUniqueNumbers;
    }

    private static char generateSymbol(){
        int random = (int)( 12 * Math.random() );
        switch (random) {
            case 0: return 'А';
            case 1: return 'В';
            case 2: return 'Е';
            case 3: return 'К';
            case 4: return 'М';
            case 5: return 'Н';
            case 6: return 'О';
            case 7: return 'Р';
            case 8: return 'С';
            case 9: return 'Т';
            case 10: return 'У';
            case 11: return 'Х';
            default: return 'U';
        }
    }

    private static String generateDigit(){
        short random = (short) ( 10 * Math.random() );
        return Short.toString(random);
    }

    private static String generateRegionNumber(){
        short random = (short) ( 1000 * Math.random());
        if(random < 10) {
            return "00" + random;
        } else if(random < 100) {
            return "0" + random;
        } else {
            return Short.toString(random);
        }
    }


    public static List<String> generateCoolNumbers() {
        String coolNumber;
        while(uniqueNumbers.size() < TOTAL_NUMBERS) {
            coolNumber  = generateSymbol() + generateDigit() + generateDigit() + generateDigit() +
                    generateSymbol() + generateSymbol() + generateRegionNumber();
            uniqueNumbers.add(coolNumber);
        }
        List<String> resultList = new ArrayList<>();
        for(String item : uniqueNumbers) {
            resultList.add(item);
            sortedUniqueNumbers.add(item);
        }
        return resultList;
    }

    public static boolean bruteForceSearchInList(List<String> list, String number) {
        return list.contains(number);
    }

    public static boolean binarySearchInList(List<String> sortedList, String number) {
        int result = Collections.binarySearch(sortedList, number);
        return  result >= 0;
    }


    public static boolean searchInHashSet(HashSet<String> hashSet, String number) {
        return hashSet.contains(number);
    }

    public static boolean searchInTreeSet(TreeSet<String> treeSet, String number) {
        return treeSet.contains(number);
    }

}
