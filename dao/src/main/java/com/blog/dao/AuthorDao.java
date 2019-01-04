package com.blog.dao;

import com.blog.Author;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AuthorDao {

    List<Author> getAllAuthors() throws DataAccessException;

    Author getAuthorById(Long userId) throws DataAccessException;

    Author getAuthorByLogin(String login) throws DataAccessException;

    Long addAuthor(Author author) throws DataAccessException;

    int updateAuthor(Author author) throws DataAccessException;

    int deleteAuthor(Long userId) throws DataAccessException;

    boolean checkAuthorById(Long userId);

    boolean checkAuthorByLogin(String login);

}
