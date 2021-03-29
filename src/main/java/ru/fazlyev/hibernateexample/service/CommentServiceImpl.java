package ru.fazlyev.hibernateexample.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fazlyev.hibernateexample.domain.Book;
import ru.fazlyev.hibernateexample.domain.Comment;
import ru.fazlyev.hibernateexample.repository.BookRepository;
import ru.fazlyev.hibernateexample.repository.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public String saveComment(long bookId, String commentContent) {
        final Book book = bookRepository.getBookById(bookId).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id"));
        final Comment comment = new Comment(0L, commentContent, book);

        commentRepository.save(comment);
        bookRepository.save(book);

        return "You successfully added a comment to " + book.getTitle();
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentById(long id) {
        return commentRepository.getCommentById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentByContent(String content) {
        return commentRepository.getCommentByContent(content);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByBook(String bookTitle) {
        return commentRepository.getCommentsByBook(bookRepository.getBookByTitle(bookTitle));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.getAll();
    }

    @Transactional
    @Override
    public String updateComment(long bookId, long commentId, String commentContent) {
        final Book book = bookRepository.getBookById(bookId).orElseThrow(
                () -> new IllegalArgumentException("Incorrect id"));
        final Comment comment = new Comment(commentId, commentContent, book);

        commentRepository.update(comment);
        bookRepository.save(book);

        return book.getTitle() + " comment was updated";
    }

    @Transactional
    @Override
    public String deleteById(long id) {
        final Comment comment = commentRepository.getCommentById(id).orElseThrow(
                () -> new IllegalArgumentException("Incorrect comment id"));

        final Book book = bookRepository.getBookByComment(comment.getContent());
        commentRepository.deleteById(id);

        return book.getTitle() + " comment was deleted";
    }
}
