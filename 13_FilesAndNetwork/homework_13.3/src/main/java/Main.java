public class Main {
    public static final String PATH = "src/test/resources/movementList.csv";

    public static void main(String[] args) {
        Movements movements = new Movements(PATH);
        System.out.println("Сумма расходов: " + movements.getExpenseSum() + " руб.");
        System.out.println("Сумма расходов: " + movements.getIncomeSum() + " руб.");
        System.out.println("_____________________________________________________");
        movements.printMap();
    }
}
