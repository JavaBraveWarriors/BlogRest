package com.blog.it;

import com.blog.Author;
import com.blog.PostListWrapper;
import com.blog.response.ExceptionResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static com.blog.JsonConverter.convertToJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AuthorRestIT extends AbstractTestIT {

    private static String CORRECT_AUTHOR_ID = "1";
    private static String DELETED_AUTHOR_ID = "4";
    private static String INCORRECT_AUTHOR_ID = "-8";
    private static String NOT_EXIST_AUTHOR_ID = "8";
    private static String NOT_EXIST_AUTHOR_LOGIN = "notExistLogin";
    private static String CORRECT_AUTHOR_LOGIN = "testLogin1";
    private static String INCORRECT_AUTHOR_LOGIN = "";

    private static String NULL = "";
    private static String SLASH = "/";

    private static Author incorrectAuthor = new Author(
            null,
            "l",
            "t",
            null,
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone"
    );

    private static Author author = new Author(
            null,
            "test@mail.ru",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");

    private static Author authorExist = new Author(
            2L,
            "testMail2@mail.ru",
            "updatedLog",
            "testPsw2",
            "testFirst2",
            "testLast2",
            null,
            null
    );

    private static Author authorNotExist = new Author(
            19L,
            "testMail2@mail.ru",
            "updatedLog",
            "testPsw2",
            "testFirst2",
            "testLast2",
            null,
            null
    );

    private static Author incorrectUpdatedAuthor = new Author(
            2L,
            "1.ru",
            "updatedLog",
            "s",
            "testFirst2",
            "testLast2",
            null,
            null
    );

    @BeforeClass
    public static void setUp() {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        endpoint = "authors";
    }

    @Test
    public void getAuthorByIdSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(CORRECT_AUTHOR_ID)),
                HttpMethod.GET, entity, Author.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CORRECT_AUTHOR_ID, response.getBody().getId().toString());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getAuthorByIncorrectId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<ExceptionResponse> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_AUTHOR_ID)),
                HttpMethod.GET, entity, ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getAuthorWithNotExistId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(NOT_EXIST_AUTHOR_ID)),
                HttpMethod.GET, entity, Author.class);
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<PostListWrapper> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(CORRECT_AUTHOR_ID).concat("/posts")),
                HttpMethod.GET, entity, PostListWrapper.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getAllPostsByAuthorIncorrectId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_AUTHOR_ID).concat("/posts")),
                HttpMethod.GET, entity, List.class);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getAllPostsWithAuthorNotExistId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(NOT_EXIST_AUTHOR_ID).concat("/posts")),
                HttpMethod.GET, entity, List.class);
    }

    @Test
    public void getAuthorByLoginSuccess() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat("login").concat(SLASH).concat(CORRECT_AUTHOR_LOGIN)),
                HttpMethod.GET, entity, Author.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CORRECT_AUTHOR_LOGIN, response.getBody().getLogin());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getAuthorByIncorrectLogin() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat("login").concat(SLASH).concat(INCORRECT_AUTHOR_LOGIN)),
                HttpMethod.GET, entity, Author.class);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getAuthorWithNotExistLogin() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat("login").concat(SLASH).concat(NOT_EXIST_AUTHOR_LOGIN)),
                HttpMethod.GET, entity, Author.class);
    }

    @Test
    public void addAuthorSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(author), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void addIncorrectAuthor() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(incorrectAuthor), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);
    }

    @Test
    public void updateAuthorSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(authorExist), headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Author.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void updateIncorrectAuthor() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(incorrectUpdatedAuthor), headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Author.class);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void updateNotExistAuthor() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(authorNotExist), headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Author.class);
    }

    @Test
    public void deleteAuthorSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(DELETED_AUTHOR_ID)),
                HttpMethod.DELETE, entity, Author.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void deleteIncorrectAuthor() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_AUTHOR_ID)),
                HttpMethod.DELETE, entity, Author.class);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void deleteNotExistAuthor() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Author> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(NOT_EXIST_AUTHOR_ID)),
                HttpMethod.DELETE, entity, Author.class);
    }
}
