package com.blog.controller;

import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.handler.RestErrorHandler;
import com.blog.model.*;
import com.blog.service.PostService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class PostRestControllerTest extends AbstractControllerTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRestController postRestController;

    private MockMvc mockMvc;

    private static final Long REQUIRED_PAGE = 2L;
    private static final Long REQUIRED_SIZE = 5L;
    private static final Long INCORRECT_REQUIRED_SIZE = -12L;
    private static final String REQUIRED_SORT = "id";

    @Value("${postController.defaultResponsePostSize}")
    private Long DEFAULT_RESPONSE_POST_SIZE;
    @Value("${postController.defaultResponsePostPage}")
    private Long DEFAULT_RESPONSE_POST_PAGE;
    @Value("${postController.defaultResponsePostSort}")
    private String DEFAULT_RESPONSE_POST_SORT;

    private static PostListWrapper POST_LIST_WRAPPER = new PostListWrapper();

    private static final Long COUNT_PAGES = 1L;
    private static final Long COUNT_POSTS = 12L;

    private static final Long CREATED_POST_ID = 412L;
    private static final Long CORRECT_POST_ID = 23L;
    private static final Long INCORRECT_POST_ID = -3L;
    private static final Long NOT_EXIST_POST_ID = 223L;

    private static final Long CORRECT_AUTHOR_ID = 2L;

    private static final Long CORRECT_TAG_ID = 4L;
    private static final Long INCORRECT_TAG_ID = -212L;

    private static final Long CORRECT_COMMENT_ID = 24L;
    private static final Long INCORRECT_COMMENT_ID = -4L;
    private static final String TEST_TEXT = "testTitle";

    private static final ResponsePostDto post = new ResponsePostDto(
            1L,
            TEST_TEXT,
            TEST_TEXT,
            TEST_TEXT,
            "",
            1L
    );

    private static final RequestPostDto CORRECT_POST = new RequestPostDto(
            null,
            TEST_TEXT,
            TEST_TEXT,
            TEST_TEXT,
            "",
            1L
    );
    private static final RequestPostDto INCORRECT_POST = new RequestPostDto(
            null,
            null,
            TEST_TEXT,
            TEST_TEXT,
            null,
            null
    );

    private static final Comment CORRECT_COMMENT = new Comment(
            TEST_TEXT,
            CORRECT_AUTHOR_ID,
            CORRECT_POST_ID
    );
    private static Comment INCORRECT_COMMENT = new Comment(
            null,
            CORRECT_AUTHOR_ID,
            CORRECT_POST_ID);

    @BeforeClass
    public static void setData() {
        ArrayList<Tag> tags = new ArrayList<>();
        POST_LIST_WRAPPER.setPosts(Collections.singletonList(post));
        POST_LIST_WRAPPER.setCountPages(COUNT_PAGES);
        POST_LIST_WRAPPER.setCountPosts(COUNT_POSTS);
        tags.add(new Tag(1L, "", "asd"));
        post.setTags(tags);
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postRestController)
                .setControllerAdvice(new RestErrorHandler())
                .alwaysDo(print())
                .build();
    }

    @After
    public void updateData() {
        Mockito.reset(postService);
    }

    @Test
    public void getPostByIdSuccess() throws Exception {
        given(postService.getPostById(anyLong())).willReturn(post);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), CORRECT_POST_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(post)));
        verify(postService, times(1)).getPostById(anyLong());
        verifyNoMoreInteractions(postService);

    }

    @Test
    public void getPostByIncorrectId() throws Exception {
        given(postService.getPostById(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), INCORRECT_POST_ID))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getPostById(anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getPostWithNotExistId() throws Exception {
        given(postService.getPostById(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), NOT_EXIST_POST_ID))
                .andExpect(status().isNotFound());
        verify(postService, times(1)).getPostById(anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void addTagToPost() throws Exception {
        mockMvc.perform(put(getEndpoint().concat("/{id}/tag/{tagId}"), CORRECT_POST_ID, CORRECT_TAG_ID))
                .andExpect(status().isOk());
        verify(postService, times(1)).addTagToPost(anyLong(), anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void addTagToIncorrectPost() throws Exception {
        doThrow(ValidationException.class).when(postService).addTagToPost(anyLong(), anyLong());
        mockMvc.perform(put(getEndpoint().concat("/{id}/tag/{tagId}"), INCORRECT_POST_ID, CORRECT_TAG_ID))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).addTagToPost(anyLong(), anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void addIncorrectTagToPost() throws Exception {
        doThrow(ValidationException.class).when(postService).addTagToPost(anyLong(), anyLong());
        mockMvc.perform(put(getEndpoint().concat("/{id}/tag/{tagId}"), CORRECT_POST_ID, INCORRECT_TAG_ID))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).addTagToPost(anyLong(), anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void deleteTagInPostSuccess() throws Exception {
        mockMvc.perform(delete(getEndpoint().concat("/{id}/tag/{tagId}"), CORRECT_POST_ID, CORRECT_TAG_ID))
                .andExpect(status().isOk());
        verify(postService, times(1)).deleteTagInPost(anyLong(), anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void deleteTagInIncorrectPost() throws Exception {
        mockMvc.perform(delete(getEndpoint().concat("/{id}/tag/{tagId}"), INCORRECT_POST_ID, CORRECT_TAG_ID))
                .andExpect(status().isOk());
        verify(postService, times(1)).deleteTagInPost(anyLong(), anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void deleteIncorrectTagInPost() throws Exception {
        mockMvc.perform(delete(getEndpoint().concat("/{id}/tag/{tagId}"), CORRECT_POST_ID, INCORRECT_TAG_ID))
                .andExpect(status().isOk());
        verify(postService, times(1)).deleteTagInPost(anyLong(), anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void addCorrectCommentToPost() throws Exception {
        mockMvc.perform(post(getEndpoint().concat("/comment"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_COMMENT)))
                .andExpect(status().isCreated());
        verify(postService, times(1)).addCommentToPost(any(Comment.class));
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void addIncorrectCommentToPost() throws Exception {
        mockMvc.perform(post(getEndpoint().concat("/comment"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(INCORRECT_COMMENT)))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void deleteCommentInPostSuccess() throws Exception {
        mockMvc.perform(delete(getEndpoint().concat("/{id}/comment/{commentId}"), CORRECT_POST_ID, CORRECT_COMMENT_ID))
                .andExpect(status().isOk());
        verify(postService, times(1)).deleteCommentInPost(CORRECT_POST_ID, CORRECT_COMMENT_ID);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void deleteCommentInIncorrectPost() throws Exception {
        doThrow(ValidationException.class).when(postService).deleteCommentInPost(anyLong(), anyLong());
        mockMvc.perform(delete(getEndpoint().concat("/{id}/comment/{commentId}"), INCORRECT_POST_ID, CORRECT_COMMENT_ID))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).deleteCommentInPost(INCORRECT_POST_ID, CORRECT_COMMENT_ID);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void deleteIncorrectCommentInPost() throws Exception {
        doThrow(ValidationException.class).when(postService).deleteCommentInPost(anyLong(), anyLong());
        mockMvc.perform(delete(getEndpoint().concat("/{id}/comment/{commentId}"), CORRECT_POST_ID, INCORRECT_COMMENT_ID))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).deleteCommentInPost(CORRECT_POST_ID, INCORRECT_COMMENT_ID);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getPostsByInitialIdAndQuantitySuccess() throws Exception {
        given(postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class))).willReturn(POST_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint())
                .param("page", REQUIRED_PAGE.toString())
                .param("size", REQUIRED_SIZE.toString())
                .param("sort", REQUIRED_SORT))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(POST_LIST_WRAPPER)));
        verify(postService, times(1)).getPostsWithPaginationAndSorting(REQUIRED_PAGE, REQUIRED_SIZE, REQUIRED_SORT);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getPostsByInitialIdAndQuantityWithoutPageSuccess() throws Exception {
        given(postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class))).willReturn(POST_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint())
                .param("size", REQUIRED_SIZE.toString())
                .param("sort", REQUIRED_SORT))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(POST_LIST_WRAPPER)));
        verify(postService, times(1)).getPostsWithPaginationAndSorting(DEFAULT_RESPONSE_POST_PAGE, REQUIRED_SIZE, REQUIRED_SORT);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getPostsByInitialIdAndQuantityWithoutSizeSuccess() throws Exception {
        given(postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class))).willReturn(POST_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint())
                .param("page", REQUIRED_PAGE.toString())
                .param("sort", REQUIRED_SORT))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(POST_LIST_WRAPPER)));
        verify(postService, times(1)).getPostsWithPaginationAndSorting(REQUIRED_PAGE, DEFAULT_RESPONSE_POST_SIZE, REQUIRED_SORT);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getPostsByInitialIdAndQuantityWithoutSortSuccess() throws Exception {
        given(postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class))).willReturn(POST_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint())
                .param("page", REQUIRED_PAGE.toString())
                .param("size", REQUIRED_SIZE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(POST_LIST_WRAPPER)));
        verify(postService, times(1)).getPostsWithPaginationAndSorting(REQUIRED_PAGE, REQUIRED_SIZE, DEFAULT_RESPONSE_POST_SORT);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getPostsByIncorrectInitialIdAndIncorrectQuantity() throws Exception {
        given(postService.getPostsWithPaginationAndSorting(anyLong(), eq(INCORRECT_REQUIRED_SIZE), any(String.class))).willThrow(ValidationException.class);
        mockMvc.perform(get(getEndpoint())
                .param("page", REQUIRED_PAGE.toString())
                .param("size", INCORRECT_REQUIRED_SIZE.toString()))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getPostsWithPaginationAndSorting(anyLong(), anyLong(), any(String.class));
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getAllPostsByTagIdSuccess() throws Exception {
        given(postService.getAllPostsByTagId(anyLong())).willReturn(POST_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint().concat("/tag/{id}"), CORRECT_TAG_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(POST_LIST_WRAPPER)));
        verify(postService, times(1)).getAllPostsByTagId(anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getAllPostsByIncorrectTagId() throws Exception {
        given(postService.getAllPostsByTagId(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get(getEndpoint().concat("/tag/{id}"), INCORRECT_TAG_ID))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getAllPostsByTagId(anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void addPostSuccess() throws Exception {
        given(postService.addPost(any(RequestPostDto.class))).willReturn(CREATED_POST_ID);
        mockMvc.perform(post(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_POST)))
                .andExpect(status().isCreated())
                .andExpect(content().json(CREATED_POST_ID.toString()));
        verify(postService, times(1)).addPost(any(RequestPostDto.class));
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void addIncorrectPost() throws Exception {
        given(postService.addPost(any(RequestPostDto.class))).willReturn(CREATED_POST_ID);
        mockMvc.perform(post(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(INCORRECT_POST)))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void updatePost() throws Exception {
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_POST)))
                .andExpect(status().isOk());
        verify(postService, times(1)).updatePost(any(RequestPostDto.class));
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void updateIncorrectPost() throws Exception {
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(INCORRECT_POST)))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void deletePostSuccess() throws Exception {
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), CORRECT_POST_ID))
                .andExpect(status().isOk());
        verify(postService, times(1)).deletePost(CORRECT_POST_ID);
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void deleteIncorrectPost() throws Exception {
        doThrow(ValidationException.class).when(postService).deletePost(anyLong());
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), INCORRECT_POST_ID))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).deletePost(INCORRECT_POST_ID);
        verifyNoMoreInteractions(postService);
    }

    protected String getEndpoint() {
        return "/posts";
    }
}