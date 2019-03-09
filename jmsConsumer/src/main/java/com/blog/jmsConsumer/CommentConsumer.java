package com.blog.jmsConsumer;

import com.blog.Comment;
import com.blog.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.TextMessage;

@Component
public class CommentConsumer {

    private static final Logger LOGGER = LogManager.getLogger();

    private JmsTemplate jmsTemplate;
    private Queue responseCommentQueue;
    private CommentService commentService;

    public CommentConsumer(
            JmsTemplate jmsTemplate,
            @Qualifier(value = "responseComment") Queue responseCommentQueue,
            CommentService commentService) {
        this.jmsTemplate = jmsTemplate;
        this.responseCommentQueue = responseCommentQueue;
        this.commentService = commentService;
    }

    @JmsListener(destination = "${queue.comment}")
    public void receiveOrder(Message<Comment> commentMessage,
                             @Header(JmsHeaders.MESSAGE_ID) String messageId) {

        commentService.addComment(commentMessage.getPayload());
        jmsTemplate.send(responseCommentQueue, messageCreator -> {
            TextMessage message =
                    messageCreator.createTextMessage("Accepted");
            message.setJMSCorrelationID(messageId);
            return message;
        });
    }
}
