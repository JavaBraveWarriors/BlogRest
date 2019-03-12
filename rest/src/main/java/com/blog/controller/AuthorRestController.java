package com.blog.controller;

import com.blog.model.Author;
import com.blog.model.PostListWrapper;
import com.blog.service.AuthorService;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;

/**
 * The type Author rest controller.
 *
 * @author Aliaksandr Yeutushenka
 * @see AuthorService
 * @see PostService
 */
@RestController
@RequestMapping("/authors")
public class AuthorRestController {

    private AuthorService authorService;
    private PostService postService;

    /**
     * Instantiates a new Author rest controller.
     *
     * @param authorService the author service
     * @param postService   the post service
     */
    @Autowired
    public AuthorRestController(AuthorService authorService, PostService postService) {
        this.authorService = authorService;
        this.postService = postService;
    }

    /**
     * Gets the {Author} object using author ID.
     *
     * @param userId the user id
     * @return {Author} is a object which has this ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Author getAuthorById(@PathVariable(value = "id") Long userId) {
        return authorService.getAuthorById(userId);
    }

    /**
     * Gets all author posts by author id.
     *
     * @param userId the user id
     * @return the all posts by author id
     */
    @GetMapping("/{id}/posts")
    @ResponseStatus(value = HttpStatus.OK)
    public PostListWrapper getAllPostsByAuthorId(@PathVariable(value = "id") Long userId) {
        return postService.getAllPostsByAuthorId(userId);
    }

    /**
     * Gets the Author object using author login.
     *
     * @param login is {String} value which identifies the author login.
     * @return {Author} is a object which has this login.
     */
    @GetMapping("/login/{login}")
    @ResponseStatus(value = HttpStatus.OK)
    public Author getAuthorByLogin(@PathVariable(value = "login") String login) {
        return authorService.getAuthorByLogin(login);
    }

    /**
     * Add new author.
     *
     * @param author            {Author} to be added.
     * @param validationResults the validation results of author object.
     * @return {Long} is the value that is the id of the new author.
     */
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long addAuthor(@Valid @RequestBody Author author, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            return authorService.addAuthor(author);
        }
    }

    /**
     * Update author.
     *
     * @param author            {Author} to be updated in the database.
     * @param validationResults the validation results of author object.
     */
    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateAuthor(@Valid @RequestBody Author author, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            authorService.updateAuthor(author);
        }
    }

    /**
     * Delete author.
     *
     * @param authorId is {Long} value which identifies the author ID.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteAuthor(@PathVariable(value = "id") Long authorId) {
        authorService.deleteAuthor(authorId);
    }
}
