package com.blog.controller;

import com.blog.Author;
import com.blog.controller.handler.RestErrorHandler;
import com.blog.service.AuthorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class AuthorRestControllerTest {

    @Mock
    private AuthorService authorServiceMock;

    @InjectMocks
    private AuthorRestController authorRestController;

    private MockMvc mockMvc;

    private static Author authors = new Author(
            1L,
            "test@mail.ru",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorRestController)
                .setControllerAdvice(new RestErrorHandler())
                .build();
    }

    @Test
    public void getAllAuthors() {

    }

    @Test
    public void getAuthorById() {
    }

    @Test
    public void getAllPostsByAuthorId() {
    }

    @Test
    public void getAuthorByLogin() {
    }

    @Test
    public void addAuthor() {
    }

    @Test
    public void updateAuthor() {
    }

    @Test
    public void deleteAuthor() {
    }
}