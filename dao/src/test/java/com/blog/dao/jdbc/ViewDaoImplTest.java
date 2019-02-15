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

    private static Long CORRECT_VIEW_ID = 1L;
    private static Long CORRECT_USER_ID = 1L;
    private static Long CORRECT_POST_ID = 1L;

    private static Long INCORRECT_USER_ID = 112L;
    private static Long INCORRECT_POST_ID = 121L;

    private static Long DELETED_POST_ID = 4L;

    private static View correctView = new View(
            null,
            CORRECT_USER_ID,
            CORRECT_POST_ID);

    private static View incorrectView = new View(
            null,
            INCORRECT_USER_ID,
            INCORRECT_POST_ID);

    @Autowired
    private ViewDao viewDao;

    @Test
    public void addViewSuccess() {
        List<View> views = viewDao.getListViewsOfPost(1L, 100L, CORRECT_POST_ID);
        assertNotNull(views);
        int initialSize = views.size();

        assertTrue(viewDao.addView(correctView));

        views = viewDao.getListViewsOfPost(1L, 100L, CORRECT_POST_ID);
        assertNotNull(views);
        assertEquals(initialSize + 1, views.size());
    }

    @Test(expected = DataAccessException.class)
    public void addViewWithIncorrectView() {
        viewDao.addView(incorrectView);
    }

    @Test
    public void deleteView() {
        List<View> views = viewDao.getListViewsOfPost(1L, 100L, CORRECT_POST_ID);
        assertNotNull(views);
        int initialSize = views.size();

        assertTrue(viewDao.deleteView(DELETED_POST_ID));

        views = viewDao.getListViewsOfPost(1L, 100L, CORRECT_POST_ID);
        assertNotNull(views);
        assertEquals(initialSize - 1, views.size());
    }

    @Test
    public void getListViewsOfPost() {
    }

    @Test
    public void getListViewsOfUser() {
    }

    @Test
    public void checkViewByPostIdAndUserId() {
    }
}