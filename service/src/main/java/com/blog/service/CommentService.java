package com.blog.service;

import com.blog.Comment;
import com.blog.CommentListWrapper;
import com.blog.dao.jdbc.CommentDaoImpl;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;

import java.util.List;

/**
 * This interface defines various ways to manage object comment with the correct business model.
 *
 * @author Aliaksandr Yeutushenka
 * @see CommentDaoImpl
 * @see Comment
 */
public interface CommentService {

    /**
     * Gets list comments by post id with pagination.
     *
     * @param page   is {Long} value ID of the page from which you want to get objects.
     * @param size   is {Long} value the number of required objects.
     * @param postId is {Long} value the post where comments are taken.
     * @return the list comments by post id with pagination.
     * @throws ValidationException Will throw an error if initial or quantity is not valid.
     * @throws NotFoundException   Will throw an error if not found post with this postId in database.
     */
    CommentListWrapper getListCommentsByPostIdWithPagination(Long page, Long size, Long postId)
            throws ValidationException, NotFoundException;

    /**
     * Add new comment.
     *
     * @param comment {Comment} to be added.
     * @return {Long} is the value that is the id of the new comment.
     * @throws ValidationException Will throw an error if postId or authorId is not valid.
     * @throws NotFoundException   Will throw an error if not found post or author with this postId or authorId in database.
     */
    Long addComment(Comment comment) throws ValidationException, NotFoundException;

    /**
     * Update comment.
     *
     * @param comment {Comment} to be updated.
     * @throws ValidationException     Will throw an error if postId or authorId is not valid.
     * @throws NotFoundException       Will throw an error if not found comment with this id, postId or authorId in database.
     * @throws InternalServerException Will throw an error if comment in post is not updated.
     */
    void updateComment(Comment comment) throws ValidationException, NotFoundException, InternalServerException;

    /**
     * Delete comment.
     *
     * @param commentId is {Long} value which identifies the comment ID.
     * @throws ValidationException     Will throw an error if commentId is not valid.
     * @throws NotFoundException       Will throw an error if not found comment with this id in database.
     * @throws InternalServerException Will throw an error if comment is not deleted.
     */
    void deleteComment(Long commentId) throws ValidationException, NotFoundException, InternalServerException;

    /**
     * Gets count of pages with pagination.
     *
     * @param postId is {Long} the value of post ID.
     * @param size   is {Long} value the size of one page.
     * @return the count of pages with pagination
     * @throws ValidationException Will throw an error if postId or size is not valid.
     * @throws NotFoundException   Will throw an error if not found post with this postId in database.
     */
    Long getCountOfPagesWithPagination(Long postId, Long size) throws ValidationException, NotFoundException;

}
