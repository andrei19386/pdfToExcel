package ru.skillbox;

public class Book {
    private String bookTitle;
    private String author;
    private int numberOfPages;
    private int price;

    public Book(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Book(String bookTitle, String author) {
        this.bookTitle = bookTitle;
        this.author = author;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
