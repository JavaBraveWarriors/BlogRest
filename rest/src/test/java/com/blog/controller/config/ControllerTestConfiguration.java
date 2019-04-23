package com.blog.controller.config;

import com.blog.JsonConverter;
import com.blog.controller.AuthorRestController;
import com.blog.controller.CommentRestController;
import com.blog.controller.PostRestController;
import com.blog.controller.TagRestController;
import com.blog.service.AuthorService;
import com.blog.service.CommentService;
import com.blog.service.PostService;
import com.blog.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.Mockito.mock;

/**
 * The Controller test configuration.
 */
@Configuration
@PropertySource("classpath:controller.properties")
public class ControllerTestConfiguration {

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        return objectMapper;
    }

    @Bean
    @Autowired
    public CommentRestController getCommentRestController(CommentService commentService) {
        return new CommentRestController(commentService);
    }

    @Bean
    @Autowired
    public PostRestController getPostRestController(PostService postService) {
        return new PostRestController(postService);
    }

    @Bean
    @Autowired
    public TagRestController getTagRestController(TagService tagService){
        return new TagRestController(tagService);
    }

    @Bean
    @Autowired
    public AuthorRestController getAuthorRestController(AuthorService authorService, PostService postService) {
        return new AuthorRestController(authorService, postService);
    }

    @Bean
    public TagService getTagService() {
        return mock(TagService.class);
    }

    @Bean
    public AuthorService getAuthorService() {
        return mock(AuthorService.class);
    }

    @Bean
    public PostService getPostService() {
        return mock(PostService.class);
    }

    @Bean
    public CommentService getCommentService() {
        return mock(CommentService.class);
    }

    @Bean
    @Autowired
    public JsonConverter getJsonConverter(ObjectMapper objectMapper) {
        return new JsonConverter(objectMapper);
    }
}