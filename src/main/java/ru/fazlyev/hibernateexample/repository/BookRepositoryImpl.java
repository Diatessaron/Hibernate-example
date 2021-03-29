package ru.fazlyev.hibernateexample.repository;

import org.springframework.stereotype.Repository;
import ru.fazlyev.hibernateexample.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public Optional<Book> getBookById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public Book getBookByTitle(String title) {
        final TypedQuery<Book> query = em.createQuery
                ("select b from Book b join fetch b.genre g join fetch b.author a " +
                        "where b.title = :title", Book.class);
        query.setParameter("title", title);

        return query.getSingleResult();
    }

    @Override
    public Book getBookByAuthor(String author) {
        final TypedQuery<Book> query = em.createQuery
                ("select b from Book b join fetch b.genre g join fetch b.author a " +
                        "where b.author.name = :author", Book.class);
        query.setParameter("author", author);

        return query.getSingleResult();
    }

    @Override
    public Book getBookByGenre(String genre) {
        final TypedQuery<Book> query = em.createQuery
                ("select b from Book b join fetch b.genre g join fetch b.author a " +
                        "where b.genre.name = :genre", Book.class);
        query.setParameter("genre", genre);

        return query.getSingleResult();
    }

    @Override
    public Book getBookByComment(String comment) {
        final TypedQuery<Book> query = em.createQuery(
                "select b from Book b join fetch b.genre g join fetch b.author a " +
                        "left join fetch Comment c on c.book = b " +
                        "where c.content = :content", Book.class
        );
        query.setParameter("content", comment);

        return query.getSingleResult();
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b join fetch b.genre g join fetch b.author a",
                Book.class).getResultList();
    }

    @Override
    public void deleteById(long id) {
        final Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
