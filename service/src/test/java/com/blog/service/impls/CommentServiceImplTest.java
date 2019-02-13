package com.blog.service.impls;

import com.blog.Comment;
import com.blog.CommentListWrapper;
import com.blog.dao.CommentDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.ValidationException;
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

    @Mock
    private CommentDao commentDao;

    @Mock
    private Validator validator;

    @InjectMocks
    private CommentServiceImpl commentService;

    private static List<Comment> comments = new ArrayList<>();

    private static Comment comment = new Comment();

    @BeforeClass
    public static void setUp() {
        comments.add(new Comment());
        comments.add(new Comment());
        comments.add(comment);
        comment.setId(1L);
        comment.setText("some");
        comment.setPostId(2L);
        comment.setAuthorId(2L);
    }

    @Test
    public void getListCommentsByPostIdWithPaginationSuccess() {
        when(commentDao.getListCommentsByInitialAndSize(anyLong(), anyLong(), anyLong())).thenReturn(comments);
        when(commentDao.getCountOfCommentsByPostId(anyLong())).thenReturn(5L);

        CommentListWrapper comments = commentService.getListCommentsByPostIdWithPagination(2L, 10L, 1L);

        assertNotNull(comments);
        assertEquals(comments.getCommentsPage().size(), comments.getCommentsPage().size());

        verify(validator, times(1)).validatePageAndSize(anyLong(), anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
        verify(commentDao, times(1)).getListCommentsByInitialAndSize(anyLong(), anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getListCommentsByIncorrectPostIdWithPagination() {
        doThrow(ValidationException.class).when(validator).validatePostId(anyLong());

        CommentListWrapper comments = commentService.getListCommentsByPostIdWithPagination(-2L, 10L, 1L);
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
}