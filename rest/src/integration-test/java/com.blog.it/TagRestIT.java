package com.blog.it;

import com.blog.JsonConverter;
import com.blog.controller.config.ControllerTestConfiguration;
import com.blog.model.Tag;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ControllerTestConfiguration.class)
public class TagRestIT extends AbstractTestIT {

    private static String CORRECT_TAG_ID = "1";
    private static String DELETED_TAG_ID = "4";
    private static String NOT_EXIST_TAG_ID = "1238";
    private static String INCORRECT_TAG_ID = "-1";
    private static String NULL = "";
    private static String SLASH = "/";

    private static Tag CORRECT_TAG = new Tag(null, "test4", "test4");
    private static Tag INCORRECT_TAG = new Tag(null, "t", "test1");
    private static Tag NOT_EXIST_TAG = new Tag(20L, "t21221", "test1");
    private static Tag UPDATED_TAG = new Tag(1L, "updatedData", "path_4");

    @Autowired
    private JsonConverter jsonConverter;

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
        HttpEntity<String> entity = new HttpEntity<>(jsonConverter.convertToJson(CORRECT_TAG), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void addIncorrectTag() {
        HttpEntity<String> entity = new HttpEntity<>(jsonConverter.convertToJson(INCORRECT_TAG), headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.POST, entity, Long.class);
    }

    @Test
    public void updateTagSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(jsonConverter.convertToJson(UPDATED_TAG), headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void updateIncorrectTag() {
        HttpEntity<String> entity = new HttpEntity<>(jsonConverter.convertToJson(INCORRECT_TAG), headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void updateNotExistTag() {
        HttpEntity<String> entity = new HttpEntity<>(jsonConverter.convertToJson(NOT_EXIST_TAG), headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.PUT, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteTagSuccess() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(DELETED_TAG_ID)),
                HttpMethod.DELETE, entity, Tag.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void deleteTagByIncorrectId() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(INCORRECT_TAG_ID)),
                HttpMethod.DELETE, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void deleteNotExistTag() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Tag> response = restTemplate.exchange(
                createURLWithPort(SLASH.concat(NOT_EXIST_TAG_ID)),
                HttpMethod.DELETE, entity, Tag.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
