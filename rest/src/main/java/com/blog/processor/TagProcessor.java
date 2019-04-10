package com.blog.processor;

import com.blog.model.Tag;
import com.blog.service.TagService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagProcessor implements Processor {

    private TagService tagService;

    @Autowired
    public TagProcessor(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public void process(Exchange exchange) {
        tagService.addTag(exchange.getIn().getBody(Tag.class));
    }
}