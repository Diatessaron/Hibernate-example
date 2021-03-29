package ru.fazlyev.hibernateexample.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fazlyev.hibernateexample.domain.Author;
import ru.fazlyev.hibernateexample.domain.Book;
import ru.fazlyev.hibernateexample.domain.Genre;
import ru.fazlyev.hibernateexample.repository.AuthorRepository;
import ru.fazlyev.hibernateexample.repository.BookRepository;
import ru.fazlyev.hibernateexample.repository.GenreRepository;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    @Override
    public void saveBook(String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(0L, title, author, genre);

        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookById(long id) {
        return bookRepository.getBookById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect book id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByTitle(String title) {
        return bookRepository.getBookByTitle(title);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByAuthor(String author) {
        return bookRepository.getBookByAuthor(author);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByGenre(String genre) {
        return bookRepository.getBookByGenre(genre);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByComment(String comment) {
        return bookRepository.getBookByComment(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAll() {
        return bookRepository.getAll();
    }

    @Transactional
    @Override
    public void updateBook(long id, String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(id, title, author, genre);

        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBookById(long id) {
        bookRepository.deleteById(id);
    }

    private Author getAuthor(String authorName) {
        try {
            return authorRepository.getAuthorByName(authorName);
        } catch (EmptyResultDataAccessException | NoResultException e) {
            Author author = new Author(0L, authorName);
            authorRepository.save(author);
            return author;
        }
    }

    private Genre getGenre(String genreName) {
        try {
            return genreRepository.getGenreByName(genreName);
        } catch (EmptyResultDataAccessException | NoResultException e) {
            Genre genre = new Genre(0L, genreName);
            genreRepository.save(genre);
            return genre;
        }
    }
}
