package com.blog.dao;

import com.blog.model.Comment;

import java.util.List;

/**
 * This interface defines various operations for easy management for the Comment object.
 * Use this interface if you want to get access to Comment.
 *
 * @author Aliaksandr Yeutushenka
 * @see Comment
 */
public interface CommentDao {

    /**
     * Gets a {Comment} object where id is equal to argument parameter.
     *
     * @param commentId {Long} value the ID of the comment you want to get.
     * @return {Comment} is a object which has this ID.
     */
    Comment getCommentById(Long commentId);

    /**
     * Gets a list of comments objects from a specific item, a specific amount.
     *
     * @param initial is {Long} value ID of the post from which you want to get objects.
     * @param size    is {Long} value the number of required objects.
     * @param postId  is {Long} value the ID of the post comments you want to get
     * @return {List<Comment>} is a list of comments.
     */
    List<Comment> getListCommentsByInitialAndSize(Long initial, Long size, Long postId);

    /**
     * Adds new comment.
     *
     * @param comment {Comment} to be added.
     * @return {Long} is the value that is the id of the new comment.
     */
    Long addComment(Comment comment);

    /**
     * Gets count of comments by post id.
     *
     * @param postId is {Long} value the ID of the post comments you want to check
     * @return {Long} value is count of comments by post id
     */
    Long getCountOfCommentsByPostId(Long postId);

    /**
     * Update comment boolean.
     *
     * @param comment {Comment} to be updated.
     * @return {boolean} value, if update was successful - returned true, if not - false.
     */
    boolean updateComment(Comment comment);

    /**
     * Delete comment boolean.
     *
     * @param commentId {Long} value the ID of the comment you want to delete
     * @return {boolean} value, if delete was successful - returned true, if not - false
     */
    boolean deleteComment(Long commentId);

    /**
     * Check comment in post by id boolean.
     *
     * @param commentId {Long} value the ID of the comment you want to check
     * @param postId    {Long} value the ID of the post where you want to check
     * @return {boolean} value, if comment is exist in this postId - returned true, if not - false
     */
    boolean checkCommentInPostById(Long commentId, Long postId);

    /**
     * Check comment by id boolean.
     *
     * @param commentId {Long} value the ID of the comment you want to check
     * @return {boolean} value, if comment is exist - returned true, if not - false
     */
    boolean checkCommentById(Long commentId);

    /**
     * Gets author id by comment id.
     *
     * @param commentId {Long} value the ID of the comment you want to check.
     * @return {Long} value is author id by comment id.
     */
    Long getAuthorIdByCommentId(Long commentId);

}
