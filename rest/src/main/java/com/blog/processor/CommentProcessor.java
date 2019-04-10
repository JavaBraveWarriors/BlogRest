package com.blog.processor;

import com.blog.model.Comment;
import com.blog.service.CommentService;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommentProcessor implements Processor {

    @Value("${comment.success.message}")
    private String CREATED_DEFAULT_MESSAGE;

    private CommentService commentService;

    @Autowired
    public CommentProcessor(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public void process(Exchange exchange) {
        String responseMessage = CREATED_DEFAULT_MESSAGE;
        Message commentMessage = exchange.getIn();

        try {
            commentService.addComment(commentMessage.getBody(Comment.class));
        } catch (RuntimeException ex) {
            responseMessage = ex.toString();
        }
        commentMessage.setBody(responseMessage);
        commentMessage.getHeaders().put("JMSCorrelationID", commentMessage.getHeaders().get("breadcrumbId"));
    }
}