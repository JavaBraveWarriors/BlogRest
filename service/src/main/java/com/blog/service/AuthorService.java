package com.blog.service;

import com.blog.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(Long userId);

    Author getAuthorByLogin(String login);

    Long addAuthor(Author author);

    void updateAuthor(Author author);

    void deleteAuthor(Long userId);

}
