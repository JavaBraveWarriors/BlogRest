package com.blog.validator;

import com.blog.Author;
import com.blog.Post;
import com.blog.Tag;
import com.blog.dao.AuthorDao;
import com.blog.dao.PostDao;
import com.blog.dao.TagDao;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Validator {

    /**
     * This field used for logging events
     */
    private static final Logger LOGGER = LogManager.getLogger();

    private PostDao postDao;
    private TagDao tagDao;
    private AuthorDao authorDao;

    //Tags messages
    @Value("${tagService.incorrectId}")
    private String incorrectTagId;

    @Value("${tagService.incorrect}")
    private String incorrectTag;

    @Value("${tagService.notExist}")
    private String notExistTag;

    @Value("${tagService.exist}")
    private String existTag;

    @Value("${postService.tagExistInPost}")
    private String tagExistInPost;

    //Posts messages
    @Value("${postService.incorrectId}")
    private String incorrectPostId;

    @Value("${postService.notExist}")
    private String notExistPost;

    @Value("${authorService.incorrectInitialNumber}")
    private String incorrectInitialNumber;

    @Value("${authorService.incorrectQuantityNumber}")
    private String incorrectQuantityNumber;

    //Authors messages
    @Value("${authorService.incorrectId}")
    private String incorrectAuthorId;

    @Value("${authorService.notExist}")
    private String authorDoesNotExist;

    @Value("${authorService.incorrectLogin}")
    private String incorrectAuthorLogin;

    @Value("${authorService.exist}")
    private String authorExist;


    @Autowired
    public Validator(PostDao postDao, TagDao tagDao, AuthorDao authorDao) {
        this.postDao = postDao;
        this.tagDao = tagDao;
        this.authorDao = authorDao;
    }

    public void validatePostId(Long id) {
        LOGGER.debug("Validates post id = [{}].", id);
        if (id == null || id < 0L)
            throw new ValidationException(incorrectPostId);
        if (!postDao.checkPostById(id))
            throw new NotFoundException(notExistPost);
    }

    public void validateTags(List<Tag> tagsValidation, TagService tagService) {
        LOGGER.debug("Validates list of tags [{}].", tagsValidation);
        tagsValidation.forEach(tag -> {
            validateTagId(tag.getId());
            if (!tag.equals( tagService.getTagById(tag.getId())))
                throw new ValidationException(incorrectTag);
        });
    }

    public void checkPost(Post post) {
        LOGGER.debug("Check post = [{}].", post);
        validatePostId(post.getId());
        validateAuthorId(post.getAuthorId());
    }

    public void validateAuthorId(Long authorId) {
        LOGGER.debug("Validate author = [{}].", authorId);
        if (authorId == null || authorId < 0L)
            throw new ValidationException(incorrectAuthorId);
        if (!authorDao.checkAuthorById(authorId))
            throw new NotFoundException(authorDoesNotExist);
    }

    public void validateAuthorLogin(String authorLogin) {
        LOGGER.debug("Validate author = [{}].", authorLogin);
        if (authorLogin == null || authorLogin.isEmpty())
            throw new ValidationException(incorrectAuthorLogin);
        if (!authorDao.checkAuthorByLogin(authorLogin))
            throw new NotFoundException(authorDoesNotExist);
    }

    public void validateTagId(Long id) {
        LOGGER.debug("Validates tag id = [{}].", id);
        if (id == null || id < 0L)
            throw new ValidationException(incorrectTagId);
        if (!tagDao.checkTagById(id))
            throw new NotFoundException(notExistTag);
    }

    public void checkTagWithTitle(Tag tag) {
        LOGGER.debug("Check tag title = [{}].", tag.getTitle());
        if (tagDao.checkTagByTitle(tag.getTitle()))
            throw new ValidationException(existTag);
    }

    public void validateInitialAndQuantity(Long initial, Long quantity) {
        LOGGER.debug("Validate numbers initial = [{}], quantity = [{}]", initial, quantity);
        if (initial == null || initial < 0L)
            throw new ValidationException(incorrectInitialNumber);
        if (quantity == null || quantity < 0L)
            throw new ValidationException(incorrectQuantityNumber);
    }

    public void checkAuthorExistence(Author author) {
        LOGGER.debug("Check author existence = [{}]", author);
        if (authorDao.checkAuthorByLogin(author.getLogin()))
            throw new ValidationException(authorExist);
    }

    public void validateTagInPost(Long postId, Long tagId) {
        if (postDao.checkTagInPostById(postId, tagId))
            throw new ValidationException(tagExistInPost);
    }

    public void validateSize(Long size) {
        if (size == null || size < 0L)
            throw new ValidationException(incorrectQuantityNumber);
    }
}
