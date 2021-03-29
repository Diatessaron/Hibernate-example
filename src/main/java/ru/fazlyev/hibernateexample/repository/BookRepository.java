package ru.fazlyev.hibernateexample.repository;

import ru.fazlyev.hibernateexample.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> getBookById(long id);

    Book getBookByTitle(String title);

    Book getBookByAuthor(String author);

    Book getBookByGenre(String genre);

    Book getBookByComment(String comment);

    List<Book> getAll();

    void deleteById(long id);
}
