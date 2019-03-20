package com.blog.it;

import com.blog.it.config.JmsConfig;
import com.blog.model.Comment;
import com.blog.model.Tag;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Message;
import javax.jms.Queue;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JmsConfig.class})
public class JMSRestIT extends AbstractTestIT {

    private static final Long MAX_WAITING_TIME = 1000L;
    private static final Long MILLISECONDS = 50L;

    private static Long CORRECT_POST_ID_ADDED_COMMENT = 3L;
    private static Long CORRECT_USER_ID = 1L;
    private static String NEW_COMMENT_TEXT = "newText4";
    private static String NEW_TAG_TITLE = "newTitle4";
    private static String NEW_TAG_PATH_IMAGE = "newPathImage4";
    private static String NULL = "";
    private static String COUNT_COMMENTS = "1111111";

    private static Tag TAG = new Tag(
            null,
            NEW_TAG_TITLE,
            NEW_TAG_PATH_IMAGE
    );
    private static Comment COMMENT = new Comment(
            NEW_COMMENT_TEXT,
            CORRECT_USER_ID,
            CORRECT_POST_ID_ADDED_COMMENT
    );

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("comment")
    private Queue queueComment;

    @Autowired
    @Qualifier("tag")
    private Queue queueTag;

    @BeforeClass
    public static void setUp() {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void listenCommentQueue() throws InterruptedException {
        Long initSize = getCountCommentsInPost(CORRECT_POST_ID_ADDED_COMMENT);
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(queueComment, COMMENT, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });
        jmsTemplate.setReceiveTimeout(10000);

        Long time = MAX_WAITING_TIME;
        Long currentSize = getCountCommentsInPost(CORRECT_POST_ID_ADDED_COMMENT);

        while (time > 0 && currentSize.equals(initSize)) {
            Thread.sleep(MILLISECONDS);
            time -= MILLISECONDS;
            currentSize = getCountCommentsInPost(CORRECT_POST_ID_ADDED_COMMENT);
        }

        assertEquals(initSize + 1L, currentSize.longValue());
    }

    @Test
    public void receiveCorrectTagToAddSuccess() throws InterruptedException {
        int initialSize = getAllTags().size();

        jmsTemplate.convertAndSend(queueTag, TAG);
        jmsTemplate.setReceiveTimeout(10_000);

        Long time = MAX_WAITING_TIME;
        int currentSize = getAllTags().size();

        while (time > 0 && currentSize == initialSize) {
            Thread.sleep(MILLISECONDS);
            time -= MILLISECONDS;
            currentSize = getAllTags().size();
        }

        assertEquals(initialSize + 1, currentSize);
    }

    private List<Tag> getAllTags() {
        endpoint = "tags";
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Tag>> response = restTemplate.exchange(
                createURLWithPort(NULL),
                HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Tag>>() {
                });
        return response.getBody();
    }

    private Long getCountCommentsInPost(Long postId) {
        endpoint = "comments";
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Long> response = restTemplate.exchange(
                createURLWithPort(
                        "/count"
                                .concat("?postId=")
                                .concat(postId.toString())
                                .concat("&size=")
                                .concat(COUNT_COMMENTS)),
                HttpMethod.GET, entity,
                Long.class);
        return response.getBody();
    }
}