package ru.skillbox;

public class Main {
    public static void main(String[] args) {
        //Создание двух объектов-экземпляров класса Country - задание 1
        Country country = new Country("Российская Федерация");
        country.setPopulation(146);
        country.setSquare(17125191);
        country.setCapitalName("Москва");
        country.setLandlocked(false);

        Country countrySecond = new Country("Белоруссия");
        countrySecond.setPopulation(9);
        countrySecond.setSquare(207600);
        countrySecond.setCapitalName("Минск");
        countrySecond.setLandlocked(true);

        //Проверяем сеттеры-геттеры к заданию 1
        System.out.println();
        System.out.println("Задание 1");
        System.out.println("Страна - " + country.getTitle());
        System.out.println("Численность населения - " + country.getPopulation() + " млн. человек");
        System.out.println("Площадь территории - " + country.getSquare() + " кв. км");
        System.out.println("Столица - " + country.getCapitalName());
        System.out.println("Есть ли выход к морю? - " + (country.getLandlocked() ? "Нет" : "Да"));
        System.out.println();
        System.out.println("Страна - " + countrySecond.getTitle());
        System.out.println("Численность населения - " + countrySecond.getPopulation() + " млн. человек");
        System.out.println("Площадь территории - " + countrySecond.getSquare() + " кв. км");
        System.out.println("Столица - " + countrySecond.getCapitalName());
        System.out.println("Есть ли выход к морю? - " + (countrySecond.getLandlocked() ? "Нет" : "Да"));

        //Создание объекта-экземпляра класса Book - задание 2

        Book book = new Book("Практический курс Трансерфинга за 78 дней");
        book.setAuthor("Зеланд");
        book.setNumberOfPages(432);
        book.setPrice(250);

        //Проверяем сеттеры-геттеры к заданию 2
        System.out.println();
        System.out.println("Задание 2");
        System.out.println("Название книги - " + book.getBookTitle());
        System.out.println("Автор - " + book.getAuthor());
        System.out.println("Количество страниц - " + book.getNumberOfPages());
        System.out.println("Цена книги - " + book.getPrice() + " р.");
    }
}
