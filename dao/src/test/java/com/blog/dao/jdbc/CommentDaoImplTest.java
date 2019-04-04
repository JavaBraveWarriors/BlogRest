package com.blog.dao.jdbc;

import com.blog.dao.CommentDao;
import com.blog.model.Comment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-spring-dao.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentDaoImplTest {

    private static final Long CORRECT_PAGE_ID = 2L;
    private static final int COUNT_COMMENTS_IN_SECOND_PAGE = 2;
    private static final Long CORRECT_POST_ID = 1L;
    private static final Long CORRECT_FIRST_COMMENT_ID = 1L;
    private static final Long CORRECT_COMMENT_DELETE_ID = 5L;
    private static final Long CORRECT_COMMENT_CHECK_ID = 1L;
    private static final Long INCORRECT_COMMENT_ID = 15L;
    private static final Long CORRECT_COMMENT_ID = 2L;
    private static final Long CORRECT_POST_ID_ADDED_COMMENT = 3L;
    private static final Long COUNT_COMMENTS_IN_POST3 = 2L;
    private static final Long COMMENTS_SIZE = 2L;

    private static final Long INCORRECT_POST_ID = 9L;
    private static final Long CORRECT_USER_ID = 1L;
    private static final Long CORRECT_AUTHOR_ID = 4L;
    private static final Long INCORRECT_USER_ID = 9L;
    private static final Long ZERO = 0L;

    private static final String NEW_COMMENT_TEXT = "newText4";

    private static final String UPDATED_COMMENT_TEXT = "updatedTestText1";

    private static Comment comment;

    @Before
    public void setUp() {
        comment = new Comment(
                NEW_COMMENT_TEXT,
                CORRECT_USER_ID,
                CORRECT_POST_ID_ADDED_COMMENT);
    }

    @Autowired
    private CommentDao commentDao;

    @Test
    public void getCommentByIdSuccess() {
        Comment comment = commentDao.getCommentById(CORRECT_COMMENT_ID);

        assertNotNull(comment);
        assertEquals(comment.getId(), CORRECT_COMMENT_ID);
    }

    @Test(expected = DataAccessException.class)
    public void getCommentByIncorrectId() {
        assertNull(commentDao.getCommentById(INCORRECT_COMMENT_ID));
    }

    @Test
    public void getListCommentsByPostIdWithPaginationSuccess() {
        List<Comment> comments = commentDao.getListCommentsByInitialAndSize(CORRECT_PAGE_ID, COMMENTS_SIZE, CORRECT_POST_ID);
        assertNotNull(comments);
        assertEquals(COUNT_COMMENTS_IN_SECOND_PAGE, comments.size());
        assertNotEquals(CORRECT_FIRST_COMMENT_ID, comments.get(1));
    }

    @Test
    public void getListCommentsByIncorrectPostIdWithPagination() {
        List<Comment> comments = commentDao.getListCommentsByInitialAndSize(CORRECT_PAGE_ID, COMMENTS_SIZE, INCORRECT_POST_ID);
        assertTrue(comments.isEmpty());
    }

    @Test
    public void addCommentSuccess() {
        Long countOfComments = commentDao.getCountOfCommentsByPostId(CORRECT_POST_ID_ADDED_COMMENT);
        assertNotNull(countOfComments);
        Long initialSize = countOfComments;

        Long newCommentId = commentDao.addComment(comment);
        assertNotNull(newCommentId);

        countOfComments = commentDao.getCountOfCommentsByPostId(CORRECT_POST_ID_ADDED_COMMENT);
        assertNotNull(countOfComments);
        assertEquals(initialSize + 1L, countOfComments.longValue());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addCommentWithIncorrectAuthorId() {
        comment.setAuthorId(INCORRECT_USER_ID);
        assertNull(commentDao.addComment(comment));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addCommentWithIncorrectPostId() {
        comment.setPostId(INCORRECT_POST_ID);
        assertNull(commentDao.addComment(comment));
    }

    @Test(expected = NullPointerException.class)
    public void addNullComment() {
        assertNull(commentDao.addComment(null));
    }

    @Test
    public void updateCommentSuccess() {
        Comment comment = commentDao.getCommentById(CORRECT_COMMENT_ID);
        assertNotNull(comment);

        comment.setText(UPDATED_COMMENT_TEXT);
        assertTrue(commentDao.updateComment(comment));
        comment = commentDao.getCommentById(CORRECT_COMMENT_ID);

        assertEquals(UPDATED_COMMENT_TEXT, comment.getText());
    }

    @Test(expected = NullPointerException.class)
    public void updateNullComment() {
        commentDao.updateComment(null);
    }

    @Test
    public void deleteComment() {
        Long countOfComments = commentDao.getCountOfCommentsByPostId(CORRECT_POST_ID);
        assertNotNull(countOfComments);
        Long initialSize = countOfComments;

        assertTrue(commentDao.deleteComment(CORRECT_COMMENT_DELETE_ID));

        countOfComments = commentDao.getCountOfCommentsByPostId(CORRECT_POST_ID);
        assertNotNull(countOfComments);
        assertEquals(initialSize - 1L, countOfComments.longValue());
    }

    @Test
    public void deleteNotExistComment() {
        assertFalse(commentDao.deleteComment(INCORRECT_COMMENT_ID));
    }

    @Test
    public void checkCommentInPostByCorrectId() {
        assertTrue(commentDao.checkCommentInPostById(CORRECT_COMMENT_ID, CORRECT_POST_ID_ADDED_COMMENT));
    }

    @Test
    public void checkCommentInPostByIncorrectId() {
        assertFalse(commentDao.checkCommentInPostById(INCORRECT_COMMENT_ID, CORRECT_POST_ID));
    }

    @Test
    public void checkCommentByCorrectId() {
        assertTrue(commentDao.checkCommentById(CORRECT_COMMENT_ID));
    }

    @Test
    public void checkCommentByIncorrectId() {
        assertFalse(commentDao.checkCommentById(INCORRECT_COMMENT_ID));
    }

    @Test
    public void getCountOfCommentsByPostId() {
        assertEquals(COUNT_COMMENTS_IN_POST3, commentDao.getCountOfCommentsByPostId(3L));
    }

    @Test
    public void getCountOfCommentsWithIncorrectPostId() {
        assertEquals(ZERO, commentDao.getCountOfCommentsByPostId(INCORRECT_POST_ID));
    }

    @Test
    public void getAuthorIdByCommentIdSuccess() {
        Long authorId = commentDao.getAuthorIdByCommentId(CORRECT_COMMENT_CHECK_ID);
        assertEquals(CORRECT_AUTHOR_ID, authorId);
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorIdByIncorrectCommentId() {
        Long authorId = commentDao.getAuthorIdByCommentId(INCORRECT_COMMENT_ID);
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorIdByNullCommentId() {
        Long authorId = commentDao.getAuthorIdByCommentId(null);
    }
}