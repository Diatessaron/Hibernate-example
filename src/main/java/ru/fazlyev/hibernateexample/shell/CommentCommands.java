package ru.fazlyev.hibernateexample.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.fazlyev.hibernateexample.service.CommentService;

@ShellComponent
public class CommentCommands {
    private final CommentService service;

    public CommentCommands(CommentService service) {
        this.service = service;
    }

    @ShellMethod(key = {"ci", "cInsert"}, value = "Insert comment. Arguments: book id, comment. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String insert(@ShellOption("BookId") long bookId,
                         @ShellOption("Comment") String commentContent) {
        return service.saveComment(bookId, reformatString(commentContent));
    }

    @ShellMethod(key = {"cbi", "cById", "commentById"}, value = "Get comment by id")
    public String getCommentById(@ShellOption("Id") long id) {
        return service.getCommentById(id).toString();
    }

    @ShellMethod(key = {"cbc", "cByContent", "commentByContent"}, value = "Get comment by content" +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getCommentByContent(@ShellOption("Content") String content) {
        return service.getCommentByContent(reformatString(content)).toString();
    }

    @ShellMethod(key = {"cbb", "cByBook", "commentByBook"}, value = "Get comment by book title" +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getCommentByBook(@ShellOption("Book title") String bookTitle) {
        return service.getCommentsByBook(bookTitle).toString();
    }

    @ShellMethod(key = {"cga", "cGetAll"}, value = "Get all comments")
    public String getAll() {
        return service.getAll().toString();
    }

    @ShellMethod(key = {"cu", "cUpdate"}, value = "Update comment in repository. Arguments: bookId, commentId, " +
            "name. Please, put comma instead of space in each argument or simply put the arguments " +
            "in quotes.")
    public String update(@ShellOption("BookId") long bookId,
                         @ShellOption("CommentId to replace") long commentId,
                         @ShellOption("Content") String commentContent) {
        return service.updateComment(bookId, commentId, commentContent);
    }

    @ShellMethod(key = {"cd", "cDelete"}, value = "Delete comment by id")
    public String deleteById(@ShellOption("Id") long id) {
        return service.deleteById(id);
    }

    private String reformatString(String str) {
        return String.join(" ", str.split(","));
    }
}
