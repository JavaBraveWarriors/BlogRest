package com.blog.dao;

import com.blog.dao.jdbc.TagDaoImpl;
import com.blog.model.Tag;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * This interface defines various operations for easy database management for the Post object.
 * Use this interface if you want to access the Post database.
 *
 * @author Aliaksandr Yeutushenka
 * @see Tag
 * @see TagDaoImpl
 */
public interface TagDao {

    /**
     * Gets the list of objects of the all tags from database.
     *
     * @return {List<Tag>} is a list of all tags from the database.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    List<Tag> getAllTags();

    /**
     * Gets a {Tag} object where id is equal to argument parameter
     *
     * @param id {Long} value the ID of the tag you want to get.
     * @return {Tag} is a object which has this ID.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Tag getTagById(final Long id);

    /**
     * Gets a list of tags objects where is this post from the database.
     *
     * @param postId is {Long} value post ID
     * @return {List<Tag>} is a list of tags from the database.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    List<Tag> getAllTagsByPostId(final Long postId) throws DataAccessException;

    /**
     * Adds new tag in database.
     *
     * @param tag {Tag} to be added to the database.
     * @return {Long} is the value that is the id of the new tag.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Long addTag(final Tag tag) throws DataAccessException;

    /**
     * Updates tag in database.
     *
     * @param tag {Tag} to be updated in the database.
     * @return {boolean} value, if update was successful - returned true, if not - false.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean updateTag(final Tag tag) throws DataAccessException;

    /**
     * Deletes tag in database using tag ID.
     *
     * @param id is {Long} value which identifies the tag ID.
     * @return {boolean} value, if delete was successful - returned true, if not - false
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean deleteTag(final Long id) throws DataAccessException;

    /**
     * Checks for the presence in the tag's database with this identifier.
     *
     * @param id is {Long} value which identifies the tag ID.
     * @return {boolean} value, if there is an tag with this identifier - returned true, if not - false
     */
    boolean checkTagById(final Long id);

    /**
     * Checks for the presence in the tag's database with this title.
     *
     * @param title is {String} value which identifies the tag title.
     * @return {boolean} value, if there is an tag with this identifier - returned true, if not - false
     */
    boolean checkTagByTitle(final String title);
}
