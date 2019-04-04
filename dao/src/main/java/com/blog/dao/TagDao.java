package com.blog.dao;

import com.blog.dto.TagDto;
import com.blog.model.Tag;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Set;

/**
 * This interface defines various operations for easy management for the Post object.
 * Use this interface if you want to access the Post.
 *
 * @author Aliaksandr Yeutushenka
 * @see Tag
 */
public interface TagDao {

    /**
     * Gets the list of objects of the all tags.
     *
     * @return {List<Tag>} is a list of all tags.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    List<Tag> getAllTags();

    /**
     * Gets a {Tag} object where id is equal to argument parameter.
     *
     * @param id {Long} value the ID of the tag you want to get.
     * @return {Tag} is a object which has this ID.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Tag getTagById(final Long id);

    /**
     * Gets a list of tags objects where is this post.
     *
     * @param postsId is {Set<Long>} value posts ID which need to get tags.
     * @return {List<TagDto>} is a list of tags.
     */
    List<TagDto> getAllTagsByPostsId(final Set<Long> postsId);

    /**
     * Adds new tag.
     *
     * @param tag {Tag} to be added.
     * @return {Long} is the value that is the id of the new tag.
     */
    Long addTag(final Tag tag);

    /**
     * Updates tag.
     *
     * @param tag {Tag} to be updated.
     * @return {boolean} value, if update was successful - returned true, if not - false.
     */
    boolean updateTag(final Tag tag);

    /**
     * Deletes tag using tag ID.
     *
     * @param id is {Long} value which identifies the tag ID.
     * @return {boolean} value, if delete was successful - returned true, if not - false
     */
    boolean deleteTag(final Long id);

    /**
     * Checks for the presence in the tag's with this identifier.
     *
     * @param id is {Long} value which identifies the tag ID.
     * @return {boolean} value, if there is an tag with this identifier - returned true, if not - false
     */
    boolean checkTagById(final Long id);

    /**
     * Checks for the presence in the tag's with this title.
     *
     * @param title is {String} value which identifies the tag title.
     * @return {boolean} value, if there is an tag with this identifier - returned true, if not - false
     */
    boolean checkTagByTitle(final String title);
}