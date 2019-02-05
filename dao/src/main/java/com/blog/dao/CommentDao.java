package com.blog.dao;

import com.blog.Comment;
import com.blog.dao.jdbc.CommentDaoImpl;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * This interface defines various operations for easy database management for the Comment object.
 * Use this interface if you want to access the Comment database.
 *
 * @author Aliaksandr Yeutushenka
 * @see Comment
 * @see CommentDaoImpl
 */
public interface CommentDao {

    /**
     * Gets a {Comment} object where id is equal to argument parameter
     *
     * @param commentId {Long} value the ID of the comment you want to get
     * @return {Comment} is a object which has this ID.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Comment getCommentById(Long commentId) throws DataAccessException;

    /**
     * Gets a list of comments objects from a specific item, a specific amount from the database.
     *
     * @param initial is {Long} value ID of the post from which you want to get objects.
     * @param size    is {Long} value the number of required objects.
     * @param postId  is {Long} value the ID of the post comments you want to get
     * @return {List<Comment>} is a list of comments from the database.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    List<Comment> getListCommentsByInitialAndSize(Long initial, Long size, Long postId) throws DataAccessException;

    /**
     * Adds new comment in database.
     *
     * @param comment {Comment} to be added to the database.
     * @return {Long} is the value that is the id of the new comment.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Long addComment(Comment comment) throws DataAccessException;

    /**
     * Gets count of comments by post id.
     *
     * @param postId is {Long} value the ID of the post comments you want to check
     * @return {Long} value is count of comments by post id
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Long getCountOfCommentsByPostId(Long postId) throws DataAccessException;

    /**
     * Update comment boolean.
     *
     * @param comment {Comment} to be updated to the database.
     * @return {boolean} value, if update was successful - returned true, if not - false.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean updateComment(Comment comment) throws DataAccessException;

    /**
     * Delete comment boolean.
     *
     * @param commentId {Long} value the ID of the comment you want to delete
     * @return {boolean} value, if delete was successful - returned true, if not - false
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean deleteComment(Long commentId) throws DataAccessException;

    /**
     * Check comment in post by id boolean.
     *
     * @param commentId {Long} value the ID of the comment you want to check
     * @param postId    {Long} value the ID of the post where you want to check
     * @return {boolean} value, if comment is exist in this postId- returned true, if not - false
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean checkCommentInPostById(Long commentId, Long postId) throws DataAccessException;

    /**
     * Check comment by id boolean.
     *
     * @param commentId {Long} value the ID of the comment you want to check
     * @return {boolean} value, if comment is exist - returned true, if not - false
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    boolean checkCommentById(Long commentId) throws DataAccessException;

    /**
     * Gets author id by comment id.
     *
     * @param commentId {Long} value the ID of the comment you want to check.
     * @return {Long} value is author id by comment id.
     * @throws DataAccessException Will throw an error if the data is not access.
     */
    Long getAuthorIdByCommentId(Long commentId) throws DataAccessException;

}
