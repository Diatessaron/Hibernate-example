package ru.fazlyev.hibernateexample.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.fazlyev.hibernateexample.domain.Author;
import ru.fazlyev.hibernateexample.domain.Book;

import javax.persistence.NoResultException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AuthorRepositoryImpl.class)
class AuthorRepositoryImplTest {
    @Autowired
    private AuthorRepositoryImpl repository;
    @Autowired
    private TestEntityManager em;

    public final Author jamesJoyce = new Author(1L, "James Joyce");

    @Test
    void testCountMethod() {
        final long expected = 1L;
        final long actual = repository.count();

        assertEquals(expected, actual);
    }

    @Test
    void shouldHavePositiveId() {
        final Author jamesJoyce = new Author(0, "James Joyce");
        repository.save(jamesJoyce);

        assertThat(jamesJoyce.getId()).isPositive();
    }

    @Test
    void shouldHaveCorrectData() {
        final Author jamesJoyce = new Author(0, "James Joyce");
        repository.save(jamesJoyce);
        Author actualAuthor = em.find(Author.class, jamesJoyce.getId());

        assertThat(actualAuthor).isNotNull().matches(s -> !s.getName().equals(""));
    }

    @Test
    void shouldReturnCorrectAuthorById() {
        final Author actual = repository.getAuthorById(1L).orElseThrow
                (() -> new IllegalArgumentException("Author with 1 id couldn't be found "));

        assertEquals(jamesJoyce, actual);
    }

    @Test
    void shouldReturnCorrectAuthorByName() {
        final Author actual = repository.getAuthorByName(jamesJoyce.getName());

        assertEquals(jamesJoyce, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetAuthorByNameMethodInvocation() {
        assertThrows(NoResultException.class, () -> repository.getAuthorByName("author"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectListOfAuthors() {
        final Author foucault = new Author(2, "Michel Foucault");
        final List<Author> expected = List.of(jamesJoyce, foucault);
        repository.save(foucault);
        final List<Author> actual = repository.getAll();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Author expected = new Author(1, "Michel Foucault");
        repository.update(expected);
        final Author actual = repository.getAuthorById(1L).get();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteAuthorById() {
        repository.deleteById(1);
        assertTrue(repository.getAuthorById(1).isEmpty());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectlyDeleteBookBeforeAuthorDeletion() {
        repository.deleteById(1L);
        assertNull(em.find(Book.class, 1L));
    }
}
