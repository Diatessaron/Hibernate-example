package ru.fazlyev.hibernateexample.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.fazlyev.hibernateexample.service.GenreService;

@ShellComponent
public class GenreCommands {
    private final GenreService genreService;

    public GenreCommands(GenreService genreService) {
        this.genreService = genreService;
    }

    @ShellMethod(key = {"gi", "gInsert"}, value = "Insert genre. Arguments: name. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String insert(@ShellOption("Name") String name) {
        return genreService.saveGenre(reformatString(name));
    }

    @ShellMethod(key = {"gbi", "genreById", "gById"}, value = "Get genre by id")
    public String getGenreById(@ShellOption("Id") long id) {
        return genreService.getGenreById(id).toString();
    }

    @ShellMethod(key = {"gbn", "genreByName", "gByName"}, value = "Get genre by name. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String getGenreByName(@ShellOption("Name") String name) {
        return genreService.getGenreByName(reformatString(name)).toString();
    }

    @ShellMethod(key = {"gga", "gGetAll"}, value = "Get all genres")
    public String getAll() {
        return genreService.getAll().toString();
    }

    @ShellMethod(key = {"gu", "gUpdate"}, value = "Update genre in repository. Arguments: id, name. " +
            "Please, put comma instead of space in each argument or simply put the arguments in quotes.")
    public String update(@ShellOption("Id") long id,
                         @ShellOption("Name") String name) {
        return genreService.updateGenre(id, reformatString(name));
    }

    @ShellMethod(key = {"gd", "gDelete"}, value = "Delete genre by id")
    public String deleteById(@ShellOption("Id") long id) {
        return genreService.deleteGenreById(id);
    }

    private String reformatString(String str) {
        return String.join(" ", str.split(","));
    }
}
