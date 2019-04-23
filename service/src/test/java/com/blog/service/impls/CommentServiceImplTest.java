package com.blog.service.impls;

import com.blog.dao.CommentDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.ValidationException;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.service.CommentService;
import com.blog.service.impls.config.ServiceTestConfiguration;
import com.blog.validator.Validator;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * The Comment service impl test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class CommentServiceImplTest {

    private static final Long CORRECT_COMMENT_ID = 1L;
    private static final Long CORRECT_PAGE = 2L;
    private static final Long SIZE_PAGES = 10L;
    private static final Long CORRECT_POST_ID = 1L;
    private static final Long INCORRECT_PAGE = -2L;
    private static List<Comment> comments = new ArrayList<>();
    private static Comment comment = new Comment();

    @Autowired
    private CommentDao commentDao;

    @Autowired
    @Qualifier("mockValidator")
    private Validator validator;

    @Autowired
    @Qualifier("testCommentService")
    private CommentService commentService;

    @BeforeClass
    public static void setUp() {
        comments.add(new Comment());
        comments.add(new Comment());
        comments.add(comment);
        comment.setId(CORRECT_COMMENT_ID);
        comment.setText("some");
        comment.setPostId(2L);
        comment.setAuthorId(2L);
    }

    @After
    public void updateData() {
        Mockito.reset(commentDao, validator);
    }

    @Test
    public void getListCommentsByPostIdWithPaginationSuccess() {
        when(commentDao.getListCommentsByInitialAndSize(anyLong(), anyLong(), anyLong())).thenReturn(comments);
        when(commentDao.getCountOfCommentsByPostId(anyLong())).thenReturn(5L);

        CommentListWrapper comments =
                commentService.getListCommentsByPostIdWithPagination(CORRECT_PAGE, SIZE_PAGES, CORRECT_POST_ID);

        assertNotNull(comments);
        assertEquals(comments.getCommentsPage().size(), CommentServiceImplTest.comments.size());

        verify(validator, times(1)).validatePageAndSize(anyLong(), anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
        verify(commentDao, times(1))
                .getListCommentsByInitialAndSize(anyLong(), anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getListCommentsByIncorrectPostIdWithPagination() {
        doThrow(ValidationException.class).when(validator).validatePostId(anyLong());

        CommentListWrapper comments =
                commentService.getListCommentsByPostIdWithPagination(INCORRECT_PAGE, SIZE_PAGES, CORRECT_POST_ID);
    }

    @Test
    public void getCommentByIdSuccess() {
        when(commentDao.getCommentById(anyLong())).thenReturn(comment);

        commentService.getCommentById(CORRECT_COMMENT_ID);

        verify(validator, times(1)).validateCommentId(anyLong());
        verify(commentDao, times(1)).getCommentById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getCommentByIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateCommentId(anyLong());

        commentService.getCommentById(CORRECT_COMMENT_ID);

        verify(validator, times(1)).validateCommentId(anyLong());
    }

    @Test
    public void addCommentSuccess() {
        when(commentDao.addComment(any(Comment.class))).thenReturn(2L);

        assertEquals((Long) 2L, commentService.addComment(comment));

        verify(commentDao, times(1)).addComment(any(Comment.class));
        verify(validator, times(1)).validateAuthorId(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test
    public void updateCommentSuccess() {
        when(commentDao.updateComment(any(Comment.class))).thenReturn(true);

        commentService.updateComment(comment);

        verify(commentDao, times(1)).updateComment(any(Comment.class));
        verify(validator, times(1)).validateUpdatedComment(any(Comment.class));
    }

    @Test(expected = InternalServerException.class)
    public void updateCommentWithNotUpdatedInDao() {
        when(commentDao.updateComment(any(Comment.class))).thenReturn(false);

        commentService.updateComment(comment);

        verify(commentDao, times(1)).updateComment(any(Comment.class));
        verify(validator, times(1)).validateUpdatedComment(any(Comment.class));
    }

    @Test
    public void deleteComment() {
        when(commentDao.deleteComment(anyLong())).thenReturn(true);

        commentService.deleteComment(anyLong());

        verify(commentDao, times(1)).deleteComment(anyLong());
        verify(validator, times(1)).validateCommentId(anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void deleteCommentWithNotDeletedInDao() {
        when(commentDao.deleteComment(anyLong())).thenReturn(false);

        commentService.deleteComment(anyLong());

        verify(commentDao, times(1)).deleteComment(anyLong());
        verify(validator, times(1)).validateCommentId(anyLong());
    }

    @Test
    public void getCountOfPagesWithPaginationAndExistRemainderDivision() {
        when(commentDao.getCountOfCommentsByPostId(anyLong())).thenReturn(9L);
        Long countPages = commentService.getCountOfPagesWithPagination(anyLong(), 4L);
        assertEquals(3L, countPages.longValue());
    }

    @Test
    public void getCountOfPagesWithPagination() {
        when(commentDao.getCountOfCommentsByPostId(anyLong())).thenReturn(8L);
        Long countPages = commentService.getCountOfPagesWithPagination(anyLong(), 4L);
        assertEquals(2L, countPages.longValue());
    }
}