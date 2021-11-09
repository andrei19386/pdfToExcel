public class Main {

    public static void main(String[] args) {
        Basket basket = new Basket();
        basket.add("Milk", 40, 3,120);
        basket.add("Sugar",30,1,15);
        basket.add("Water",10,1);
        basket.print("Milk");
        System.out.println("Общий вес покупок - " + basket.getTotalWeight() + " г");
    }
}
