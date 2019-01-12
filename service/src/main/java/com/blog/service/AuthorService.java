package com.blog.service;

import com.blog.Author;
import com.blog.dao.jdbc.AuthorDaoImpl;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;

import java.util.List;


/**
 * This interface defines various ways to manage object author with the correct business model.
 *
 * @author Aliaksandr Yeutushenka
 * @see AuthorDaoImpl
 * @see Author
 */
public interface AuthorService {

    /**
     * Gets the list of objects of the all authors.
     *
     * @return {List<Author>} is a list of all authors.
     */
    List<Author> getAllAuthors();

    /**
     * Gets the {Author} object using author ID.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {Author} is a object which has this ID.
     * @throws ValidationException Will throw an error if authorId is not valid.
     * @throws NotFoundException   Will throw an error if not found author with this authorId in database.
     */
    Author getAuthorById(Long authorId) throws ValidationException, NotFoundException;

    /**
     * Gets the Author object using author login.
     *
     * @param login is {String} value which identifies the author login.
     * @return {Author} is a object which has this login.
     * @throws ValidationException Will throw an error if login is not valid.
     * @throws NotFoundException   Will throw an error if not found author with this login in database.
     */
    Author getAuthorByLogin(String login) throws ValidationException, NotFoundException;

    /**
     * Add new author.
     *
     * @param author {Author} to be added to the database.
     * @return {Long} is the value that is the id of the new author.
     * @throws ValidationException Will throw an error if author is not valid.
     */
    Long addAuthor(Author author) throws ValidationException;

    /**
     * Update author.
     *
     * @param author {Author} to be updated in the database.
     * @throws NotFoundException       Will throw an error if not found author in database.
     * @throws InternalServerException Will throw an error if not updated this author.
     */
    void updateAuthor(Author author) throws NotFoundException, InternalServerException;

    /**
     * Delete author.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @throws ValidationException     Will throw an error if authorId is not valid.
     * @throws NotFoundException       Will throw an error if not found author with this authorId in database.
     * @throws InternalServerException Will throw an error if not deleted author with correct author id.
     */
    void deleteAuthor(Long authorId) throws ValidationException, NotFoundException, InternalServerException;

}
