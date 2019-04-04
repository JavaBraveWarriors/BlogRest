package com.blog.controller;

import com.blog.controller.config.ControllerTestConfiguration;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.handler.RestErrorHandler;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.service.CommentService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ControllerTestConfiguration.class)
public class CommentRestControllerTest extends AbstractControllerTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRestController commentRestController;

    private MockMvc mockMvc;
    private static Comment CORRECT_COMMENT;
    private static Comment INCORRECT_COMMENT;
    private static String COMMENT_TEXT = "testText";
    private static final Long CORRECT_AUTHOR_ID = 2L;
    private static final Long CORRECT_POST_ID = 10L;
    private static final Long CORRECT_COMMENT_ID = 13L;
    private static final Long INCORRECT_COMMENT_ID = -123L;
    private static final Long NOT_EXIST_COMMENT_ID = 113L;
    private static final Long COUNT_COMMENTS_PAGES = 2L;
    private static final Long COUNT_COMMENTS_IN_POST = 12L;
    private static final Long INCORRECT_POST_ID = -12L;
    private static CommentListWrapper COMMENT_LIST_WRAPPER = new CommentListWrapper();

    private static final Long REQUIRED_PAGE = 2L;
    private static final Long REQUIRED_SIZE = 5L;

    @Value("${commentController.defaultResponseCommentPage}")
    private Long DEFAULT_COMMENT_PAGE;

    @Value("${commentController.defaultResponseCommentSize}")
    private Long DEFAULT_COMMENT_SIZE;

    @BeforeClass
    public static void setData() {
        CORRECT_COMMENT = new Comment(
                COMMENT_TEXT,
                CORRECT_AUTHOR_ID,
                CORRECT_POST_ID);
        INCORRECT_COMMENT = new Comment(COMMENT_TEXT, CORRECT_AUTHOR_ID, null);
        COMMENT_LIST_WRAPPER.setCommentsPage(Collections.singletonList(CORRECT_COMMENT));
        COMMENT_LIST_WRAPPER.setCountCommentsInPost(COUNT_COMMENTS_PAGES);
        COMMENT_LIST_WRAPPER.setCountCommentsInPost(COUNT_COMMENTS_IN_POST);
    }

    @After
    public void updateData() {
        Mockito.reset(commentService);
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentRestController)
                .setControllerAdvice(new RestErrorHandler())
                .alwaysDo(print())
                .build();
    }

    @Test
    public void getCommentByIdSuccess() throws Exception {
        given(commentService.getCommentById(anyLong())).willReturn(CORRECT_COMMENT);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), CORRECT_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(CORRECT_COMMENT)));
        verify(commentService, times(1)).getCommentById(anyLong());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getCommentByIncorrectId() throws Exception {
        given(commentService.getCommentById(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), INCORRECT_COMMENT_ID))
                .andExpect(status().isBadRequest());
        verify(commentService, times(1)).getCommentById(anyLong());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getCommentByNotExistId() throws Exception {
        given(commentService.getCommentById(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), NOT_EXIST_COMMENT_ID))
                .andExpect(status().isNotFound());
        verify(commentService, times(1)).getCommentById(anyLong());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getListCommentsByPostIdWithPaginationSuccess() throws Exception {
        given(commentService.getListCommentsByPostIdWithPagination(anyLong(), anyLong(), anyLong()))
                .willReturn(COMMENT_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint())
                .param("page", REQUIRED_PAGE.toString())
                .param("size", REQUIRED_SIZE.toString())
                .param("postId", CORRECT_POST_ID.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(COMMENT_LIST_WRAPPER)));
        verify(commentService, times(1))
                .getListCommentsByPostIdWithPagination(REQUIRED_PAGE, REQUIRED_SIZE, CORRECT_POST_ID);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getListCommentsByPostIdWithPaginationWithoutPageSuccess() throws Exception {
        given(commentService.getListCommentsByPostIdWithPagination(anyLong(), anyLong(), anyLong()))
                .willReturn(COMMENT_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint())
                .param("size", REQUIRED_SIZE.toString())
                .param("postId", CORRECT_POST_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(COMMENT_LIST_WRAPPER)));
        verify(commentService, times(1))
                .getListCommentsByPostIdWithPagination(DEFAULT_COMMENT_PAGE, REQUIRED_SIZE, CORRECT_POST_ID);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getListCommentsByPostIdWithPaginationWithoutSizeSuccess() throws Exception {
        given(commentService.getListCommentsByPostIdWithPagination(anyLong(), anyLong(), anyLong()))
                .willReturn(COMMENT_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint())
                .param("page", REQUIRED_PAGE.toString())
                .param("postId", CORRECT_POST_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(COMMENT_LIST_WRAPPER)));
        verify(commentService, times(1))
                .getListCommentsByPostIdWithPagination(REQUIRED_PAGE, DEFAULT_COMMENT_SIZE, CORRECT_POST_ID);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getListCommentsByPostIdWithPaginationWithoutPostId() throws Exception {
        given(commentService.getListCommentsByPostIdWithPagination(anyLong(), anyLong(), anyLong()))
                .willReturn(COMMENT_LIST_WRAPPER);
        mockMvc.perform(get(getEndpoint())
                .param("page", REQUIRED_PAGE.toString())
                .param("size", REQUIRED_SIZE.toString()))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getCountOfCommentsByPostId() throws Exception {
        given(commentService.getCountOfPagesWithPagination(anyLong(), anyLong())).willReturn(COUNT_COMMENTS_IN_POST);
        mockMvc.perform(get(getEndpoint().concat("/countPages"))
                .param("size", REQUIRED_SIZE.toString())
                .param("postId", CORRECT_POST_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(is(COUNT_COMMENTS_IN_POST.toString())));
        verify(commentService, times(1)).getCountOfPagesWithPagination(CORRECT_POST_ID, REQUIRED_SIZE);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getCountOfCommentsByPostIdWithoutSize() throws Exception {
        given(commentService.getCountOfPagesWithPagination(anyLong(), anyLong())).willReturn(COUNT_COMMENTS_IN_POST);
        mockMvc.perform(get(getEndpoint().concat("/countPages"))
                .param("postId", CORRECT_POST_ID.toString()))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void getCountOfCommentsByIncorrectPostId() throws Exception {
        given(commentService.getCountOfPagesWithPagination(anyLong(), anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get(getEndpoint().concat("/countPages"))
                .param("size", REQUIRED_SIZE.toString())
                .param("postId", INCORRECT_POST_ID.toString()))
                .andExpect(status().isBadRequest());
        verify(commentService, times(1)).getCountOfPagesWithPagination(INCORRECT_POST_ID, REQUIRED_SIZE);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void updateCommentSuccess() throws Exception {
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_COMMENT)))
                .andExpect(status().isOk());
        verify(commentService, times(1)).updateComment(any(Comment.class));
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void updateIncorrectCommentSuccess() throws Exception {
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(INCORRECT_COMMENT)))
                .andExpect(status().isBadRequest());
        verifyNoMoreInteractions(commentService);
    }

    protected String getEndpoint() {
        return "/comments";
    }
}