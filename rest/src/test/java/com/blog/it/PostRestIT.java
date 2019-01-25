package com.blog.it;

import com.blog.Post;
import com.blog.Tag;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
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

    private static List<Tag> tags = new ArrayList<>();
    private static List<Tag> incorrectTags = new ArrayList<>();


    private static Post correctPost = new Post(
            null,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );

    private static Post existPost = new Post(
            1L,
            "title1",
            "testDescription1",
            "testText1",
            "",
            1L
    );

    private static Post authorIdIncorrectPost = new Post(
            null,
            "testTitle",
            "testDescription",
            "testText",
            "testPath",
            -2L
    );

    private static Post authorNotExistIncorrectPost = new Post(
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
        tags.add(new Tag(2L, "dogs", "path_2"));
        tags.add(new Tag(3L, "cats", "path_3"));
        incorrectTags.add(new Tag(21L, "notExist", "path_1212"));
    }


    @Test
    public void getAllPosts() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.GET, entity, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
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
                createURLWithPort("?from=".concat(CORRECT_INITIAL_NUMBER).concat("&quantity=".concat(CORRECT_QUANTITY_NUMBER))),
                HttpMethod.GET, entity, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getPostsByIncorrectInitialIdAndIncorrectQuantity() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort("?from=".concat(INCORRECT_INITIAL_NUMBER).concat("&quantity=".concat(INCORRECT_QUANTITY_NUMBER))),
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

    @Test(expected = HttpClientErrorException.BadRequest.class)
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

    @Test
    public void deleteIncorrectPost() {
        existPost.setTags(tags);
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(existPost), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_POST_ID)),
                HttpMethod.DELETE, entity, Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}