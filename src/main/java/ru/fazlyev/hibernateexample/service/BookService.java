package ru.fazlyev.hibernateexample.service;

import ru.fazlyev.hibernateexample.domain.Book;

import java.util.List;

public interface BookService {
    void saveBook(String title, String authorNameParameter, String genreNameParameter);

    Book getBookById(long id);

    Book getBookByTitle(String title);

    Book getBookByAuthor(String author);

    Book getBookByGenre(String genre);

    Book getBookByComment(String comment);

    List<Book> getAll();

    void updateBook(long id, String title, String authorNamePArameter, String genreNameParameter);

    void deleteBookById(long id);
}
