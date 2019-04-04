package com.blog.jmsConsumer;

import com.blog.model.Comment;
import com.blog.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class CommentConsumer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CREATED_STATUS = "Created";

    private CommentService commentService;

    @Autowired
    public CommentConsumer(CommentService commentService) {
        this.commentService = commentService;
    }

    @JmsListener(destination = "${queue.comment}")
    @SendTo("${queue.responseComment}")
    public Message<String> listenCommentQueue(Message<Comment> commentMessage,
                                              @Header(JmsHeaders.MESSAGE_ID) String messageId) {
        LOGGER.debug("Add new Comment from queue [{}]", commentMessage.getPayload());

        String responseMessage = CREATED_STATUS;
        try {
            commentService.addComment(commentMessage.getPayload());
        } catch (RuntimeException ex) {
            LOGGER.error("Could not add new comment exception = [{}]", ex.toString());
            responseMessage = ex.toString();
        }

        LOGGER.debug("Send response after attempt to add comment. Message = [{}]", responseMessage);

        return MessageBuilder
                .withPayload(responseMessage)
                .setHeader("JMSCorrelationId", messageId)
                .build();
    }
}