package ru.fazlyev.hibernateexample.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.fazlyev.hibernateexample.domain.Author;
import ru.fazlyev.hibernateexample.repository.AuthorRepositoryImpl;

import javax.persistence.NoResultException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AuthorRepositoryImpl.class, AuthorServiceImpl.class})
class AuthorServiceImplTest {
    @Autowired
    private AuthorServiceImpl service;
    @Autowired
    private TestEntityManager em;

    private Author jamesJoyce = new Author(1L, "James Joyce");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveByComparing() {
        final Author foucault = new Author(0L, "Michel Foucault");
        service.saveAuthor(foucault.getName());

        final Author actual = service.getAuthorById(2L);

        assertEquals(foucault.getName(), actual.getName());
    }

    @Test
    void shouldReturnCorrectAuthorById() {
        final Author actual = service.getAuthorById(1L);

        assertEquals(jamesJoyce, actual);
    }

    @Test
    void shouldReturnCorrectAuthorByName() {
        final Author actual = service.getAuthorByName(jamesJoyce.getName());

        assertEquals(jamesJoyce, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetAuthorByNameMethodInvocation() {
        assertThrows(NoResultException.class, () -> service.getAuthorByName("author"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectListOfAuthor() {
        final Author foucault = new Author(0, "Michel Foucault");
        final List<Author> expected = List.of(this.jamesJoyce, foucault);

        service.saveAuthor(foucault.getName());

        final List<Author> actual = service.getAll();

        assertThat(actual).isNotNull().matches(a -> a.size() == expected.size())
                .matches(a -> a.get(0).getName().equals(expected.get(0).getName()))
                .matches(a -> a.get(1).getName().equals(expected.get(1).getName()));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateAuthorMethodByComparing() {
        service.updateAuthor(1L, "Author");

        final Author actualAuthor = em.find(Author.class, 1L);
        assertThat(actualAuthor).isNotNull().matches(s -> !s.getName().isBlank())
                .matches(s -> s.getName().equals("Author"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void authorShouldBeDeletedCorrectly() {
        final String expected = "James Joyce was deleted";
        final String actual = service.deleteAuthorById(1L);

        assertEquals(expected, actual);
    }
}
