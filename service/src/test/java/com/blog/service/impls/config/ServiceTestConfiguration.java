package com.blog.service.impls.config;

import com.blog.dao.*;
import com.blog.service.AuthorService;
import com.blog.service.CommentService;
import com.blog.service.PostService;
import com.blog.service.TagService;
import com.blog.service.impls.AuthorServiceImpl;
import com.blog.service.impls.CommentServiceImpl;
import com.blog.service.impls.PostServiceImpl;
import com.blog.service.impls.TagServiceImpl;
import com.blog.validator.Validator;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The Service test configuration.
 */
@Configuration
public class ServiceTestConfiguration {
    @Bean
    public AuthorDao getMockAuthorDao() {
        return Mockito.mock(AuthorDao.class);
    }

    @Bean("mockValidator")
    public Validator getMockValidator() {
        return Mockito.mock(Validator.class);
    }

    @Bean
    public CommentDao getMockCommentDao() {
        return Mockito.mock(CommentDao.class);
    }

    @Bean
    public PostDao getMockPostDao() {
        return Mockito.mock(PostDao.class);
    }

    @Bean
    public TagDao getMockTagDao() {
        return Mockito.mock(TagDao.class);
    }

    @Bean("commentServiceMock")
    public CommentService getMockCommentService() {
        return Mockito.mock(CommentService.class);
    }

    @Bean("tagServiceMock")
    public TagService getMockTagService() {
        return Mockito.mock(TagService.class);
    }

    @Bean
    public ViewDao getMockViewDao() {
        return Mockito.mock(ViewDao.class);
    }

    @Bean
    @Autowired
    public AuthorService getAuthorService(
            @Qualifier("mockValidator") Validator validator,
            AuthorDao authorDao) {
        return new AuthorServiceImpl(authorDao, validator);
    }

    @Bean("testValidator")
    @Autowired
    public Validator getValidator(PostDao postDao, TagDao tagDao, AuthorDao authorDao, CommentDao commentDao) {
        return new Validator(postDao, tagDao, authorDao, commentDao);
    }

    @Bean("testCommentService")
    @Autowired
    public CommentService getCommentService(
            @Qualifier("mockValidator") Validator validator,
            CommentDao commentDao) {
        return new CommentServiceImpl(validator, commentDao);
    }

    @Bean("testTagService")
    @Autowired
    public TagService getTagService(
            TagDao tagDao,
            @Qualifier("mockValidator") Validator validator) {
        return new TagServiceImpl(tagDao, validator);
    }

    @Bean("testPostService")
    @Autowired
    public PostService getPostService(
            @Qualifier("mockValidator") Validator validator,
            @Qualifier("commentServiceMock") CommentService commentService,
            PostDao postDao,
            @Qualifier("tagServiceMock") TagService tagService,
            ViewDao viewDao) {
        return new PostServiceImpl(postDao, validator, tagService, commentService, viewDao);
    }
}
