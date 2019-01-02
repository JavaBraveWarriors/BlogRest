package com.yeutushenka.service;

import com.yeutushenka.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(Long userId);

    Author getAuthorByLogin(String login);

    Long addAuthor(Author author);

    int updateAuthor(Author author);

    int deleteAuthor(Long userId);

}
