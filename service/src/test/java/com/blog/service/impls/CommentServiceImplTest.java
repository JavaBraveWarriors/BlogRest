package com.blog.service.impls;

import com.blog.dao.CommentDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.ValidationException;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.validator.Validator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {

    public static final long CORRECT_COMMENT_ID = 1L;
    public static final long CORRECT_PAGE = 2L;
    public static final long SIZE_PAGES = 10L;
    public static final long CORRECT_POST_ID = 1L;
    public static final long INCORRECT_PAGE = -2L;
    @Mock
    private CommentDao commentDao;

    @Mock
    private Validator validator;

    @InjectMocks
    private CommentServiceImpl commentService;

    private static List<Comment> COMMENTS = new ArrayList<>();

    private static Comment COMMENT = new Comment();

    @BeforeClass
    public static void setUp() {
        COMMENTS.add(new Comment());
        COMMENTS.add(new Comment());
        COMMENTS.add(COMMENT);
        COMMENT.setId(CORRECT_COMMENT_ID);
        COMMENT.setText("some");
        COMMENT.setPostId(2L);
        COMMENT.setAuthorId(2L);
    }

    @Test
    public void getListCommentsByPostIdWithPaginationSuccess() {
        when(commentDao.getListCommentsByInitialAndSize(anyLong(), anyLong(), anyLong())).thenReturn(COMMENTS);
        when(commentDao.getCountOfCommentsByPostId(anyLong())).thenReturn(5L);

        CommentListWrapper comments = commentService.getListCommentsByPostIdWithPagination(CORRECT_PAGE, SIZE_PAGES, CORRECT_POST_ID);

        assertNotNull(comments);
        assertEquals(comments.getCommentsPage().size(), COMMENTS.size());

        verify(validator, times(1)).validatePageAndSize(anyLong(), anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
        verify(commentDao, times(1)).getListCommentsByInitialAndSize(anyLong(), anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getListCommentsByIncorrectPostIdWithPagination() {
        doThrow(ValidationException.class).when(validator).validatePostId(anyLong());

        CommentListWrapper comments = commentService.getListCommentsByPostIdWithPagination(INCORRECT_PAGE, SIZE_PAGES, CORRECT_POST_ID);
    }

    @Test
    public void getCommentByIdSuccess() {
        when(commentDao.getCommentById(anyLong())).thenReturn(COMMENT);

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

        assertEquals((Long) 2L, commentService.addComment(COMMENT));

        verify(commentDao, times(1)).addComment(any(Comment.class));
        verify(validator, times(1)).validateAuthorId(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test
    public void updateCommentSuccess() {
        when(commentDao.updateComment(any(Comment.class))).thenReturn(true);

        commentService.updateComment(COMMENT);

        verify(commentDao, times(1)).updateComment(any(Comment.class));
        verify(validator, times(1)).validateUpdatedComment(any(Comment.class));
    }

    @Test(expected = InternalServerException.class)
    public void updateCommentWithNotUpdatedInDao() {
        when(commentDao.updateComment(any(Comment.class))).thenReturn(false);

        commentService.updateComment(COMMENT);

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