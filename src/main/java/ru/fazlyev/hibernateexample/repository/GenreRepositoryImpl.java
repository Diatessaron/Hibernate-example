package ru.fazlyev.hibernateexample.repository;

import org.springframework.stereotype.Repository;
import ru.fazlyev.hibernateexample.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryImpl implements GenreRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return (long) em.createQuery("select count(g) from Genre g").getSingleResult();
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public Genre getGenreByName(String name) {
        final TypedQuery<Genre> query = em.createQuery
                ("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);

        return query.getSingleResult();
    }

    @Override
    public List<Genre> getAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public void update(Genre genre) {
        final Query query = em.createQuery("update Genre g set g.name = :name where g.id = :id");
        query.setParameter("id", genre.getId());
        query.setParameter("name", genre.getName());
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        final Query query = em.createQuery("delete from Genre g where g.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
