package com.blog;

import com.blog.controller.PostRestController;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.handler.RestErrorHandler;
import com.blog.service.PostService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private static Comment comment = new Comment(
            "text",
            1L,
            2L
    );

    @BeforeClass
    public static void setData() {
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1L, "testTitle", "asd"));
        post.setTags(tags);
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postRestController)
                .setControllerAdvice(new RestErrorHandler())
                .build();
        post.setTimeOfCreation(LocalDateTime.now());
    }

    @Test
    public void getAllPosts() throws Exception {
        given(postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class))).willReturn(Collections.singletonList(post));
        mockMvc.perform(get("/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(convertToJson(Collections.singletonList(post))));
        verify(postService, times(1)).getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class));
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
    public void getPostByIncorrectId() throws Exception {
        post.setTimeOfCreation(null);
        given(postService.getPostById(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get("/posts/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getPostById(anyLong());
    }

    @Test
    public void getPostWithNotExistId() throws Exception {
        given(postService.getPostById(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get("/posts/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(postService, times(1)).getPostById(anyLong());
    }

    @Test
    public void addTagToPost() throws Exception {
        mockMvc.perform(put("/posts/{id}/tag/{tagId}", anyLong(), anyLong()))
                .andDo(print())
                .andExpect(status().isOk());
        verify(postService, times(1)).addTagToPost(anyLong(), anyLong());
    }

    @Test
    public void deleteTagInPost() throws Exception {
        mockMvc.perform(delete("/posts/{id}/tag/{tagId}", anyLong(), anyLong()))
                .andDo(print())
                .andExpect(status().isOk());
        verify(postService, times(1)).deleteTagInPost(anyLong(), anyLong());
    }

    @Test
    public void addCommentToPost() throws Exception {
        mockMvc.perform(post("/posts/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(comment)))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(postService, times(1)).addCommentToPost(any(Comment.class));
    }

    @Test
    public void deleteCommentInPost() throws Exception {
        mockMvc.perform(delete("/posts/{id}/comment/{commentId}", anyLong(), anyLong()))
                .andDo(print())
                .andExpect(status().isOk());
        verify(postService, times(1)).deleteCommentInPost(anyLong(), anyLong());
    }

    @Test
    public void getPostsByInitialIdAndQuantitySuccess() throws Exception {
        given(postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class))).willReturn(Collections.singletonList(post));
        mockMvc.perform(get("/posts?page={page}&size={size}&sort={sort}", anyLong(), anyLong(), any(String.class)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertToJson(Collections.singletonList(post))));
        verify(postService, times(1)).getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class));
    }

    @Test
    public void getPostsByIncorrectInitialIdAndIncorrectQuantity() throws Exception {
        given(postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class))).willThrow(ValidationException.class);
        mockMvc.perform(get("/posts?page={page}&size={size}&sort={sort}", anyLong(), anyLong(), any(String.class)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class));
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
    public void getAllPostsByIncorrectTagId() throws Exception {
        given(postService.getAllPostsByTagId(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get("/posts/tag/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getAllPostsByTagId(anyLong());
    }

    @Test
    public void addPost() throws Exception {
        post.setTimeOfCreation(null);
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
        post.setTimeOfCreation(null);
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