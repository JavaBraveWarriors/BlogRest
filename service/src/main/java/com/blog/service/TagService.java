package com.blog.service;

import com.blog.dao.jdbc.TagDaoImpl;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.model.Tag;

import java.util.List;

/**
 * This interface defines various ways to manage object tag with the correct business model.
 *
 * @author Aliaksandr Yeutushenka
 * @see TagDaoImpl
 * @see Tag
 */
public interface TagService {

    /**
     * Gets the list of objects of the all tags.
     *
     * @return {List<Tag>} is a list of all tags.
     */
    List<Tag> getAllTags();

    /**
     * Gets a {Tag} object where id is equal to argument parameter.
     *
     * @param tagId {Long} value the ID of the tag you want to get.
     * @return {Tag} is a object which has this ID.
     * @throws ValidationException Will throw an error if tagId is not valid.
     * @throws NotFoundException   Will throw an error if not found tag with this tagId in database.
     */
    Tag getTagById(Long tagId) throws ValidationException, NotFoundException;

    /**
     * Gets all tags by post id.
     *
     * @param postId {Long} value the ID of the post.
     * @return {List<Tag>} is the all tags by post id.
     * @throws ValidationException Will throw an error if postId is not valid.
     * @throws NotFoundException   Will throw an error if not found post with this postId in database.
     */
    List<Tag> getAllTagsByPostId(Long postId) throws ValidationException, NotFoundException;

    /**
     * Add new tag.
     *
     * @param tag {Tag} to be added.
     * @return {Long} is the value that is the id of the new tag.
     * @throws ValidationException Will throw an error if tag exist with this title.
     */
    Long addTag(Tag tag)  throws ValidationException;

    /**
     * Update tag.
     *
     * @param tag {Tag} to be updated.
     * @throws ValidationException     Will throw an error if tag id is not valid.
     * @throws NotFoundException       Will throw an error if not found tag in database.
     * @throws InternalServerException Will throw an error if tag is not updated.
     */
    void updateTag(Tag tag) throws ValidationException, NotFoundException, InternalServerException;

    /**
     * Delete tag using tag ID.
     *
     * @param tagId is {Long} value which identifies the tag ID.
     * @throws ValidationException     Will throw an error if tag id is not valid.
     * @throws NotFoundException       Will throw an error if not found tag in database.
     * @throws InternalServerException Will throw an error if tag is not deleted.
     */
    void deleteTag(Long tagId) throws ValidationException, NotFoundException, InternalServerException;
}
