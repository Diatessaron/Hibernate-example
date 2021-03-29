package ru.fazlyev.hibernateexample.repository;

import ru.fazlyev.hibernateexample.domain.Book;
import ru.fazlyev.hibernateexample.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    long count();

    Comment save(Comment comment);

    Optional<Comment> getCommentById(long id);

    Comment getCommentByContent(String content);

    List<Comment> getCommentsByBook(Book book);

    List<Comment> getAll();

    void update(Comment comment);

    void deleteById(long id);
}
