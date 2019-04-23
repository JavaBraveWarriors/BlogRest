package com.blog.service.impls;

import com.blog.dao.CommentDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.service.CommentService;
import com.blog.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * The Comment service.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER = LogManager.getLogger();

    private Validator validator;

    private CommentDao commentDao;

    @Value("${commentService.errorOfUpdating}")
    private String updateError;

    @Value("${commentService.errorOfDeleting}")
    private String deleteError;

    @Autowired
    public CommentServiceImpl(Validator validator, CommentDao commentDao) {
        this.validator = validator;
        this.commentDao = commentDao;
    }

    @Override
    public Comment getCommentById(Long commentId) throws ValidationException, NotFoundException {
        LOGGER.debug("Gets comment by id = [{}].", commentId);
        validator.validateCommentId(commentId);
        return commentDao.getCommentById(commentId);
    }

    public CommentListWrapper getListCommentsByPostIdWithPagination(Long page, Long size, Long postId)
            throws ValidationException, NotFoundException {
        LOGGER.debug("Gets list of comments by page id = [{}], size = [{}] and postId = [{}].", page, size, postId);
        validator.validatePageAndSize(page, size);
        validator.validatePostId(postId);
        Long startItem = (page - 1) * size + 1;
        CommentListWrapper commentListWrapper = new CommentListWrapper();
        commentListWrapper.setCommentsPage(commentDao.getListCommentsByInitialAndSize(startItem, size, postId));
        Long countOfComments = commentDao.getCountOfCommentsByPostId(postId);
        commentListWrapper.setCountCommentsInPost(countOfComments);
        Long countOfPages = PageCounter.getCountPages(size, countOfComments);
        commentListWrapper.setCountPages(countOfPages);
        return commentListWrapper;
    }

    public Long addComment(Comment comment) throws ValidationException, NotFoundException {
        LOGGER.debug("Adds new comment = [{}].", comment);
        validator.validatePostId(comment.getPostId());
        validator.validateAuthorId(comment.getAuthorId());
        validator.validateCommentText(comment.getText());
        comment.setTimeOfCreation(LocalDateTime.now());
        return commentDao.addComment(comment);
    }

    public void updateComment(Comment comment) throws ValidationException, NotFoundException, InternalServerException {
        LOGGER.debug("Updates post = [{}].", comment);
        validator.validateUpdatedComment(comment);
        if (!commentDao.updateComment(comment)) {
            throw new InternalServerException(updateError);
        }
    }

    public void deleteComment(Long commentId) throws ValidationException, NotFoundException, InternalServerException {
        LOGGER.debug("Deletes post = [{}].", commentId);
        validator.validateCommentId(commentId);
        if (!commentDao.deleteComment(commentId)) {
            throw new InternalServerException(updateError);
        }
    }

    public Long getCountOfPagesWithPagination(Long postId, Long size) throws ValidationException, NotFoundException {
        LOGGER.debug("Gets counts of pages comments in post id = [{}].", postId);
        validator.validatePostId(postId);
        validator.validateSizeOfPages(size);
        Long countOfPosts = commentDao.getCountOfCommentsByPostId(postId);
        Long countOfPages = countOfPosts / size;
        if (countOfPosts % size != 0) {
            countOfPages++;
        }
        return countOfPages;
    }
}
