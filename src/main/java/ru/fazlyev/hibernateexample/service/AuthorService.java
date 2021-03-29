package ru.fazlyev.hibernateexample.service;

import ru.fazlyev.hibernateexample.domain.Author;

import java.util.List;

public interface AuthorService {
    String saveAuthor(String name);

    Author getAuthorById(long id);

    Author getAuthorByName(String name);

    List<Author> getAll();

    String updateAuthor(long id, String name);

    String deleteAuthorById(long id);
}
