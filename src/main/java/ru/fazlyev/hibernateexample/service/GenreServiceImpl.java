package ru.fazlyev.hibernateexample.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fazlyev.hibernateexample.domain.Genre;
import ru.fazlyev.hibernateexample.repository.GenreRepository;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    @Override
    public String saveGenre(String name) {
        final Genre genre = new Genre(0L, name);
        genreRepository.save(genre);

        return String.format("You successfully saved a %s to repository", genre.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreById(long id) {
        return genreRepository.getGenreById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreByName(String name) {
        return genreRepository.getGenreByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAll() {
        return genreRepository.getAll();
    }

    @Transactional
    @Override
    public String updateGenre(long id, String name) {
        final Genre genre = new Genre(id, name);
        genreRepository.update(genre);

        return String.format("%s was updated", genre.getName());
    }

    @Transactional
    @Override
    public String deleteGenreById(long id) {
        final Genre genre = genreRepository.getGenreById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id"));
        genreRepository.deleteById(id);

        return String.format("%s was deleted", genre.getName());
    }
}
