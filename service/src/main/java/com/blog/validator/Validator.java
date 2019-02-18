package com.blog.validator;

import com.blog.Author;
import com.blog.Comment;
import com.blog.Post;
import com.blog.Tag;
import com.blog.dao.AuthorDao;
import com.blog.dao.CommentDao;
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

import java.util.Arrays;
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
    private CommentDao commentDao;

    //Tags messages
    @Value("${tagService.incorrectId}")
    private String incorrectTagId;

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

    //Comments messages
    @Value("${commentService.notExist}")
    private String commentDoesNotExist;

    @Value("${commentService.incorrectUpdate}")
    private String commentIncorrectUpdate;

    @Value("${commentService.incorrectCommentId}")
    private String incorrectCommentId;

    @Value("${commentService.notExistCommentInPost}")
    private String notExistCommentInPost;

    @Value("${commentService.commentTextNull}")
    private String commentTextNull;

    @Autowired
    public Validator(PostDao postDao, TagDao tagDao, AuthorDao authorDao, CommentDao commentDao) {
        this.postDao = postDao;
        this.tagDao = tagDao;
        this.authorDao = authorDao;
        this.commentDao = commentDao;
    }

    public void validateTags(List<Long> tagsValidation) {
        LOGGER.debug("Validates list of tags [{}].", tagsValidation);
        tagsValidation.forEach(this::validateTagId);
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

    public void validateTagInPost(Long postId, Long tagId) {
        if (postDao.checkTagInPostById(postId, tagId))
            throw new ValidationException(tagExistInPost);
    }

    public void validatePostId(Long id) {
        LOGGER.debug("Validates post id = [{}].", id);
        if (id == null || id < 0L)
            throw new ValidationException(incorrectPostId);
        if (!postDao.checkPostById(id))
            throw new NotFoundException(notExistPost);
    }

    public void checkPost(Post post) {
        LOGGER.debug("Check post = [{}].", post);
        validatePostId(post.getId());
        validateAuthorId(post.getAuthorId());
    }

    public void validateSizeOfPages(Long size) {
        if (size == null || size < 0L)
            throw new ValidationException(incorrectQuantityNumber);
    }

    public void validatePageAndSize(Long page, Long size) {
        LOGGER.debug("Validate numbers page = [{}], size = [{}]", page, size);
        if (page == null || page < 0L)
            throw new ValidationException(incorrectInitialNumber);
        if (size == null || size < 0L)
            throw new ValidationException(incorrectQuantityNumber);
    }

    public void validateUpdatedComment(Comment comment) {
        LOGGER.debug("Validate comment = [{}].", comment);
        if (!commentDao.checkCommentInPostById(comment.getId(), comment.getPostId()))
            throw new NotFoundException(commentDoesNotExist);
        if (!commentDao.getAuthorIdByCommentId(comment.getId()).equals(comment.getAuthorId()))
            throw new ValidationException(commentIncorrectUpdate);
    }

    public void validateCommentId(Long commentId) {
        LOGGER.debug("Validate commentId = [{}].", commentId);
        if (commentId == null || commentId < 0L)
            throw new ValidationException(incorrectCommentId);
        if (!commentDao.checkCommentById(commentId))
            throw new NotFoundException(commentDoesNotExist);
    }

    public void checkAuthorExistence(Author author) {
        LOGGER.debug("Check author existence = [{}]", author);
        if (authorDao.checkAuthorByLogin(author.getLogin()))
            throw new ValidationException(authorExist);
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

    public void validateCommentInPost(Long postId, Long commentId) {
        LOGGER.debug("Validate comment in post id = [{}], comment id = [{}].", postId, commentId);
        validatePostId(postId);
        validateCommentId(commentId);
        if (!commentDao.checkCommentInPostById(commentId, postId))
            throw new NotFoundException(notExistCommentInPost);
    }
    public void validateCommentText(String text){
        LOGGER.debug("Validate comment in text= [{}].", text);
        if(text==null|| text.isEmpty()){
            throw new ValidationException(commentTextNull);
        }
    }
}
