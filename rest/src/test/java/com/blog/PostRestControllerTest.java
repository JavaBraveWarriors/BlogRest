package com.blog;

import com.blog.controller.PostRestController;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.handler.RestErrorHandler;
import com.blog.service.PostService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static com.blog.JsonConverter.convertToJson;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
public class PostRestControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostRestController postRestController;

    private MockMvc mockMvc;

    private static Post post = new Post(
            1L,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );

    @BeforeClass
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postRestController)
                .setControllerAdvice(new RestErrorHandler())
                .build();
        post.setDate(LocalDate.now());
    }

    @Test
    public void getAllPosts() throws Exception {
        given(postService.getAllPosts()).willReturn(Collections.singletonList(post));
        mockMvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(convertToJson(Collections.singletonList(post))));
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    public void getPostByIdSuccess() throws Exception {
        given(postService.getPostById(anyLong())).willReturn(post);
        mockMvc.perform(get("/posts/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertToJson(post)));
        verify(postService, times(1)).getPostById(anyLong());
    }

    @Test
    public void getPostByIdWithValidationException() throws Exception {
        post.setDate(null);
        given(postService.getPostById(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get("/posts/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getPostById(anyLong());
    }

    @Test
    public void getPostByIdWithNotFoundException() throws Exception {
        given(postService.getPostById(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get("/posts/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(postService, times(1)).getPostById(anyLong());
    }

    @Test
    public void getPostsByInitialIdAndQuantitySuccess() throws Exception {
        given(postService.getPostsByInitialIdAndQuantity(anyLong(), anyLong())).willReturn(Collections.singletonList(post));
        mockMvc.perform(get("/posts/from{initial}/{quantity}", anyLong(), anyLong()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertToJson(Collections.singletonList(post))));
        verify(postService, times(1)).getPostsByInitialIdAndQuantity(anyLong(), anyLong());
    }

    @Test
    public void getPostsByInitialIdAndQuantityWithValidationException() throws Exception {
        given(postService.getPostsByInitialIdAndQuantity(anyLong(), anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get("/posts/from{initial}/{quantity}", anyLong(), anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getPostsByInitialIdAndQuantity(anyLong(), anyLong());
    }

    @Test
    public void getAllPostsByTagIdSuccess() throws Exception {
        given(postService.getAllPostsByTagId(anyLong())).willReturn(Collections.singletonList(post));
        mockMvc.perform(get("/posts/tag/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertToJson(Collections.singletonList(post))));
        verify(postService, times(1)).getAllPostsByTagId(anyLong());
    }

    @Test
    public void getAllPostsByTagIdWithValidationException() throws Exception {
        given(postService.getAllPostsByTagId(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get("/posts/tag/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getAllPostsByTagId(anyLong());
    }

    @Test
    public void addPost() throws Exception {
        post.setDate(null);
        given(postService.addPost(any(Post.class))).willReturn(anyLong());
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(post)))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(postService, times(1)).addPost(any(Post.class));
    }

    @Test
    public void updatePost() throws Exception {
        post.setDate(null);
        mockMvc.perform(put("/posts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(post)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(postService, times(1)).updatePost(any(Post.class));
    }

    @Test
    public void deletePost() throws Exception {
        mockMvc.perform(delete("/posts/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isOk());
        verify(postService, times(1)).deletePost(anyLong());
    }
}