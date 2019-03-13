package com.blog.it;

import com.blog.model.Tag;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static com.blog.JsonConverter.convertToJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TagRestIT extends AbstractTestIT {

    private static String CORRECT_TAG_ID = "1";
    private static String DELETED_TAG_ID = "4";
    private static String NOT_EXIST_TAG_ID = "1238";
    private static String INCORRECT_TAG_ID = "-1";
    private static String NULL = "";
    private static String SLASH = "/";

    private static Tag correctTag = new Tag(null, "test4", "test4");
    private static Tag incorrectTag = new Tag(null, "t", "test1");
    private static Tag notExistTag = new Tag(20L, "t21221", "test1");
    private static Tag updatedTag = new Tag(1L, "updatedData", "path_4");

    @BeforeClass
    public static void setUp() {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        endpoint = "tags";
    }

    @Test
    public void getAllTagsSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.GET, entity, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getTagByIdSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(CORRECT_TAG_ID)),
                HttpMethod.GET, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getTagByIncorrectId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_TAG_ID)),
                HttpMethod.GET, entity, Tag.class);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getTagWithNotExistId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(NOT_EXIST_TAG_ID)),
                HttpMethod.GET, entity, Tag.class);
    }

    @Test
    public void addTagSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(correctTag), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void addIncorrectTag() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(incorrectTag), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);
    }

    @Test
    public void updateTagSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(updatedTag), headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void updateIncorrectTag() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(incorrectTag), headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void updateNotExistTag() {
        HttpEntity<String> entity = new HttpEntity<>(convertToJson(notExistTag), headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteTagSuccess() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(DELETED_TAG_ID)),
                HttpMethod.DELETE, entity, Tag.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void deleteTagByIncorrectId() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_TAG_ID)),
                HttpMethod.DELETE, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void deleteNotExistTag() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(NOT_EXIST_TAG_ID)),
                HttpMethod.DELETE, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
