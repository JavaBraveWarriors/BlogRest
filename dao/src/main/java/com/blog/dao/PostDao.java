package com.blog.dao;

import com.blog.model.Post;
import com.blog.model.ResponsePostDto;

import java.util.List;

/**
 * This interface defines various operations for easy management for the Post object.
 * Use this interface if you want to access the Post.
 *
 * @author Aliaksandr Yeutushenka
 * @see Post
 */
public interface PostDao {

    /**
     * Gets the list of objects of the all posts that belong to the author.
     *
     * @param authorId the author id
     * @return {List<Post>} is a list of all posts that belong to the author.
     */
    List<ResponsePostDto> getAllPostsByAuthorId(Long authorId);

    /**
     * Gets a list of post objects from a specific item, a specific amount.
     *
     * @param initial  is {Long} value ID of the post from which you want to get objects.
     * @param quantity is {Long} value the number of required objects.
     * @return {List<Post>} is a list of posts.
     */
    List<ResponsePostDto> getPostsByInitialIdAndQuantity(Long initial, Long quantity);

    /**
     * Gets a list of post objects from a specific item, a specific amount and sort.
     *
     * @param initial  is {Long} value ID of the post from which you want to get objects.
     * @param quantity is {Long} value the number of required objects.
     * @param sort     is {String} value the sort field.
     * @return {List<Post>} is a list of posts.
     */
    List<ResponsePostDto> getPostsByInitialIdAndQuantity(Long initial, Long quantity, String sort);

    /**
     * Gets a list of post objects where is this tag.
     *
     * @param tagId is {Long} value tag ID
     * @return {List<Post>} is a list of posts.
     */
    List<ResponsePostDto> getAllPostsByTagId(Long tagId);

    /**
     * Gets a {Post} object where id is equal to argument parameter.
     *
     * @param id {Long} value the ID of the post you want to get
     * @return {Post} is a object which has this ID.
     */
    ResponsePostDto getPostById(Long id);

    /**
     * Adds new post .
     *
     * @param post {Post} to be added.
     * @return {Long} is the value that is the id of the new post.
     */
    Long addPost(final Post post);

    /**
     * Adds tag to post.
     *
     * @param id    is {Long} the value of post ID.
     * @param tagId id is {Long} the value of tag ID.
     * @return {boolean} value, if add was successful - returned true, if not - false.
     */
    boolean addTagToPost(Long id, Long tagId);

    /**
     * Updates post.
     *
     * @param post {Post} to be updated.
     * @return {boolean} value, if update was successful - returned true, if not - false.
     */
    boolean updatePost(final Post post);

    /**
     * Deletes post using post ID.
     *
     * @param id is {Long} value which identifies the post ID.
     * @return {boolean} value, if delete was successful - returned true, if not - false
     */
    boolean deletePost(Long id);

    /**
     * Deletes tag from post using post ID and tag ID.
     *
     * @param id    is {Long} the value of post ID.
     * @param tagId id is {Long} the value of tag ID.
     * @return {boolean} value, if delete was successful - returned true, if not - false.
     */
    boolean deleteTagInPost(Long id, Long tagId);

    /**
     * Checks for the presence in the post's with this identifier.
     *
     * @param id is {Long} value which identifies the post ID.
     * @return {boolean} value, if there is an post with this identifier - returned true, if not - false
     */
    boolean checkPostById(Long id);

    /**
     * Checks if there is a tag for this post.
     *
     * @param id    is {Long} value which identifies the post ID.
     * @param tagId is {Long} value which identifies the tag ID.
     * @return {boolean} value if the post has a tag - returns true, if not - false
     */
    boolean checkTagInPostById(Long id, Long tagId);

    /**
     * Checks the presence of posts by the author.
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {boolean} value, if the post has a given tag - returns true, if not - false
     */
    boolean checkPostByAuthorId(Long authorId);

    /**
     * Gets count of posts.
     *
     * @return the count of posts
     */
    Long getCountOfPosts();

    /**
     * Add comment in this post.
     *
     * @param postId is {Long} value which identifies the post ID.
     * @return {boolean} value, if comment was added - returns true, if not - false
     */
    boolean addComment(Long postId);

    /**
     * Delete comment in this post.
     *
     * @param postId is {Long} value which identifies the post ID.
     * @return {boolean} value, if comment was deleted - returns true, if not - false
     */
    boolean deleteComment(Long postId);

    /**
     * Add view to post.
     *
     * @param postId is {Long} value which identifies the post ID.
     * @return {boolean} value, if view was added - returns true, if not - false
     */
    boolean addViewToPost(Long postId);

    /**
     * Delete all tags in post by post ID.
     *
     * @param postId is {Long} value which identifies the post ID.
     * @return {boolean} value, if tags was deleted - returns true, if not - false
     */
    boolean deleteAllTags(Long postId);

    /**
     * Add tags boolean.
     *
     * @param postId is {Long} value which identifies the post ID.
     * @param tags   is {List<Long>} value determines which tags to add to this post.
     * @return {boolean} value, if tags were added - returns true, if not - false
     */
    boolean addTags(Long postId, List<Long> tags);
}
