package ru.fazlyev.hibernateexample.repository;

import org.hibernate.SessionFactory;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BookRepositoryImpl.class)
class BookRepositoryImplTest {
    @Autowired
    private BookRepositoryImpl repository;
    @Autowired
    private TestEntityManager em;

    private final Book expectedUlysses = new Book(1, "Ulysses", new Author(1, "James Joyce"),
            new Genre(1, "Modernist novel"));

    @Test
    void testSaveByComparing() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");

        final Book expected = new Book(0, "Discipline and Punish", foucault,
                philosophy);
        repository.save(expected);
        final Book actual = repository.getBookById(expected.getId()).orElseThrow
                (() -> new IllegalArgumentException("Incorrect id"));

        assertEquals(expected, actual);
    }

    @Test
    void shouldHavePositiveId() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline and Punish", foucault, philosophy);
        repository.save(book);

        assertThat(book.getId()).isPositive();
    }

    @Test
    void shouldHaveCorrectData() {
        final Author foucault = new Author(0, "Michel Foucault");
        final Genre philosophy = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline and Punish", foucault, philosophy);
        repository.save(book);
        Book actualBook = em.find(Book.class, book.getId());

        assertThat(actualBook).isNotNull().matches(s -> !s.getTitle().equals(""));
    }

    @Test
    void shouldReturnCorrectBookById() {
        final Book actual = repository.getBookById(1L).orElseThrow
                (() -> new IllegalArgumentException("Book with 1 id couldn't be found "));

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByTitle() {
        final Book actual = repository.getBookByTitle(expectedUlysses.getTitle());

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByAuthor() {
        final Book actual = repository.getBookByAuthor(expectedUlysses.getAuthor().getName());

        assertEquals(expectedUlysses, actual);
    }

    @Test
    void shouldReturnCorrectBookByGenre() {
        final Book actual = repository.getBookByGenre(expectedUlysses.getGenre().getName());

        assertEquals(expectedUlysses, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCorrectListOfBooks() {
        final Author author = new Author(0, "Michel Foucault");
        final Genre genre = new Genre(0, "Philosophy");
        final Book book = new Book(0, "Discipline And Punish", author, genre);
        final List<Book> expected = List.of(expectedUlysses, book);
        repository.save(book);
        final List<Book> actual = repository.getAll();

        assertEquals(expected, actual);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateByComparing() {
        final Author author = new Author(0L, "Michel Foucault");
        final Genre genre = new Genre(0L, "Philosophy");
        final Book expected = new Book(1L, "Discipline And Punish", author, genre);

        repository.save(expected);

        final Book actual = repository.getBookById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id"));

        assertThat(actual).isNotNull().matches(b -> b.getId() == expected.getId(),
                "Correct id")
                .matches(b -> !b.getTitle().equals(""), "Blank title")
                .matches(b -> b.getTitle().equals(expected.getTitle()), "Correct title")
                .matches(b -> b.getAuthor().getName().equals(expected.getAuthor().getName()),
                        "Correct author name")
                .matches(b -> b.getGenre().getName().equals(expected.getGenre().getName()),
                        "Correct genre name");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldCorrectDeleteBookById() {
        repository.deleteById(1);
        assertTrue(repository.getBookById(1).isEmpty());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void commentShouldBeDeletedBeforeBookDeletion() {
        repository.deleteById(1L);
        assertNull(em.find(Comment.class, 1L));
    }

    @Test
    void shouldReturnCorrectStudentsListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        final List<Book> books = repository.getAll();
        assertThat(books).isNotNull().hasSize(1)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenre() != null);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1L);
    }

    @Test
    void shouldReturnCorrectBookByTitleWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        final Book book = repository.getBookByTitle(expectedUlysses.getTitle());
        assertThat(book).isNotNull().matches(b -> !b.getTitle().equals(""), "Blank title")
                .matches(b -> b.getId() == expectedUlysses.getId(), "Correct id")
                .matches(b -> b.getAuthor() != null, "Correct author")
                .matches(b -> b.getGenre() != null, "Correct genre");

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1L);
    }
}
