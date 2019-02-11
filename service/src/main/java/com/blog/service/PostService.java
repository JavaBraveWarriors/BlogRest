package com.blog.service;

import com.blog.Comment;
import com.blog.Post;
import com.blog.PostForAdd;
import com.blog.PostForGet;
import com.blog.dao.jdbc.PostDaoImpl;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;

import java.util.List;

/**
 * This interface defines various ways to manage object post with the correct business model.
 *
 * @author Aliaksandr Yeutushenka
 * @see PostDaoImpl
 * @see Post
 */
public interface PostService {

    /**
     * Gets all posts by author id.
     *
     * @param authorId the user id
     * @return {List<Post>} is a list of all posts that belong to the author from the database.
     * @throws ValidationException Will throw an error if authorId is not valid.
     * @throws NotFoundException   Will throw an error if not found author with this authorId in database.
     */
    List<PostForGet> getAllPostsByAuthorId(Long authorId) throws ValidationException, NotFoundException;

    /**
     * Gets a list of post objects from a specific item, a specific amount.
     *
     * @param page is {Long} value ID of the page from which you want to get objects.
     * @param size is {Long} value the number of required objects.
     * @return {List<Post>} is a list of posts.
     * @throws ValidationException Will throw an error if initial or quantity is not valid.
     */
    List<PostForGet> getPostsWithPaginationAndSorting(Long page, Long size, String sort) throws ValidationException;

    /**
     * Gets a list of post objects where is this tag.
     *
     * @param tagId is {Long} value tag ID
     * @return {List<Post>} is a list of posts.
     * @throws ValidationException Will throw an error if tagId is not valid.
     * @throws NotFoundException   Will throw an error if not found tag with this Id in database.
     */
    List<PostForGet> getAllPostsByTagId(Long tagId) throws ValidationException, NotFoundException;

    /**
     * Gets a {PostForGet} object where id is equal to argument parameter.
     *
     * @param postId {Long} value the ID of the post you want to get.
     * @return {Post} is a object which has this ID.
     * @throws ValidationException Will throw an error if postId is not valid.
     * @throws NotFoundException   Will throw an error if not found post with this postId in database.
     */
    PostForGet getPostById(Long postId) throws ValidationException, NotFoundException;

    /**
     * Add new post.
     *
     * @param post {PostForAdd} to be added.
     * @return {Long} is the value that is the id of the new post.
     * @throws ValidationException Will throw an error if authorId or list of tags is not valid.
     * @throws NotFoundException   Will throw an error if not found author or one of the tag list items in database.
     */
    Long addPost(PostForAdd post) throws ValidationException, NotFoundException;

    /**
     * Add tag to post.
     *
     * @param postId is {Long} the value of post ID.
     * @param tagId  id is {Long} the value of tag ID.
     * @throws ValidationException     Will throw an error if postId or tagId is not valid.
     * @throws NotFoundException       Will throw an error if not found post or tag in database.
     * @throws InternalServerException Will throw an error if tag is not added to post.
     */
    void addTagToPost(Long postId, Long tagId) throws ValidationException, NotFoundException, InternalServerException;

    /**
     * Update post.
     *
     * @param post {PostForAdd} to be updated.
     * @throws ValidationException     Will throw an error if authorId or list of tags is not valid.
     * @throws NotFoundException       Will throw an error if not found post or one of the tag list items in database.
     * @throws InternalServerException Will throw an error if post or tags in post is not updated.
     */
    void updatePost(PostForAdd post) throws ValidationException, NotFoundException, InternalServerException;

    /**
     * Deletes post using post ID.
     *
     * @param postId is {Long} value which identifies the post ID.
     * @throws ValidationException     Will throw an error if postId is not valid.
     * @throws NotFoundException       Will throw an error if not found post with this id in database.
     * @throws InternalServerException Will throw an error if post is not deleted.
     */
    void deletePost(Long postId) throws ValidationException, NotFoundException, InternalServerException;

    /**
     * Delete tag in post using post ID and tag ID.
     *
     * @param postId is {Long} the value of post ID.
     * @param tagId  id is {Long} the value of tag ID.
     * @throws ValidationException     Will throw an error if postId or tagId is not valid.
     * @throws NotFoundException       Will throw an error if not found post or tag in database.
     * @throws InternalServerException Will throw an error if tag is not deleted in post.
     */
    void deleteTagInPost(Long postId, Long tagId) throws ValidationException, NotFoundException, InternalServerException;

    Long getCountOfPagesWithPagination(Long size) throws ValidationException, NotFoundException;

    void addCommentToPost(Comment comment);

    void deleteCommentInPost(Long postId, Long commentId);
}
