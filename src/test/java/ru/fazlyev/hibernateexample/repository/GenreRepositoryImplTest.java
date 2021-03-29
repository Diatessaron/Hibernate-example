package ru.fazlyev.hibernateexample.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.fazlyev.hibernateexample.domain.Book;
import ru.fazlyev.hibernateexample.domain.Genre;

import javax.persistence.NoResultException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(GenreRepositoryImpl.class)
class GenreRepositoryImplTest {
    @Autowired
    private GenreRepositoryImpl repository;
    @Autowired
    private TestEntityManager em;

    private final Genre expectedNovel = new Genre(1, "Modernist novel");

    @Test
    void testCountMethod() {
        final long expected = 1L;
        final long actual = repository.count();

        assertEquals(expected, actual);
    }

    @Test
    void shouldHavePositiveId() {
        final Genre genre = new Genre(0, "Modernist novel");
        repository.save(genre);

        assertThat(genre.getId()).isPositive();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldHaveCorrectData(){
        final Genre genre = new Genre(0, "Modernist novel");
        repository.save(genre);
        Genre actualGenre = em.find(Genre.class, genre.getId());

        assertThat(actualGenre).isNotNull().matches(s -> !s.getName().equals(""));
    }

    @Test
    void shouldReturnCorrectGenreById() {
        final Genre actual = repository.getGenreById(1L).orElseThrow
                (() -> new IllegalArgumentException("Genre with 1 id couldn't be found "));

        assertEquals(expectedNovel, actual);
    }

    @Test
    void shouldReturnCorrectGenreByName() {
        final Genre actual = repository.getGenreByName(expectedNovel.getName());

        assertEquals(expectedNovel, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetGenreByNameMethodInvocation(){
        assertThrows(NoResultException.class, () -> repository.getGenreByName("genre"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectListOfGenres() {
        final Genre philosophy = new Genre(2, "Philosophy");
        final List<Genre> expected = List.of(expectedNovel, philosophy);
        repository.save(philosophy);
        final List<Genre> actual = repository.getAll();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Genre expected = new Genre(1, "Philosophy");
        repository.update(expected);
        final Genre actual = repository.getGenreById(1L).get();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteGenreById() {
        repository.deleteById(1L);
        assertTrue(repository.getGenreById(1).isEmpty());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectlyDeleteBookBeforeGenreDeletion(){
        repository.deleteById(1L);
        assertNull(em.find(Book.class, 1L));
    }
}
