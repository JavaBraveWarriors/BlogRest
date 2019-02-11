package com.blog.it;

import com.blog.Post;
import com.blog.PostForAdd;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static com.blog.JsonConverter.convertToJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PostRestIT extends AbstractTestIT {

    private static String CORRECT_INITIAL_NUMBER = "1";
    private static String INCORRECT_INITIAL_NUMBER = "-3";
    private static String CORRECT_QUANTITY_NUMBER = "1";
    private static String INCORRECT_QUANTITY_NUMBER = "-3";

    private static String CORRECT_POST_ID = "1";
    private static String DELETED_POST_ID = "4";
    private static String INCORRECT_POST_ID = "-2";
    private static String NOT_EXIST_POST_ID = "10";

    private static String NULL = "";
    private static String SLASH = "/";

    private static String CORRECT_TAG_ID = "2";
    private static String INCORRECT_TAG_ID = "-5";
    private static String NOT_EXIST_TAG_ID = "51";

    private static Long[] tags = new Long[]{3L, 4L};
    private static Long[] incorrectTags = new Long[]{21L};

    private static PostForAdd correctPost = new PostForAdd(
            null,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );

    private static PostForAdd existPost = new PostForAdd(
            1L,
            "title1",
            "testDescription1",
            "testText1",
            "",
            1L
    );

    private static PostForAdd authorIdIncorrectPost = new PostForAdd(
            null,
            "testTitle",
            "testDescription",
            "testText",
            "testPath",
            -2L
    );

    private static PostForAdd authorNotExistIncorrectPost = new PostForAdd(
            null,
            "testTitle",
            "testDescription",
            "testText",
            "testPath",
            22L
    );

    @BeforeClass
    public static void setUp() {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        endpoint = "posts";
    }

    @Test
    public void getPostByIdSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Post> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(CORRECT_POST_ID)),
                HttpMethod.GET, entity, Post.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CORRECT_POST_ID, response.getBody().getId().toString());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getPostByIncorrectId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Post> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_POST_ID)),
                HttpMethod.GET, entity, Post.class);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getPostWithNotExistId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Post> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(NOT_EXIST_POST_ID)),
                HttpMethod.GET, entity, Post.class);
    }

    @Test
    public void getPostsByInitialIdAndQuantitySuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort("?page=".concat(CORRECT_INITIAL_NUMBER).concat("&size=".concat(CORRECT_QUANTITY_NUMBER))),
                HttpMethod.GET, entity, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getPostsByIncorrectInitialIdAndIncorrectQuantity() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort("?page=".concat(INCORRECT_INITIAL_NUMBER).concat("&size=".concat(INCORRECT_QUANTITY_NUMBER))),
                HttpMethod.GET, entity, List.class);
    }

    @Test
    public void getAllPostsByTagIdSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat("tag".concat(SLASH).concat(CORRECT_TAG_ID))),
                HttpMethod.GET, entity, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getAllPostsByIncorrectTagId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat("tag".concat(SLASH).concat(INCORRECT_TAG_ID))),
                HttpMethod.GET, entity, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getAllPostsByNotExistTagId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat("tag".concat(SLASH).concat(NOT_EXIST_TAG_ID))),
                HttpMethod.GET, entity, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void addPostSuccess() {
        correctPost.setTags(tags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(correctPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void addPostWithIncorrectTags() {
        correctPost.setTags(incorrectTags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(correctPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void addPostWithIncorrectAuthorId() {
        authorIdIncorrectPost.setTags(tags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(authorIdIncorrectPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void addPostWithNotExistAuthorId() {
        authorNotExistIncorrectPost.setTags(tags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(authorNotExistIncorrectPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);
    }

    @Test
    public void updatePostSuccess() {
        existPost.setTags(tags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(existPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void updatePostWithIncorrectTags() {
        existPost.setTags(incorrectTags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(existPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deletePost() {
        existPost.setTags(tags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(existPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(DELETED_POST_ID)),
                HttpMethod.DELETE, entity, Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void deleteIncorrectPost() {
        existPost.setTags(tags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(existPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_POST_ID)),
                HttpMethod.DELETE, entity, Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}