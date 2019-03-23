package com.blog.dao.jdbc;

import com.blog.dao.TagDao;
import com.blog.dto.TagDto;
import com.blog.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-spring-dao.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TagDaoImplTest {

    private static Long CORRECT_TAG_ID = 1L;
    private static Long CORRECT_TAG_UPDATED_ID = 2L;
    private static Long INCORRECT_TAG_ID = 7L;
    private static Long CORRECT_POST_ID = 1L;
    private static int COUNT_TAGS_IN_DB = 5;

    private static Long NEW_TAG_ID = 6L;
    private static String NEW_TAG_TITLE = "newTitle4";
    private static String NEW_TAG_PATH_IMAGE = "newPathImage4";

    private static String UPDATED_TAG_TITLE = "updatedTestTitle1";

    private static Tag tag = new Tag(
            null,
            NEW_TAG_TITLE,
            NEW_TAG_PATH_IMAGE
    );

    @Autowired
    private TagDao tagDao;

    @Test
    public void getAllTagsSuccess() {
        List<Tag> tags = tagDao.getAllTags();

        assertNotNull(tags);
        assertEquals(COUNT_TAGS_IN_DB, tags.size());
    }

    @Test
    public void getAllTagsByCorrectPostId() {
        List<TagDto> tags = tagDao.getAllTagsByPostsId(Collections.singleton(CORRECT_POST_ID));
        assertNotNull(tags);
        assertEquals(2, tags.size());
    }

    @Test
    public void getTagByCorrectId() {
        Tag tag = tagDao.getTagById(1L);
        assertEquals("life", tag.getTitle());
    }

    @Test(expected = DataAccessException.class)
    public void getTagByIncorrectId() {
        assertNull(tagDao.getTagById(6L));
    }

    @Test
    public void addTag() {
        List<Tag> tags = tagDao.getAllTags();
        assertNotNull(tags);
        int initialSize = tags.size();

        Long newTagId = tagDao.addTag(tag);
        assertNotNull(newTagId);
        Tag newTag = tagDao.getTagById(newTagId);
        assertEquals(NEW_TAG_ID, newTag.getId());
        assertEquals(NEW_TAG_TITLE, newTag.getTitle());
        assertEquals(NEW_TAG_PATH_IMAGE, newTag.getPathImage());

        tags = tagDao.getAllTags();
        assertNotNull(tags);
        assertEquals(initialSize + 1, tags.size());
    }

    @Test
    public void updateTag() {
        Tag tag = tagDao.getTagById(CORRECT_TAG_UPDATED_ID);
        assertNotNull(tag);
        tag.setTitle(UPDATED_TAG_TITLE);

        assertTrue(tagDao.updateTag(tag));

        Tag updatedPost = tagDao.getTagById(CORRECT_TAG_UPDATED_ID);
        assertNotNull(updatedPost);
        assertEquals(tag.getTitle(), updatedPost.getTitle());
    }

    @Test
    public void deleteTag() {
        List<Tag> tags = tagDao.getAllTags();
        assertNotNull(tags);
        int initialSize = tags.size();

        Tag newAuthor = tagDao.getTagById(CORRECT_TAG_ID);
        assertNotNull(newAuthor);

        assertTrue(tagDao.deleteTag(CORRECT_TAG_ID));
        assertFalse(tagDao.deleteTag(INCORRECT_TAG_ID));

        tags = tagDao.getAllTags();
        assertNotNull(tags);
        assertEquals(initialSize - 1, tags.size());

    }

    @Test
    public void checkTagByCorrectId() {
        assertTrue(tagDao.checkTagById(2L));
    }

    @Test
    public void checkTagByIncorrectId() {
        assertFalse(tagDao.checkTagById(6L));
    }

    @Test
    public void checkTagByCorrectTitle() {
        assertTrue(tagDao.checkTagByTitle("cats"));

    }

    @Test
    public void checkTagByIncorrectTitle() {
        assertFalse(tagDao.checkTagByTitle("not"));

    }

}