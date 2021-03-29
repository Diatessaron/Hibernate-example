package ru.fazlyev.hibernateexample.shell;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.fazlyev.hibernateexample.domain.Author;
import ru.fazlyev.hibernateexample.repository.AuthorRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorCommandsTest {
    @MockBean
    private AuthorRepositoryImpl authorRepository;

    @Autowired
    private Shell shell;
    public final Author jamesJoyce = new Author(1, "James Joyce");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testInsertMethodByTimesOfInvocation() {
        shell.evaluate(() -> "aInsert Michel,Foucault");

        verify(authorRepository, times(1))
                .save(new Author(0L, "Michel Foucault"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectMessage() {
        final String expected = "You successfully saved a Michel Foucault to repository";
        final String actual = shell.evaluate(() -> "aInsert Michel,Foucault").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAuthorByIdByMessageComparison() {
        when(authorRepository.getAuthorById(1)).thenReturn(Optional.of(jamesJoyce));
        final String expected = jamesJoyce.toString();
        final String actual = shell.evaluate(() -> "authorById 1").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAuthorByNameByMessageComparison() {
        when(authorRepository.getAuthorByName(jamesJoyce.getName())).thenReturn(jamesJoyce);
        final String expected = jamesJoyce.toString();
        final String actual = shell.evaluate(() -> "authorByName James,Joyce").toString();

        assertEquals(expected, actual);
    }

    @Test
    void testGetAllByMessageComparison() {
        when(authorRepository.getAll()).thenReturn(List.of(jamesJoyce));
        final String expected = List.of(jamesJoyce).toString();
        final String actual = shell.evaluate(() -> "aGetAll").toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectMessageAfterUpdateMethod() {
        final String expected = "Michel Foucault was updated";
        final String actual = shell.evaluate(() -> "aUpdate 1 Michel,Foucault").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectMessageAfterDeleteMethod() {
        when(authorRepository.getAuthorById(1)).thenReturn(Optional.of(jamesJoyce));

        final String expected = "James Joyce was deleted";
        final String actual = shell.evaluate(() -> "aDelete 1").toString();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void bookShouldBeDeletedBeforeAuthorDeletion() {
        final String authorName = jamesJoyce.getName();
        when(authorRepository.getAuthorById(1)).thenReturn(Optional.of(jamesJoyce));
        shell.evaluate(() -> "aDelete 1");

        final InOrder inOrder = inOrder(authorRepository);
        inOrder.verify(authorRepository).getAuthorById(1);
        inOrder.verify(authorRepository).deleteById(1);
    }
}
