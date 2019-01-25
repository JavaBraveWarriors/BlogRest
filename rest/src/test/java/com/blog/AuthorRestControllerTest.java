package com.blog;

import com.blog.controller.AuthorRestController;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.handler.RestErrorHandler;
import com.blog.service.AuthorService;
import com.blog.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AuthorRestControllerTest {

    @Mock
    private AuthorService authorServiceMock;

    @Mock
    private PostService postService;

    @InjectMocks
    private AuthorRestController authorRestController;

    private MockMvc mockMvc;

    private static Author author = new Author(
            1L,
            "test@mail.ru",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");

    private static Post post = new Post(
            1L,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorRestController)
                .setControllerAdvice(new RestErrorHandler())
                .build();
        author.setRegistrationTime(LocalDateTime.now());
        post.setTimeOfCreation(LocalDateTime.now());
    }

    @Test
    public void getAuthorByIdSuccess() throws Exception {
        given(authorServiceMock.getAuthorById(anyLong())).willReturn(author);
        mockMvc.perform(get("/authors/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(JsonConverter.convertToJson(author)));
        verify(authorServiceMock, times(1)).getAuthorById(anyLong());
    }

    @Test
    public void getAuthorByIncorrectId() throws Exception {
        given(authorServiceMock.getAuthorById(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get("/authors/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(authorServiceMock, times(1)).getAuthorById(anyLong());
    }

    @Test
    public void getAuthorWithNotExistId() throws Exception {
        given(authorServiceMock.getAuthorById(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get("/authors/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(authorServiceMock, times(1)).getAuthorById(anyLong());
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() throws Exception {
        given(postService.getAllPostsByAuthorId(anyLong())).willReturn(Collections.singletonList(post));
        mockMvc.perform(get("/authors/{id}/posts", anyLong()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(JsonConverter.convertToJson(Collections.singletonList(post))));
        verify(postService, times(1)).getAllPostsByAuthorId(anyLong());
    }

    @Test
    public void getAllPostsByAuthorIncorrectId() throws Exception {
        given(postService.getAllPostsByAuthorId(anyLong())).willThrow(ValidationException.class);
        mockMvc.perform(get("/authors/{id}/posts", anyLong()))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(postService, times(1)).getAllPostsByAuthorId(anyLong());
    }

    @Test
    public void getAllPostsWithAuthorNotExistId() throws Exception {
        given(postService.getAllPostsByAuthorId(anyLong())).willThrow(NotFoundException.class);
        mockMvc.perform(get("/authors/{id}/posts", anyLong()))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(postService, times(1)).getAllPostsByAuthorId(anyLong());
    }


    @Test
    public void getAuthorByLoginSuccess() throws Exception {
        given(authorServiceMock.getAuthorByLogin(anyString())).willReturn(author);
        mockMvc.perform(get("/authors/login/{login}", "testLogin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(JsonConverter.convertToJson(author)));
        verify(authorServiceMock, times(1)).getAuthorByLogin(anyString());
    }

    @Test
    public void getAuthorByIncorrectLogin() throws Exception {
        given(authorServiceMock.getAuthorByLogin(anyString())).willThrow(ValidationException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/login/{login}", "testLogin"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(authorServiceMock, times(1)).getAuthorByLogin(anyString());
    }

    @Test
    public void getAuthorWithNotExistLogin() throws Exception {
        given(authorServiceMock.getAuthorByLogin(anyString())).willThrow(NotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/login/{login}", "testLogin"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(authorServiceMock, times(1)).getAuthorByLogin(anyString());
    }

    @Test
    public void addAuthorSuccess() throws Exception {
        author.setRegistrationTime(null);
        given(authorServiceMock.addAuthor(any(Author.class))).willReturn(anyLong());
        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonConverter.convertToJson(author)))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(authorServiceMock, times(1)).addAuthor(any(Author.class));
    }

    @Test
    public void updateAuthorSuccess() throws Exception {
        author.setRegistrationTime(null);
        mockMvc.perform(put("/authors")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonConverter.convertToJson(author)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(authorServiceMock, times(1)).updateAuthor(any(Author.class));
    }

    @Test
    public void deleteAuthor() throws Exception {
        mockMvc.perform(delete("/authors/{id}", anyLong()))
                .andDo(print())
                .andExpect(status().isOk());
        verify(authorServiceMock, times(1)).deleteAuthor(anyLong());
    }
}