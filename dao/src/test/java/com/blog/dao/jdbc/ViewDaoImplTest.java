package com.blog.dao.jdbc;

import com.blog.View;
import com.blog.dao.ViewDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-spring-dao.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ViewDaoImplTest {

    private static Long NOT_EXIST_USER_ID = 123L;
    private static Long CORRECT_USER_ID = 1L;
    private static Long CORRECT_POST_ID = 1L;

    private static Long LIMIT_COUNT_VIEWS_IN_DB = 1000L;
    private static Long INITIAL_VIEW_ID = 1L;

    private static int COUNT_VIEWS_OF_FIRST_USER = 2;
    private static int COUNT_VIEWS_OF_FIRST_POST = 5;

    private static Long INCORRECT_USER_ID = -112L;
    private static Long NOT_EXIST_POST_ID = 121L;

    private static Long DELETED_VIEW_ID = 4L;
    private static Long INCORRECT_VIEW_ID = 214L;
    private static Long INCORRECT_POST_ID = -12L;

    private static View correctView = new View(
            null,
            CORRECT_USER_ID,
            CORRECT_POST_ID);

    private static View incorrectView = new View(
            null,
            INCORRECT_USER_ID,
            NOT_EXIST_POST_ID);

    @Autowired
    private ViewDao viewDao;

    @Test
    public void addViewSuccess() {
        List<View> views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, CORRECT_POST_ID);
        assertNotNull(views);
        int initialSize = views.size();

        assertTrue(viewDao.addView(correctView));

        views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, CORRECT_POST_ID);
        assertNotNull(views);
        assertEquals(initialSize + 1, views.size());
    }

    @Test(expected = DataAccessException.class)
    public void addViewWithIncorrectView() {
        viewDao.addView(incorrectView);
    }

    @Test
    public void deleteViewSuccess() {
        List<View> views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, CORRECT_POST_ID);
        assertNotNull(views);
        int initialSize = views.size();

        assertTrue(viewDao.deleteView(DELETED_VIEW_ID));

        views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, CORRECT_POST_ID);
        assertNotNull(views);
        assertEquals(initialSize - 1, views.size());
    }

    @Test
    public void deleteIncorrectView() {
        List<View> views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, CORRECT_POST_ID);
        assertNotNull(views);
        int initialSize = views.size();

        assertFalse(viewDao.deleteView(INCORRECT_VIEW_ID));

        views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, CORRECT_POST_ID);
        assertNotNull(views);
        assertEquals(initialSize, views.size());
    }

    @Test
    public void getListViewsOfPostSuccess() {
        List<View> views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, CORRECT_POST_ID);

        assertNotNull(views);
        assertEquals(COUNT_VIEWS_OF_FIRST_POST, views.size());
    }

    @Test
    public void getListViewsOfNotExistPost() {
        List<View> views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, NOT_EXIST_POST_ID);

        assertNotNull(views);
        assertEquals(0, views.size());
    }

    @Test
    public void getListViewsOfIncorrectPost() {
        List<View> views = viewDao.getListViewsOfPost(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, INCORRECT_POST_ID);

        assertNotNull(views);
        assertEquals(0, views.size());
    }

    @Test
    public void getListViewsOfUserSuccess() {
        List<View> views = viewDao.getListViewsOfUser(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, CORRECT_USER_ID);

        assertNotNull(views);
        assertEquals(COUNT_VIEWS_OF_FIRST_USER, views.size());
    }

    @Test
    public void getListViewsOfNotExistUser() {
        List<View> views = viewDao.getListViewsOfUser(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, NOT_EXIST_USER_ID);

        assertNotNull(views);
        assertEquals(0, views.size());
    }

    @Test
    public void getListViewsOfIncorrectUser() {
        List<View> views = viewDao.getListViewsOfUser(INITIAL_VIEW_ID, LIMIT_COUNT_VIEWS_IN_DB, INCORRECT_USER_ID);

        assertNotNull(views);
        assertEquals(0, views.size());
    }

    @Test
    public void checkViewByPostIdAndUserCorrectId() {
        assertTrue(viewDao.checkViewByPostIdAndUserId(CORRECT_POST_ID, CORRECT_USER_ID));
    }

    @Test
    public void checkViewByPostIdAndUserIncorrectId() {
        assertFalse(viewDao.checkViewByPostIdAndUserId(CORRECT_POST_ID, INCORRECT_USER_ID));
    }
}