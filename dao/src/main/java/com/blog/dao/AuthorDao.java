package com.blog.dao;

import com.blog.model.Author;

import java.util.List;

/**
 * This interface defines various operations for easy management for the Author object.
 * Use this interface if you want to access the Author.
 *
 * @author Aliaksandr Yeutushenka
 * @see Author
 */
public interface AuthorDao {

    /**
     * Gets the list of objects of the all authors.
     *
     * @return {List<Author>} is a list of all authors.
     */
    List<Author> getAllAuthors();

    /**
     * Gets the Author object using author ID.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {Author} is a object which has this ID.
     */
    Author getAuthorById(final Long authorId);

    /**
     * Gets the Author object using author login.
     *
     * @param login is {String} value which identifies the author login.
     * @return {Author} is a object which has this login.
     */
    Author getAuthorByLogin(final String login);

    /**
     * Adds new author.
     *
     * @param author {Author} to be added.
     * @return {Long} is the value that is the id of the new author.
     */
    Long addAuthor(final Author author);

    /**
     * Updates author.
     *
     * @param author {Author} to be updated.
     * @return {boolean} value, if update was successful - returned true, if not - false.
     */
    boolean updateAuthor(final Author author);

    /**
     * Deletes author using author ID.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {boolean} value, if delete was successful - returned true, if not - false
     */
    boolean deleteAuthor(final Long authorId);

    /**
     * Checks for the presence in the author's with this identifier.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {boolean} value, if there is an author with this identifier - returned true, if not - false
     */
    boolean checkAuthorById(final Long authorId);

    /**
     * Checks for the presence in the author's with this login.
     *
     * @param login is {String} value which identifies the author login.
     * @return {boolean} value, if there is an author with this login - returned true, if not - false
     */
    boolean checkAuthorByLogin(final String login);
}