package ru.skillbox;

public class Main {

    public static void main(String[] args) {
        //Задание 1 - Создание Экземпляра класса Book через конструктор и вывод информации о нем
        System.out.println();
        System.out.println("Задание 1:");
        Book book = new Book("Практический курс Трансерфинга за 78 дней", "Зеланд", 432, "978-5-9573-2489-8");
        System.out.println("Название книги - " + book.getBookTitle());
        System.out.println("Автор книги - " + book.getBookAuthor());
        System.out.println("Количество страниц - " + book.getNumberOfPages());
        System.out.println("ISBN - " + book.getIsbn());

        //Задание 2 - Создание Экземпляра класса Product через конструктор
        System.out.println();
        System.out.println("Задание 2:");
        Product product = new Product("Ушки творожные", "2100100200268");
        //Установим цену на продукт
        product.setPrice(112);
        //Выведем информацию на экран:
        System.out.println("Наименование товара - " + product.getName());
        System.out.println("Бар-код товара - " + product.getBarcode());
        System.out.println("Цена товара - " + product.getPrice() + " р.");
    }
}
