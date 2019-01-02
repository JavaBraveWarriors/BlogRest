package com.yeutushenka.controller;

import com.yeutushenka.Author;
import com.yeutushenka.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class UserRestController {
    private AuthorService authorService;

    @Autowired
    public UserRestController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/all")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.FOUND)
    public Author getAuthorById(@PathVariable(value = "id") Long userId) {
        return authorService.getAuthorById(userId);
    }

    @GetMapping("/login/{login}")
    @ResponseStatus(value = HttpStatus.FOUND)
    public Author getAuthorByLogin(@PathVariable(value = "login") String login) {
        return authorService.getAuthorByLogin(login);
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long addAuthor(@Valid @RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateAuthor(@Valid @RequestBody Author author) {
        authorService.updateAuthor(author);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable(value = "id") Long userId) {
        authorService.deleteAuthor(userId);
    }


}
