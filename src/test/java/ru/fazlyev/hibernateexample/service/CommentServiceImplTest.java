package ru.fazlyev.hibernateexample.service;

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
import ru.fazlyev.hibernateexample.repository.BookRepositoryImpl;
import ru.fazlyev.hibernateexample.repository.CommentRepositoryImpl;

import javax.persistence.NoResultException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CommentRepositoryImpl.class, BookRepositoryImpl.class, CommentServiceImpl.class})
class CommentServiceImplTest {
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private TestEntityManager em;

    private final Book ulysses = new Book(1L, "Ulysses", new Author(1L, "James Joyce"),
            new Genre(1L, "Modernist novel"));
    private final Comment ulyssesComment = new Comment(1L, "Published in 1922", ulysses);

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveByComparing() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline and Punish", foucault,
                philosophy);
        final Comment expected = new Comment(0L, "Published in 1975", book);

        em.persist(book);
        commentService.saveComment(expected.getBook().getId(), expected.getContent());
        final Comment actual = commentService.getCommentById(2L);

        assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    void shouldReturnCorrectCommentById() {
        commentService.saveComment(ulyssesComment.getBook().getId(), ulyssesComment.getContent());
        final Comment actual = commentService.getCommentById(1L);

        assertEquals(ulyssesComment, actual);
    }

    @Test
    void shouldReturnCorrectCommentByContent() {
        final Comment actual = commentService.getCommentByContent(ulyssesComment.getContent());

        assertEquals(ulyssesComment, actual);
    }

    @Test
    void shouldThrowExceptionAfterGetCommentByContentMethodInvocation() {
        assertThrows(NoResultException.class, () -> commentService.getCommentByContent("comment"));
    }

    @Test
    void testGetCommentByBookMethod() {
        final List<Comment> expected = List.of(ulyssesComment);
        final List<Comment> actual = commentService.getCommentsByBook("Ulysses");

        assertEquals(expected, actual);
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

        em.persist(book);
        commentService.saveComment(disciplineAndPunishComment.getBook().getId(),
                disciplineAndPunishComment.getContent());
        final List<Comment> actual = commentService.getAll();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateCommentCorrectly() {
        commentService.updateComment(1L, 1L, "Comment");

        final Comment actualComment = em.find(Comment.class, 1L);
        assertThat(actualComment).isNotNull().matches(s -> !s.getContent().isBlank())
                .matches(s -> s.getContent().equals("Comment"));
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testDeleteByIdMethodByResultStringComparing() {
        final String expected = "Ulysses comment was deleted";
        final String actual = commentService.deleteById(1L);

        assertEquals(expected, actual);
    }
}
