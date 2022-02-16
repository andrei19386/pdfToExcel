package mongo;

public class Shop {
    private final String shopName;
    private int countGoods;
    private double averagePrice;
    private String nameExpensive;
    private String nameCheap;
    private int countLess100;
    private int minPrice;
    private int maxPrice;


    public Shop(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public int getCountGoods() {
        return countGoods;
    }

    public void setCountGoods(int countGoods) {
        this.countGoods = countGoods;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getNameExpensive() {
        return nameExpensive;
    }

    public void setNameExpensive(String nameExpensive) {
        this.nameExpensive = nameExpensive;
    }

    public String getNameCheap() {
        return nameCheap;
    }

    public void setNameCheap(String nameCheap) {
        this.nameCheap = nameCheap;
    }

    public int getCountLess100() {
        return countLess100;
    }

    public void setCountLess100(int countLess100) {
        this.countLess100 = countLess100;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }
}
