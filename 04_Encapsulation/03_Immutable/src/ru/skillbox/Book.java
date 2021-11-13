package ru.skillbox;

public class Book {
    private final String bookTitle;
    private final String bookAuthor;
    private final int numberOfPages;
    private final String isbn;

    public Book(String bookTitle, String bookAuthor, int numberOfPages, String isbn) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.numberOfPages = numberOfPages;
        this.isbn = isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public String getIsbn() {
        return isbn;
    }
}
