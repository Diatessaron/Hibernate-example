package ru.fazlyev.hibernateexample.repository;

import ru.fazlyev.hibernateexample.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    long count();

    Author save(Author author);

    Optional<Author> getAuthorById(long id);

    Author getAuthorByName(String name);

    List<Author> getAll();

    void update(Author author);

    void deleteById(long id);
}
