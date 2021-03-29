package ru.fazlyev.hibernateexample.repository;

import org.springframework.stereotype.Repository;
import ru.fazlyev.hibernateexample.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return (long) em.createQuery("select count(a) from Author a").getSingleResult();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public Optional<Author> getAuthorById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Author getAuthorByName(String name) {
        final TypedQuery<Author> query = em.createQuery
                ("select a from Author a where a.name = :name", Author.class);
        query.setParameter("name", name);

        return query.getSingleResult();
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public void update(Author author) {
        final Query query = em.createQuery("update Author a set a.name = :name where a.id = :id");
        query.setParameter("id", author.getId());
        query.setParameter("name", author.getName());
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        final Query query = em.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
