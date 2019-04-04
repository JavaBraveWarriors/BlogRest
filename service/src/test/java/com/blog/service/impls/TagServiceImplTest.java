package com.blog.service.impls;

import com.blog.dao.TagDao;
import com.blog.dto.TagDto;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.model.Tag;
import com.blog.validator.Validator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {

    private static final Long CORRECT_TAG_ID = 21L;
    private static final Long INCORRECT_TAG_ID = -12L;
    private static final Long NOT_EXIST_TAG_ID = 123L;
    private static List<TagDto> POST_TAGS = new ArrayList<>();
    private static List<Tag> TEST_TAGS = new ArrayList<>();
    private static Tag testTag = new Tag(4L, "4", "4");
    private static Long CORRECT_POST_ID = 1L;

    @Mock
    private Validator validator;

    @Mock
    private TagDao tagDao;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeClass
    public static void setUp() {
        TEST_TAGS.add(new Tag(1L, "1", "1"));
        TEST_TAGS.add(new Tag(2L, "2", "2"));
        TEST_TAGS.add(new Tag(3L, "3", "3"));
        POST_TAGS.add(new TagDto(CORRECT_POST_ID, new Tag(1L, "1", "1")));
        POST_TAGS.add(new TagDto(CORRECT_POST_ID, new Tag(2L, "2", "2")));
        POST_TAGS.add(new TagDto(CORRECT_POST_ID, new Tag(3L, "3", "3")));
    }

    @Test
    public void getAllTagsSuccess() {
        when(tagDao.getAllTags()).thenReturn(TEST_TAGS);
        List<Tag> tags = tagService.getAllTags();
        verify(tagDao, times(1)).getAllTags();
        assertNotNull(tags);
        assertEquals(TEST_TAGS.size(), tags.size());
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
        when(tagDao.getAllTagsByPostsId(any())).thenReturn(POST_TAGS);
        List<TagDto> tags = tagService.getAllTagsByPostsId(Collections.singleton(CORRECT_POST_ID));
        verify(tagDao, times(1)).getAllTagsByPostsId(any());
        assertNotNull(tags);
        assertEquals(tags.size(), POST_TAGS.size());
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
        tagService.deleteTag(CORRECT_TAG_ID);
        verify(tagDao, times(1)).deleteTag(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void deleteTagByIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        tagService.deleteTag(INCORRECT_TAG_ID);
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void deleteTagWithNotDeletedInDao() {
        when(tagDao.deleteTag(anyLong())).thenReturn(false);
        tagService.deleteTag(CORRECT_TAG_ID);
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotExistenceTag() {
        doThrow(NotFoundException.class).when(validator).validateTagId(anyLong());
        tagService.deleteTag(NOT_EXIST_TAG_ID);
        verify(validator, times(1)).validateTagId(anyLong());
    }
}