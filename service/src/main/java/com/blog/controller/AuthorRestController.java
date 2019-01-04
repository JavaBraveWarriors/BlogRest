package com.blog.controller;

import com.blog.Author;
import com.blog.Post;
import com.blog.service.AuthorService;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorRestController {

    private AuthorService authorService;
    private PostService postService;

    @Autowired
    public AuthorRestController(AuthorService authorService, PostService postService) {
        this.authorService = authorService;
        this.postService = postService;
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Author getAuthorById(@PathVariable(value = "id") Long userId) {
        return authorService.getAuthorById(userId);
    }

    @GetMapping("/{id}/posts")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getAllPostsByAuthorId(@PathVariable(value = "id") Long userId) {
        return postService.getAllPostsByAuthorId(userId);
    }

    @GetMapping("/login/{login}")
    @ResponseStatus(value = HttpStatus.OK)
    public Author getAuthorByLogin(@PathVariable(value = "login") String login) {
        return authorService.getAuthorByLogin(login);
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long addAuthor(@RequestBody Author author, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result.getFieldErrors().toString());// TODO: протестировать!!!
        } else {
            return authorService.addAuthor(author);
        }
    }

    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateAuthor(@Valid @RequestBody Author author) {
        authorService.updateAuthor(author);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteAuthor(@PathVariable(value = "id") Long userId) {
        authorService.deleteAuthor(userId);
    }


}
