package com.blog.service.impls;

import com.blog.Tag;
import com.blog.dao.TagDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {

    @Mock
    private Validator validator;

    @Mock
    private TagDao tagDao;

    @InjectMocks
    private TagServiceImpl tagService;

    private static List<Tag> testTags = new ArrayList<>();
    private static Tag testTag = new Tag(4L, "4", "4");


    @BeforeClass
    public static void setUp() {
        testTags.add(new Tag(1L, "1", "1"));
        testTags.add(new Tag(2L, "2", "2"));
        testTags.add(new Tag(3L, "3", "3"));
    }

    @Test
    public void getAllTagsSuccess() {
        when(tagDao.getAllTags()).thenReturn(testTags);
        List<Tag> tags = tagService.getAllTags();
        verify(tagDao, times(1)).getAllTags();
        assertNotNull(tags);
        assertEquals(testTags.size(), tags.size());
    }

    @Test
    public void getTagByIdSuccess() {
        when(tagDao.getTagById(anyLong())).thenReturn(testTag);
        Tag tag = tagService.getTagById(anyLong());
        verify(tagDao, times(1)).getTagById(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
        assertNotNull(tag);
        assertEquals(tag.getTitle(), testTag.getTitle());
    }

    @Test(expected = ValidationException.class)
    public void getTagByIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        tagService.getTagById(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getTagByNotExistId() {
        doThrow(NotFoundException.class).when(validator).validateTagId(anyLong());
        tagService.getTagById(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test
    public void getAllTagsByPostIdSuccess() {
        when(tagDao.getAllTagsByPostId(anyLong())).thenReturn(testTags);
        List<Tag> tags = tagService.getAllTagsByPostId(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
        verify(tagDao, times(1)).getAllTagsByPostId(anyLong());
        assertNotNull(tags);
        assertEquals(tags.size(), testTags.size());
    }

    @Test(expected = ValidationException.class)
    public void getAllTagsByIncorrectPostId() {
        doThrow(ValidationException.class).when(validator).validatePostId(anyLong());
        tagService.getAllTagsByPostId(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
        verify(tagDao, times(1)).getAllTagsByPostId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getAllTagsByNotExistPostId() {
        doThrow(NotFoundException.class).when(validator).validatePostId(anyLong());
        tagService.getAllTagsByPostId(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
        verify(tagDao, times(1)).getAllTagsByPostId(anyLong());
    }

    @Test
    public void addTagSuccess() {
        when(tagDao.addTag(any(Tag.class))).thenReturn(1L);
        assertEquals((Long) 1L, tagService.addTag(testTag));
        verify(tagDao, times(1)).addTag(any(Tag.class));
        verify(validator, times(1)).checkTagWithTitle(any(Tag.class));
    }

    @Test(expected = ValidationException.class)
    public void addExistenceTag() {
        doThrow(ValidationException.class).when(validator).checkTagWithTitle(any(Tag.class));
        tagService.addTag(testTag);
        verify(validator, times(1)).checkTagWithTitle(any(Tag.class));
    }

    @Test
    public void updateTagSuccess() {
        when(tagDao.updateTag(any(Tag.class))).thenReturn(true);
        tagService.updateTag(testTag);
        verify(tagDao, times(1)).updateTag(any(Tag.class));
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void updateTagWithIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        tagService.updateTag(testTag);
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void updateTagWithNotUpdatedInDao() {
        when(tagDao.updateTag(any(Tag.class))).thenReturn(false);
        tagService.updateTag(testTag);
        verify(tagDao, times(1)).updateTag(any(Tag.class));
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void updateNotExistenceTag() {
        doThrow(NotFoundException.class).when(validator).validateTagId(anyLong());
        tagService.updateTag(testTag);
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test
    public void deleteTagSuccess() {
        when(tagDao.deleteTag(anyLong())).thenReturn(true);
        tagService.deleteTag(anyLong());
        verify(tagDao, times(1)).deleteTag(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void deleteTagByIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        tagService.deleteTag(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void deleteTagWithNotDeletedInDao() {
        when(tagDao.deleteTag(anyLong())).thenReturn(false);
        tagService.deleteTag(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotExistenceTag() {
        doThrow(NotFoundException.class).when(validator).validateTagId(anyLong());
        tagService.deleteTag(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }
}