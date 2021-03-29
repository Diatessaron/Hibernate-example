package ru.fazlyev.hibernateexample.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fazlyev.hibernateexample.domain.Author;
import ru.fazlyev.hibernateexample.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    @Override
    public String saveAuthor(String name) {
        final Author author = new Author(0L, name);
        authorRepository.save(author);
        return String.format("You successfully saved a %s to repository", author.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public Author getAuthorById(long id) {
        return authorRepository.getAuthorById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Author getAuthorByName(String name) {
        return authorRepository.getAuthorByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorRepository.getAll();
    }

    @Transactional
    @Override
    public String updateAuthor(long id, String name) {
        final Author author = new Author(id, String.join(" ", name.split(",")));
        authorRepository.update(author);

        return String.format("%s was updated", author.getName());
    }

    @Transactional
    @Override
    public String deleteAuthorById(long id) {
        final Author author = authorRepository.getAuthorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id"));
        authorRepository.deleteById(id);

        return String.format("%s was deleted", author.getName());
    }
}
