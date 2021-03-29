package ru.fazlyev.hibernateexample.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.fazlyev.hibernateexample.domain.Genre;
import ru.fazlyev.hibernateexample.repository.GenreRepositoryImpl;

import javax.persistence.NoResultException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({GenreRepositoryImpl.class, GenreServiceImpl.class})
class GenreServiceImplTest {
    @Autowired
    private GenreServiceImpl service;
    @Autowired
    private TestEntityManager em;

    private final Genre expectedNovel = new Genre(1L, "Modernist novel");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveByComparing() {
        final Genre philosophy = new Genre(0L, "Philosophy");
        service.saveGenre(philosophy.getName());

        final Genre actual = service.getGenreById(2L);

        assertEquals(philosophy.getName(), actual.getName());
    }

    @Test
    void shouldReturnCorrectGenreById() {
        final Genre actual = service.getGenreById(1L);

        assertEquals(expectedNovel, actual);
    }

    @Test
    void shouldReturnCorrectGenreByName() {
        final Genre actual = service.getGenreByName(expectedNovel.getName());

        assertEquals(expectedNovel, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetGenreByNameMethodInvocation() {
        assertThrows(NoResultException.class, () -> service.getGenreByName("genre"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectListOfGenre() {
        final Genre philosophy = new Genre(0, "Philosophy");
        final List<Genre> expected = List.of(expectedNovel, philosophy);

        service.saveGenre(philosophy.getName());

        final List<Genre> actual = service.getAll();

        assertThat(actual).isNotNull().matches(a -> a.size() == expected.size())
                .matches(a -> a.get(0).getName().equals(expected.get(0).getName()))
                .matches(a -> a.get(1).getName().equals(expected.get(1).getName()));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateAuthorMethodByComparing() {
        service.updateGenre(1L, "Genre");

        final Genre actualGenre = em.find(Genre.class, 1L);
        assertThat(actualGenre).isNotNull().matches(s -> !s.getName().isBlank())
                .matches(s -> s.getName().equals("Genre"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void authorShouldBeDeletedCorrectly() {
        final String expected = "Modernist novel was deleted";
        final String actual = service.deleteGenreById(1L);

        assertEquals(expected, actual);
    }
}
