public class Basket {

    private static int count = 0;
    private String items;
    private int totalPrice = 0;
    private int limit;
    private double totalWeight = 0;

    private static int allTotalPrice = 0;
    private static int allGoods = 0;

    public static int getAllTotalPrice() {
        return allTotalPrice;
    }

    public static int getAllGoods() {
        return allGoods;
    }

    private static void increaseAllTotalPrice(int totalPrice) {
        Basket.allTotalPrice = Basket.allTotalPrice + totalPrice;
    }

    private static void increaseAllGoods (int goods) {
        Basket.allGoods = Basket.allGoods + goods;
    }

    public static double averagePriceGoods() {
        if(allGoods != 0){
            return (1.0 * allTotalPrice) / allGoods;
        }
        //System.out.println("Не могу рассчитать среднюю стоимость товара, пока ни в одной корзине нет ни одного товара!");
        return 0;
    }


    public static double averagePriceBasket() {
        if(count != 0){
            return (1.0 * allTotalPrice) / count;
        }
        //System.out.println("Не могу рассчитать среднюю стоимость товара, пока не создана ни одна корзина!");
        return 0;
    }

    public Basket() {
        increaseCount(1);
        items = "Список товаров:";
        this.limit = 1000000;
    }

    public Basket(int limit) {
        this();
        this.limit = limit;
    }

    public Basket(String items, int totalPrice) {
        this();
        this.items = this.items + items;
        this.totalPrice = totalPrice;
        increaseAllTotalPrice(totalPrice);
        increaseAllGoods(1);
    }

    public Basket(String items, int totalPrice, double totalWeight) {
        this(items, totalPrice);
        this.totalWeight = totalWeight;
    }

    public static int getCount() {
        return count;
    }

    private static void increaseCount(int count) {
        Basket.count = Basket.count + count;
    }

    public void add(String name, int price) {
        add(name, price, 1);
    }

    public void add(String name, int price, int count) { add(name, price, count, 0);  }


    public void add(String name, int price, int count, double weight) {
        boolean error = contains(name);

//        if (contains(name)) {
//            error = true;
//        }

        if (totalPrice + count * price >= limit) {
            error = true;
        }

        if (error) {
            System.out.println("Error occured :(");
            return;
        }

        items = items + "\n" + name + " - " +
                count + " шт. - " + price;
        totalPrice = totalPrice + count * price;
        increaseAllTotalPrice(count * price);
        increaseAllGoods(count);
        totalWeight = totalWeight + count * weight;

    }

    public void clear() {
        items = "";
        totalPrice = 0;
        totalWeight = 0;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
    public double getTotalWeight() { return totalWeight; }

    public boolean contains(String name) {
        return items.contains(name);
    }

    public void print(String title) {
        System.out.println(title);
        if (items.isEmpty()) {
            System.out.println("Корзина пуста");
        } else {
            System.out.println(items);
        }
    }
}