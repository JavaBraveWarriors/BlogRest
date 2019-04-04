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

    private static final long NOT_EXIST_TAG_ID = 6L;
    private static final String CORRECT_TAG_TITLE = "cats";
    private static final String INCORRECT_TAG_TITLE = "not";
    private static final Long CORRECT_TAG_ID = 1L;
    private static final Long CORRECT_TAG_UPDATED_ID = 2L;
    private static final Long INCORRECT_TAG_ID = 7L;
    private static final Long CORRECT_POST_ID = 1L;
    private static final int COUNT_TAGS_IN_FIRST_POST = 2;
    private static final int COUNT_TAGS_IN_DB = 5;

    private static final Long NEW_TAG_ID = 6L;
    private static final String NEW_TAG_TITLE = "newTitle4";
    private static final String NEW_TAG_PATH_IMAGE = "newPathImage4";

    private static final String UPDATED_TAG_TITLE = "updatedTestTitle1";

    private static final Tag tag = new Tag(
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
        assertEquals(COUNT_TAGS_IN_FIRST_POST, tags.size());
    }

    @Test
    public void getTagByCorrectId() {
        Tag tag = tagDao.getTagById(CORRECT_TAG_ID);
        assertEquals(CORRECT_TAG_ID, tag.getId());
    }

    @Test(expected = DataAccessException.class)
    public void getTagByIncorrectId() {
        assertNull(tagDao.getTagById(NOT_EXIST_TAG_ID));
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
        assertTrue(tagDao.checkTagByTitle(CORRECT_TAG_TITLE));
    }

    @Test
    public void checkTagByIncorrectTitle() {
        assertFalse(tagDao.checkTagByTitle(INCORRECT_TAG_TITLE));

    }
}