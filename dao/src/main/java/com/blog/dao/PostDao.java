package com.blog.dao;

import com.blog.Post;
import com.blog.ResponsePostDto;
import com.blog.dao.jdbc.PostDaoImpl;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * This interface defines various operations for easy database management for the Post object.
 * Use this interface if you want to access the Post database.
 *
 * @author Aliaksandr Yeutushenka
 * @see Post
 * @see PostDaoImpl
 */
public interface PostDao {

    /**
     * Gets the list of objects of the all posts that belong to the author from database.
     *
     * @param authorId the author id
     * @return {List<Post>} is a list of all posts that belong to the author from the database.
     * @throws DataAccessException Will throw an error if the data is not access or the table post does not contain posts with this author ID.
     */
    List<ResponsePostDto> getAllPostsByAuthorId(Long authorId) throws DataAccessException;

    /**
     * Gets a list of post objects from a specific item, a specific amount from the database.
     *
     * @param initial  is {Long} value ID of the post from which you want to get objects.
     * @param quantity is {Long} value the number of required objects.
     * @return {List<Post>} is a list of posts from the database.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    List<ResponsePostDto> getPostsByInitialIdAndQuantity(Long initial, Long quantity) throws DataAccessException;

    /**
     * Gets a list of post objects from a specific item, a specific amount and sort from the database.
     *
     * @param initial  is {Long} value ID of the post from which you want to get objects.
     * @param quantity is {Long} value the number of required objects.
     * @param sort     is {String} value the sort field.
     * @return {List<Post>} is a list of posts from the database.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    List<ResponsePostDto> getPostsByInitialIdAndQuantity(Long initial, Long quantity, String sort) throws DataAccessException;

    /**
     * Gets a list of post objects where is this tag from the database.
     *
     * @param tagId is {Long} value tag ID
     * @return {List<Post>} is a list of posts from the database.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    List<ResponsePostDto> getAllPostsByTagId(Long tagId) throws DataAccessException;

    /**
     * Gets a {Post} object where id is equal to argument parameter
     *
     * @param id {Long} value the ID of the post you want to get
     * @return {Post} is a object which has this ID.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    ResponsePostDto getPostById(Long id) throws DataAccessException;

    /**
     * Adds new post in database.
     *
     * @param post {Post} to be added to the database.
     * @return {Long} is the value that is the id of the new post.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Long addPost(final Post post) throws DataAccessException;

    /**
     * Adds tag to post to database.
     *
     * @param id    is {Long} the value of post ID.
     * @param tagId id is {Long} the value of tag ID.
     * @return {boolean} value, if add was successful - returned true, if not - false.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean addTagToPost(Long id, Long tagId) throws DataAccessException;

    /**
     * Updates post in database.
     *
     * @param post {Post} to be updated in the database.
     * @return {boolean} value, if update was successful - returned true, if not - false.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean updatePost(final Post post) throws DataAccessException;

    /**
     * Deletes post in database using post ID.
     *
     * @param id is {Long} value which identifies the post ID.
     * @return {boolean} value, if delete was successful - returned true, if not - false
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean deletePost(Long id) throws DataAccessException;

    /**
     * Deletes tag from post in database using post ID and tag ID.
     *
     * @param id    is {Long} the value of post ID.
     * @param tagId id is {Long} the value of tag ID.
     * @return {boolean} value, if delete was successful - returned true, if not - false.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean deleteTagInPost(Long id, Long tagId) throws DataAccessException;

    /**
     * Checks for the presence in the post's database with this identifier.
     *
     * @param id is {Long} value which identifies the post ID.
     * @return {boolean} value, if there is an post with this identifier - returned true, if not - false
     */
    boolean checkPostById(Long id);

    /**
     * Checks if there is a tag in the database for this post.
     *
     * @param id    is {Long} value which identifies the post ID.
     * @param tagId is {Long} value which identifies the tag ID.
     * @return {boolean} value if the post has a tag - returns true, if not - false
     */
    boolean checkTagInPostById(Long id, Long tagId);

    /**
     * Checks the presence in the database of posts by the author
     *
     * @param authorId is {Long} value which identifies the author ID.
     * @return {boolean} value, if the post has a given tag - returns true, if not - false
     */
    boolean checkPostByAuthorId(Long authorId);

    /**
     * Gets count of posts.
     *
     * @return the count of posts
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Long getCountOfPosts() throws DataAccessException;

    /**
     * Add comment in this post to database.
     *
     * @param postId is {Long} value which identifies the post ID.
     * @return {boolean} value, if comment was added - returns true, if not - false
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean addComment(Long postId) throws DataAccessException;

    /**
     * Delete comment in this post to database.
     *
     * @param postId is {Long} value which identifies the post ID.
     * @return {boolean} value, if comment was deleted - returns true, if not - false
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean deleteComment(Long postId) throws DataAccessException;

    //boolean addViewToPost(Long postId, Long userId);

    //boolean deleteViewFormPost(Long postId, Long userId);
}
