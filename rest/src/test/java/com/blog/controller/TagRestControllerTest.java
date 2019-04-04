package com.blog.controller;

import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.handler.RestErrorHandler;
import com.blog.model.Tag;
import com.blog.service.TagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class TagRestControllerTest extends AbstractControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagRestController tagRestController;

    private MockMvc mockMvc;
    private static final Long CORRECT_TAG_ID = 1L;
    private static final Long INCORRECT_TAG_ID = -1L;
    private static final Long NOT_EXIST_TAG_ID = 121L;
    private static final String TEST_TEXT = "testTitle";

    private Tag CORRECT_TAG = new Tag(CORRECT_TAG_ID, TEST_TEXT, TEST_TEXT);
    private Tag INCORRECT_TAG = new Tag(null, null, TEST_TEXT);
    private Tag NOT_EXIST_TAG = new Tag(NOT_EXIST_TAG_ID, TEST_TEXT, TEST_TEXT);

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagRestController)
                .setControllerAdvice(new RestErrorHandler())
                .alwaysDo(print())
                .build();
    }

    @Test
    public void getAllTagsSuccess() throws Exception {
        given(tagService.getAllTags()).willReturn(Collections.singletonList(CORRECT_TAG));
        mockMvc.perform(get(getEndpoint()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        jsonConverter.convertToJson(Collections.singletonList(CORRECT_TAG))));
        verify(tagService, times(1)).getAllTags();
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void getTagByIdSuccess() throws Exception {
        given(tagService.getTagById(anyLong())).willReturn(CORRECT_TAG);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), CORRECT_TAG_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonConverter.convertToJson(CORRECT_TAG)));
        verify(tagService, times(1)).getTagById(CORRECT_TAG_ID);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void getTagByIncorrectId() throws Exception {
        given(tagService.getTagById(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), INCORRECT_TAG_ID))
                .andExpect(status().isBadRequest());
        verify(tagService, times(1)).getTagById(INCORRECT_TAG_ID);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void getTagWithNotExistId() throws Exception {
        given(tagService.getTagById(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), NOT_EXIST_TAG_ID))
                .andExpect(status().isNotFound());
        verify(tagService, times(1)).getTagById(NOT_EXIST_TAG_ID);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void addTagSuccess() throws Exception {
        given(tagService.addTag(any(Tag.class))).willReturn(anyLong());
        mockMvc.perform(post(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_TAG)))
                .andExpect(status().isCreated());
        verify(tagService, times(1)).addTag(any(Tag.class));
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void addIncorrectTag() throws Exception {
        mockMvc.perform(post(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(INCORRECT_TAG)))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void updateTagSuccess() throws Exception {
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_TAG)))
                .andExpect(status().isOk());
        verify(tagService, times(1)).updateTag(any(Tag.class));
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void updateIncorrectTag() throws Exception {
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(INCORRECT_TAG)))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void updateNotExistTag() throws Exception {
        doThrow(NotFoundException.class).when(tagService).updateTag(any(Tag.class));
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(NOT_EXIST_TAG)))
                .andExpect(status().isNotFound());
        verify(tagService, times(1)).updateTag(any(Tag.class));
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void updateTagWithInternalServerException() throws Exception {
        doThrow(InternalServerException.class).when(tagService).updateTag(any(Tag.class));
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_TAG)))
                .andExpect(status().isInternalServerError());
        verify(tagService, times(1)).updateTag(any(Tag.class));
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void updateTagWithDataAccessError() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(tagService).updateTag(any(Tag.class));
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_TAG)))
                .andExpect(status().isConflict());
        verify(tagService, times(1)).updateTag(any(Tag.class));
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void deleteTagSuccess() throws Exception {
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), CORRECT_TAG_ID))
                .andExpect(status().isOk());
        verify(tagService, times(1)).deleteTag(CORRECT_TAG_ID);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void deleteTagByIncorrectId() throws Exception {
        doThrow(ValidationException.class).when(tagService).deleteTag(anyLong());
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), INCORRECT_TAG_ID))
                .andExpect(status().isBadRequest());
        verify(tagService, times(1)).deleteTag(INCORRECT_TAG_ID);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void deleteNotExistTag() throws Exception {
        doThrow(NotFoundException.class).when(tagService).deleteTag(anyLong());
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), NOT_EXIST_TAG_ID))
                .andExpect(status().isNotFound());
        verify(tagService, times(1)).deleteTag(NOT_EXIST_TAG_ID);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void deleteTagWithDataAccessException() throws Exception {
        doThrow(SQLWarningException.class).when(tagService).deleteTag(anyLong());
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), CORRECT_TAG_ID))
                .andExpect(status().isInternalServerError());
        verify(tagService, times(1)).deleteTag(CORRECT_TAG_ID);
        verifyNoMoreInteractions(tagService);
    }

    protected String getEndpoint() {
        return "/tags";
    }
}