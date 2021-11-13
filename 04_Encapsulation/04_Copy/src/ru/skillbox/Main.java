package ru.skillbox;

public class Main {

    public static void main(String[] args) {

        Dimensions dimensions = new Dimensions(20.3,30,40.5);

        GoodsTransferredToCourier goodsTransferredToCourier = new GoodsTransferredToCourier(
                dimensions, 4.1, "Улица Академика Курчатова, д.1 ", false,
                "135TR",true
        );

        Dimensions dimensionsFirst = dimensions.setHeight(5.1).setWidth(2.3).setLength(7.9);
        GoodsTransferredToCourier goodsFirst = goodsTransferredToCourier.
                setDeliveryAddress("Енисейская улица, д.3").
                setWeight(3.5).setDimensions(dimensionsFirst);

        System.out.println();
        System.out.println("Информация по исходному объекту");
        System.out.println("Габариты  - " + dimensions.getLength()
                + " * " + dimensions.getWidth() + " * " + dimensions.getHeight() + " см");
        System.out.println("Объем товара - " + dimensions.computeVolume() + " см^3");
        System.out.println("Масса товара - " + goodsTransferredToCourier.getWeight() + " кг");
        System.out.println("Адрес доставки - " + goodsTransferredToCourier.getDeliveryAddress());
        System.out.println("Можно переворачивать? - " + (goodsTransferredToCourier.getCanBeFlipped() ? "Да" : "Нет"));
        System.out.println("Регистрационный номер - " + goodsTransferredToCourier.getRegistrationNumber());
        System.out.println("Товар хрупкий? - " + (goodsTransferredToCourier.getIsFragile() ? "Да" : "Нет"));

        System.out.println();
        System.out.println("Информация по скопированному объекту");
        System.out.println("Габариты  - " + dimensionsFirst.getLength()
                + " * " + dimensionsFirst.getWidth() + " * " + dimensionsFirst.getHeight() + " см");
        System.out.println("Объем товара - " + dimensionsFirst.computeVolume() + " см^3");
        System.out.println("Масса товара - " + goodsFirst.getWeight() + " кг");
        System.out.println("Адрес доставки - " + goodsFirst.getDeliveryAddress());
        System.out.println("Можно переворачивать? - " + (goodsFirst.getCanBeFlipped() ? "Да" : "Нет"));
        System.out.println("Регистрационный номер - " + goodsFirst.getRegistrationNumber());
        System.out.println("Товар хрупкий? - " + (goodsFirst.getIsFragile() ? "Да" : "Нет"));
    }
}
