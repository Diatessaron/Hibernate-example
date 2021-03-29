package ru.fazlyev.hibernateexample.repository;

import ru.fazlyev.hibernateexample.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    long count();

    Genre save(Genre genre);

    Optional<Genre> getGenreById(long id);

    Genre getGenreByName(String name);

    List<Genre> getAll();

    void update(Genre genre);

    void deleteById(long id);
}
