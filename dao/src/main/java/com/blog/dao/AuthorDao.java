package com.blog.dao;

import com.blog.Author;
import com.blog.dao.jdbc.AuthorDaoImpl;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * This interface defines various operations for easy database management for the Author object.
 * Use this interface if you want to access the Author database.
 *
 * @author Aliaksandr Yeutushenka
 * @see AuthorDaoImpl
 * @see Author
 */
public interface AuthorDao {

    /**
     * Gets the list of objects of the all authors from database.
     *
     * @return {List<Author>} is a list of all authors in the database.
     * @throws DataAccessException Will throw an error if the data is not access or the table {author} is empty.
     */
    List<Author> getAllAuthors() throws DataAccessException;

    /**
     * Gets the Author object from database using author ID.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {Author} is a object which has this ID.
     * @throws DataAccessException Will throw an error if the data is not access or the object is not exist in the database with such ID.
     */
    Author getAuthorById(final Long authorId) throws DataAccessException;

    /**
     * Gets the Author object from database using author login.
     *
     * @param login is {String} value which identifies the author login.
     * @return {Author} is a object which has this login.
     * @throws DataAccessException Will throw an error if the data is not access or the object is not exist in the database with such login.
     */
    Author getAuthorByLogin(final String login) throws DataAccessException;

    /**
     * Adds new author in database.
     *
     * @param author {Author} to be added to the database.
     * @return {Long} is the value that is the id of the new author.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Long addAuthor(final Author author) throws DataAccessException;

    /**
     * Updates author in database.
     *
     * @param author {Author} to be updated in the database.
     * @return {boolean} value, if update was successful - returned true, if not - false.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean updateAuthor(final Author author) throws DataAccessException;

    /**
     * Deletes author in database using author ID.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {boolean} value, if delete was successful - returned true, if not - false
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean deleteAuthor(final Long authorId) throws DataAccessException;

    /**
     * Checks for the presence in the author's database with this identifier.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {boolean} value, if there is an author with this identifier - returned true, if not - false
     */
    boolean checkAuthorById(final Long authorId);

    /**
     * Checks for the presence in the author's database with this login.
     *
     * @param login is {String} value which identifies the author login.
     * @return {boolean} value, if there is an author with this login - returned true, if not - false
     */
    boolean checkAuthorByLogin(final String login);
}
