package com.blog;

import com.blog.controller.TagRestController;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.handler.RestErrorHandler;
import com.blog.service.TagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
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

@RunWith(MockitoJUnitRunner.class)
public class TagRestControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagRestController tagRestController;

    private MockMvc mockMvc;
    private Tag tag = new Tag(1L, "testTitle", "testPath");

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagRestController)
                .setControllerAdvice(new RestErrorHandler())
                .build();
    }

    @Test
    public void getAllPostsSuccess() throws Exception {
        given(tagService.getAllTags()).willReturn(Collections.singletonList(tag));
        mockMvc.perform(get("/tags"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(JsonConverter.convertToJson(Collections.singletonList(tag))));
        verify(tagService, times(1)).getAllTags();
    }

    @Test
    public void getTagByIdSuccess() throws Exception {
        given(tagService.getTagById(anyLong())).willReturn(tag);
        mockMvc.perform(get("/tags/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(JsonConverter.convertToJson(tag)));
        verify(tagService, times(1)).getTagById(anyLong());
    }

    @Test
    public void getTagByIncorrectId() throws Exception {
        given(tagService.getTagById(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get("/tags/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(tagService, times(1)).getTagById(anyLong());
    }

    @Test
    public void getTagWithNotExistId() throws Exception {
        given(tagService.getTagById(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get("/tags/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(tagService, times(1)).getTagById(anyLong());
    }

    @Test
    public void addTagSuccess() throws Exception {
        given(tagService.addTag(any(Tag.class))).willReturn(anyLong());
        mockMvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonConverter.convertToJson(tag)))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(tagService, times(1)).addTag(any(Tag.class));
    }

    @Test
    public void addIncorrectTag() throws Exception {
        given(tagService.addTag(any(Tag.class))).willThrow(ValidationException.class);
        mockMvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonConverter.convertToJson(tag)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(tagService, times(1)).addTag(any(Tag.class));
    }

    @Test
    public void updateTagSuccess() throws Exception {
        mockMvc.perform(put("/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonConverter.convertToJson(tag)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(tagService, times(1)).updateTag(any(Tag.class));
    }

    @Test
    public void updateIncorrectTag() throws Exception {
        doThrow(ValidationException.class).when(tagService).updateTag(any(Tag.class));
        mockMvc.perform(put("/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonConverter.convertToJson(tag)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(tagService, times(1)).updateTag(any(Tag.class));
    }

    @Test
    public void updateNotExistTag() throws Exception {
        doThrow(NotFoundException.class).when(tagService).updateTag(any(Tag.class));
        mockMvc.perform(put("/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonConverter.convertToJson(tag)))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(tagService, times(1)).updateTag(any(Tag.class));
    }

    @Test
    public void updateTagWithInternalServerException() throws Exception {
        doThrow(InternalServerException.class).when(tagService).updateTag(any(Tag.class));
        mockMvc.perform(put("/tags")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonConverter.convertToJson(tag)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
        verify(tagService, times(1)).updateTag(any(Tag.class));
    }

    @Test
    public void deleteTagSuccess() throws Exception {
        mockMvc.perform(delete("/tags/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isOk());
        verify(tagService, times(1)).deleteTag(anyLong());
    }

    @Test
    public void deleteTagByIncorrectId() throws Exception {
        doThrow(ValidationException.class).when(tagService).deleteTag(anyLong());
        mockMvc.perform(delete("/tags/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(tagService, times(1)).deleteTag(anyLong());
    }

    @Test
    public void deleteNotExistTag() throws Exception {
        doThrow(NotFoundException.class).when(tagService).deleteTag(anyLong());
        mockMvc.perform(delete("/tags/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(tagService, times(1)).deleteTag(anyLong());
    }

    @Test
    public void deleteTagWithInternalServerException() throws Exception {
        doThrow(InternalServerException.class).when(tagService).deleteTag(anyLong());
        mockMvc.perform(delete("/tags/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
        verify(tagService, times(1)).deleteTag(anyLong());
    }
}