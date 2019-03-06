package com.blog.jmsConsumer;

import com.blog.Tag;
import com.blog.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class TagConsumer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String TAG_QUEUE = "tag.queue";

    private TagService tagService;

    @Autowired
    public TagConsumer(TagService tagService) {
        this.tagService = tagService;
    }

    @JmsListener(destination = TAG_QUEUE)
    public void receiveMessage(Message<Tag> message) {
        LOGGER.debug("Add new Tag from queue [{}]", message.getPayload());
        tagService.addTag(message.getPayload());
    }
}
