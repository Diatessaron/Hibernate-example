package ru.fazlyev.hibernateexample.service;

import ru.fazlyev.hibernateexample.domain.Comment;

import java.util.List;

public interface CommentService {
    String saveComment(long bookId, String commentContent);

    Comment getCommentById(long id);

    Comment getCommentByContent(String content);

    List<Comment> getCommentsByBook(String bookTitle);

    List<Comment> getAll();

    String updateComment(long bookId, long commentId, String commentContent);

    String deleteById(long id);
}
