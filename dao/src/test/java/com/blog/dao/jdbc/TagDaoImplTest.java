package com.blog.dao.jdbc;

import com.blog.Tag;
import com.blog.dao.TagDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
public class TagDaoImplTest {

    private static Long CORRECT_TAG_ID = 1L;
    private static Long INCORRECT_TAG_ID = 6L;
    private static Long CORRECT_POST_ID = 1L;


    private static Long NEW_TAG_ID_5 = 5L;
    private static String NEW_TAG_TITLE_5 = "newTitle4";
    private static String NEW_TAG_PATH_IMAGE_5 = "newPathImage4";

    private static String UPDATED_TAG_TITLE_1 = "updatedTestTitle1";

    private static Tag tag = new Tag(
            null,
            NEW_TAG_TITLE_5,
            NEW_TAG_PATH_IMAGE_5
    );


    @Autowired
    private TagDao tagDao;

    @Test
    public void getAllTags() {
        List<Tag> tags = tagDao.getAllTags();

        assertNotNull(tags);
        assertEquals(4, tags.size());
    }

    @Test
    public void getAllTagsByPostId() {
        List<Tag> tags = tagDao.getAllTagsByPostId(CORRECT_POST_ID);
        assertNotNull(tags);
        assertEquals(2, tags.size());
    }

    @Test
    public void getTagById() {
        Tag tag = tagDao.getTagById(1L);
        assertEquals("life", tag.getTitle());
    }

    @Test(expected = DataAccessException.class)
    public void getTagByIdWithException() {
        assertEquals("", tagDao.getTagById(5L).getTitle());
    }

    @Test
    public void addTag() {
        List<Tag> tags = tagDao.getAllTags();
        assertNotNull(tags);
        int initialSize = tags.size();

        Long newTagId = tagDao.addTag(tag);
        assertNotNull(newTagId);
        Tag newTag = tagDao.getTagById(newTagId);
        assertEquals(NEW_TAG_ID_5, newTag.getId());
        assertEquals(NEW_TAG_TITLE_5, newTag.getTitle());
        assertEquals(NEW_TAG_PATH_IMAGE_5, newTag.getPathImage());


        tags = tagDao.getAllTags();
        assertNotNull(tags);
        assertEquals(initialSize + 1, tags.size());
    }

    @Test
    public void updateTag() {
        Tag tag = tagDao.getTagById(CORRECT_TAG_ID);
        assertNotNull(tag);
        tag.setTitle(UPDATED_TAG_TITLE_1);

        assertEquals(1, tagDao.updateTag(tag));

        Tag updatedPost = tagDao.getTagById(CORRECT_TAG_ID);
        assertNotNull(updatedPost);
        assertEquals(tag, updatedPost);
    }

    @Test
    public void deleteTag() {
        List<Tag> tags = tagDao.getAllTags();
        assertNotNull(tags);
        int initialSize = tags.size();

        Tag newAuthor = tagDao.getTagById(CORRECT_TAG_ID);
        assertNotNull(newAuthor);

        assertEquals(1, tagDao.deleteTag(CORRECT_TAG_ID));
        assertEquals(0, tagDao.deleteTag(INCORRECT_TAG_ID));

        tags = tagDao.getAllTags();
        assertNotNull(tags);
        assertEquals(initialSize - 1, tags.size());

    }


    @Test
    public void checkTagByIdReturnedTrue() {
        assert (tagDao.checkTagById(1L));
    }

    @Test
    public void checkTagByIdReturnedFalse() {
        assertFalse(tagDao.checkTagById(5L));
    }

    @Test
    public void checkTagByTitleReturnedTrue() {
        assert (tagDao.checkTagByTitle("cats"));

    }

    @Test
    public void checkTagByTitleReturnedFalse() {
        assertFalse(tagDao.checkTagByTitle("not"));

    }

}