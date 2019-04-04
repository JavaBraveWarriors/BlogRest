package com.blog.controller;

import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.handler.RestErrorHandler;
import com.blog.model.Author;
import com.blog.model.PostListWrapper;
import com.blog.model.ResponsePostDto;
import com.blog.service.AuthorService;
import com.blog.service.PostService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthorRestControllerTest extends AbstractControllerTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PostService postService;

    @Autowired
    private AuthorRestController authorRestController;

    private MockMvc mockMvc;

    private static PostListWrapper postListWrapper = new PostListWrapper();
    private static final Long CORRECT_AUTHOR_ID = 2L;
    private static final Long CREATED_AUTHOR_ID = 12L;
    private static final Long INCORRECT_AUTHOR_ID = -12L;
    private static final Long NOT_EXIST_AUTHOR_ID = 123L;
    private static final String CORRECT_AUTHOR_LOGIN = "CorrectLogin";
    private static final String INCORRECT_AUTHOR_LOGIN = "1";
    private static final String NOT_EXIST_AUTHOR_LOGIN = "NotExistLogin";

    private static Author CORRECT_AUTHOR = new Author(
            1L,
            "test@mail.ru",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");
    private static Author INCORRECT_AUTHOR = new Author(
            1L,
            "incorrectEmail",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");

    private static ResponsePostDto POST = new ResponsePostDto(
            1L,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );

    @Before
    public void setUp() {
        postListWrapper.setPosts(Collections.singletonList(POST));
        mockMvc = MockMvcBuilders.standaloneSetup(authorRestController)
                .setControllerAdvice(new RestErrorHandler())
                .alwaysDo(print())
                .build();
    }

    @After
    public void updateData() {
        Mockito.reset(authorService, postService);
    }

    @Test
    public void getAuthorByIdSuccess() throws Exception {
        given(authorService.getAuthorById(anyLong())).willReturn(CORRECT_AUTHOR);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), CORRECT_AUTHOR_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonConverter.convertToJson(CORRECT_AUTHOR)));
        verify(authorService, times(1)).getAuthorById(anyLong());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void getAuthorByIncorrectId() throws Exception {
        given(authorService.getAuthorById(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), INCORRECT_AUTHOR_ID))
                .andExpect(status().isBadRequest());
        verify(authorService, times(1)).getAuthorById(anyLong());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void getAuthorWithNotExistId() throws Exception {
        given(authorService.getAuthorById(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}"), NOT_EXIST_AUTHOR_ID))
                .andExpect(status().isNotFound());
        verify(authorService, times(1)).getAuthorById(anyLong());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() throws Exception {
        given(postService.getAllPostsByAuthorId(anyLong())).willReturn(postListWrapper);
        mockMvc.perform(get(getEndpoint().concat("/{id}/posts"), CORRECT_AUTHOR_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(postListWrapper)));
        verify(postService, times(1)).getAllPostsByAuthorId(anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getAllPostsByAuthorIncorrectId() throws Exception {
        given(postService.getAllPostsByAuthorId(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}/posts"), INCORRECT_AUTHOR_ID))
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getAllPostsByAuthorId(anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getAllPostsWithAuthorNotExistId() throws Exception {
        given(postService.getAllPostsByAuthorId(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get(getEndpoint().concat("/{id}/posts"), NOT_EXIST_AUTHOR_ID))
                .andExpect(status().isNotFound());
        verify(postService, times(1)).getAllPostsByAuthorId(anyLong());
        verifyNoMoreInteractions(postService);
    }

    @Test
    public void getAuthorByLoginSuccess() throws Exception {
        given(authorService.getAuthorByLogin(anyString())).willReturn(CORRECT_AUTHOR);
        mockMvc.perform(get(getEndpoint().concat("/login/{login}"), CORRECT_AUTHOR_LOGIN))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonConverter.convertToJson(CORRECT_AUTHOR)));
        verify(authorService, times(1)).getAuthorByLogin(anyString());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void getAuthorByIncorrectLogin() throws Exception {
        given(authorService.getAuthorByLogin(anyString())).willThrow(ValidationException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(getEndpoint().concat("/login/{login}"), INCORRECT_AUTHOR_LOGIN))
                .andExpect(status().isBadRequest());
        verify(authorService, times(1)).getAuthorByLogin(anyString());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void getAuthorWithNotExistLogin() throws Exception {
        given(authorService.getAuthorByLogin(anyString())).willThrow(NotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(getEndpoint().concat("/login/{login}"), NOT_EXIST_AUTHOR_LOGIN))
                .andExpect(status().isNotFound());
        verify(authorService, times(1)).getAuthorByLogin(anyString());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void addAuthorSuccess() throws Exception {
        given(authorService.addAuthor(any(Author.class))).willReturn(CREATED_AUTHOR_ID);
        mockMvc.perform(post(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_AUTHOR)))
                .andExpect(status().isCreated())
                .andExpect(content().string(is(CREATED_AUTHOR_ID.toString())));
        verify(authorService, times(1)).addAuthor(any(Author.class));
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void addIncorrectAuthor() throws Exception {
        mockMvc.perform(post(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(INCORRECT_AUTHOR)))
                .andExpect(status().isBadRequest());
        verify(authorService, times(0)).addAuthor(any(Author.class));
        verifyZeroInteractions(authorService);
    }

    @Test
    public void updateAuthorSuccess() throws Exception {
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(CORRECT_AUTHOR)))
                .andExpect(status().isOk());
        verify(authorService, times(1)).updateAuthor(any(Author.class));
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void updateIncorrectSuccess() throws Exception {
        mockMvc.perform(put(getEndpoint())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonConverter.convertToJson(INCORRECT_AUTHOR)))
                .andExpect(status().isBadRequest());
        verify(authorService, times(0)).updateAuthor(any(Author.class));
        verifyZeroInteractions(authorService);
    }

    @Test
    public void deleteAuthorSuccess() throws Exception {
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), CORRECT_AUTHOR_ID))
                .andExpect(status().isOk());
        verify(authorService, times(1)).deleteAuthor(anyLong());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void deleteIncorrectAuthor() throws Exception {
        doThrow(ValidationException.class).when(authorService).deleteAuthor(anyLong());
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), INCORRECT_AUTHOR_ID))
                .andExpect(status().isBadRequest());
        verify(authorService, times(1)).deleteAuthor(anyLong());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void deleteNotExistAuthor() throws Exception {
        doThrow(NotFoundException.class).when(authorService).deleteAuthor(anyLong());
        mockMvc.perform(delete(getEndpoint().concat("/{id}"), NOT_EXIST_AUTHOR_ID))
                .andExpect(status().isNotFound());
        verify(authorService, times(1)).deleteAuthor(anyLong());
        verifyNoMoreInteractions(authorService);
    }

    protected String getEndpoint() {
        return "/authors";
    }
}