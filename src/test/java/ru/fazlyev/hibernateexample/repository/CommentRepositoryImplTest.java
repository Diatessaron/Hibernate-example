package ru.fazlyev.hibernateexample.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.fazlyev.hibernateexample.domain.Author;
import ru.fazlyev.hibernateexample.domain.Book;
import ru.fazlyev.hibernateexample.domain.Comment;
import ru.fazlyev.hibernateexample.domain.Genre;

import javax.persistence.NoResultException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CommentRepositoryImpl.class)
class CommentRepositoryImplTest {
    @Autowired
    private CommentRepositoryImpl repository;
    @Autowired
    private TestEntityManager em;

    private final Book ulysses = new Book(1L, "Ulysses", new Author(1L, "James Joyce"),
            new Genre(1L, "Modernist novel"));
    private final Comment ulyssesComment = new Comment(1L, "Published in 1922", ulysses);

    @Test
    void testCountMethod() {
        final long expected = 1L;
        final long actual = repository.count();

        assertEquals(expected, actual);
    }

    @Test
    void testSaveByComparing() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline and Punish", foucault,
                philosophy);

        final Comment expected = new Comment(0L, "Published in 1975", book);
        repository.save(expected);
        final Comment actual = repository.getCommentById(2L).orElseThrow(() ->
                new IllegalArgumentException("Incorrect id"));

        assertEquals(expected, actual);
    }

    @Test
    void shouldHavePositiveId() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");

        final Book book = new Book(0, "Discipline and Punish", foucault,
                philosophy);

        final Comment comment = new Comment(0L, "Published in 1975", book);
        repository.save(comment);

        assertThat(comment.getId()).isPositive();
    }

    @Test
    void shouldHaveCorrectData() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline and Punish", foucault,
                philosophy);
        final Comment comment = new Comment(0L, "Published in 1975", book);
        repository.save(comment);

        final Comment actualComment = em.find(Comment.class, comment.getId());

        assertThat(actualComment).isNotNull().matches(c -> !c.getContent().equals(""))
                .matches(c -> c.getContent().equals("Published in 1975"))
                .matches(c -> c.getId() == 2L)
                .matches(c -> c.getBook() != null)
                .matches(c -> c.getBook().toString().equals(book.toString()));
    }

    @Test
    void shouldReturnCorrectCommentById() {
        repository.save(ulyssesComment);
        final Comment actual = repository.getCommentById(1L).orElseThrow(() ->
                new IllegalArgumentException("Incorrect id"));

        assertEquals(ulyssesComment, actual);
    }

    @Test
    void shouldReturnCorrectCommentByContent() {
        repository.save(ulyssesComment);
        final Comment actual = repository.getCommentByContent(ulyssesComment.getContent());

        assertEquals(ulyssesComment, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetCommentByNameMethodInvocation() {
        assertThrows(NoResultException.class, () -> repository.getCommentByContent("comment"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectListOfComments() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline and Punish", foucault,
                philosophy);

        final Comment disciplineAndPunishComment = new Comment(2L, "Published in 1975", book);
        final List<Comment> expected = List.of(this.ulyssesComment, disciplineAndPunishComment);

        repository.save(disciplineAndPunishComment);
        final List<Comment> actual = repository.getAll();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Comment expected = new Comment(1L, "Published in 1975", ulysses);
        repository.update(expected);
        final Comment actual = repository.getCommentById(1L).orElseThrow(() ->
                new IllegalArgumentException("Incorrect id"));

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteCommentById() {
        repository.deleteById(1);
        assertTrue(repository.getCommentById(1L).isEmpty());
    }
}
