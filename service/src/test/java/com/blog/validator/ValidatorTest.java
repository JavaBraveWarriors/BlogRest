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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {

    @Mock
    private AuthorDao authorDao;

    @Mock
    private TagDao tagDao;

    @Mock
    private TagService tagService;

    @Mock
    private PostDao postDao;

    @Mock
    private CommentDao commentDao;

    @InjectMocks
    private Validator validator;

    private static Long CORRECT_AUTHOR_ID = 1L;
    private static Long INCORRECT_AUTHOR_ID = -2L;
    private static String CORRECT_AUTHOR_LOGIN = "testLoginCorrect";
    private static String INCORRECT_AUTHOR_LOGIN = "";

    private static Long NULL_LONG = null;
    private static Long CORRECT_POST_ID = 1L;
    private static Long INCORRECT_POST_ID = -2L;

    private static Long CORRECT_TAG_ID = 1L;
    private static Long INCORRECT_TAG_ID = -1L;

    private static Long CORRECT_COMMENT_ID = 1L;
    private static Long INCORRECT_COMMENT_ID = -1L;

    private static Long CORRECT_INITIAL_NUMBER = 1L;
    private static Long INCORRECT_INITIAL_NUMBER = -3L;
    private static Long CORRECT_QUANTITY_NUMBER = 1L;
    private static Long INCORRECT_QUANTITY_NUMBER = -3L;
    private static String NULL_STRING = null;

    private static Tag correctTag = new Tag(4L, "test4", "test4");
    private static Tag incorrectTag = new Tag(-1L, "test1", "test1");

    private static Post correctPost = new Post(
            1L,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );

    private static Post incorrectPost1 = new Post(
            -1L,
            "testTitle",
            "testDescription",
            "testText",
            "testPath",
            1L
    );
    private static Post incorrectPost2 = new Post(
            1L,
            "testTitle",
            "testDescription",
            "testText",
            "testPath",
            -1L
    );
    private static Author author = new Author(
            1L,
            "test@mail.ru",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");

    private static Comment comment = new Comment(
            "text",
            2L,
            CORRECT_POST_ID
    );

    @Test
    public void validatePostIdSuccess() {
        when(postDao.checkPostById(anyLong())).thenReturn(true);
        validator.validatePostId(CORRECT_POST_ID);
        verify(postDao, times(1)).checkPostById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void validatePostIncorrectId() {
        validator.validatePostId(INCORRECT_POST_ID);
    }

    @Test(expected = ValidationException.class)
    public void validatePostNullId() {
        validator.validatePostId(NULL_LONG);
    }

    @Test(expected = NotFoundException.class)
    public void validateNotExistPostId() {
        when(postDao.checkPostById(anyLong())).thenReturn(false);
        validator.validatePostId(CORRECT_POST_ID);
        verify(postDao, times(1)).checkPostById(anyLong());
    }

    @Test
    public void validateUpdatedCommentSuccess() {
        when(commentDao.checkCommentInPostById(anyLong(), anyLong())).thenReturn(true);
        when(commentDao.getAuthorIdByCommentId(anyLong())).thenReturn(comment.getAuthorId());
        comment.setId(CORRECT_COMMENT_ID);
        validator.validateUpdatedComment(comment);
        verify(commentDao, times(1)).checkCommentInPostById(anyLong(), anyLong());
        verify(commentDao, times(1)).getAuthorIdByCommentId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void validateUpdatedCommentByIncorrectAuthorId() {
        when(commentDao.checkCommentInPostById(anyLong(), anyLong())).thenReturn(true);
        when(commentDao.getAuthorIdByCommentId(anyLong())).thenReturn(CORRECT_AUTHOR_ID);
        comment.setId(CORRECT_COMMENT_ID);
        validator.validateUpdatedComment(comment);
        verify(commentDao, times(1)).checkCommentInPostById(anyLong(), anyLong());
        verify(commentDao, times(1)).getAuthorIdByCommentId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void validateUpdatedCommentByIncorrectCommentId() {
        when(commentDao.checkCommentInPostById(anyLong(), anyLong())).thenReturn(false);
        comment.setId(CORRECT_COMMENT_ID);
        validator.validateUpdatedComment(comment);
        verify(commentDao, times(1)).checkCommentInPostById(anyLong(), anyLong());
    }

    @Test
    public void validateCommentId() {
        when(commentDao.checkCommentById(anyLong())).thenReturn(true);
        validator.validateCommentId(CORRECT_COMMENT_ID);
        verify(commentDao, times(1)).checkCommentById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void validateNullCommentId() {
        validator.validateCommentId(null);
    }

    @Test(expected = ValidationException.class)
    public void validateIncorrectCommentId() {
        validator.validateCommentId(INCORRECT_COMMENT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void validateNotExistCommentId() {
        when(commentDao.checkCommentById(anyLong())).thenReturn(false);
        validator.validateCommentId(CORRECT_COMMENT_ID);
        verify(commentDao, times(1)).checkCommentById(anyLong());
    }

    @Test
    public void validateTagsSuccess() {
        when(tagService.getTagById(anyLong())).thenReturn(correctTag);
        when(tagDao.checkTagById(anyLong())).thenReturn(true);
        validator.validateTags(Collections.singletonList(correctTag), tagService);
        verify(tagDao, times(1)).checkTagById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void validateNotExistTags() {
        when(tagService.getTagById(anyLong())).thenReturn(null);
        when(tagDao.checkTagById(anyLong())).thenReturn(true);
        validator.validateTags(Collections.singletonList(correctTag), tagService);
        verify(tagDao, times(1)).checkTagById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void validateTagsWhereNotEqualsWithDataInDB() {
        when(tagService.getTagById(anyLong())).thenReturn(new Tag(4L, "1", "1"));
        when(tagDao.checkTagById(anyLong())).thenReturn(true);
        validator.validateTags(Collections.singletonList(correctTag), tagService);
        verify(tagDao, times(1)).checkTagById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void validateIncorrectTags() {
        validator.validateTags(Collections.singletonList(incorrectTag), tagService);
        verify(tagDao, times(1)).checkTagById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void validateTagsWhereTagHasIdEqualsNull() {
        incorrectTag.setId(NULL_LONG);
        validator.validateTags(Collections.singletonList(incorrectTag), tagService);
        verify(tagDao, times(1)).checkTagById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void validateTagsWhereTagNotExistInDB() {
        when(tagDao.checkTagById(anyLong())).thenReturn(false);
        validator.validateTags(Collections.singletonList(correctTag), tagService);
        verify(tagDao, times(1)).checkTagById(anyLong());
    }

    @Test
    public void checkPostSuccess() {
        when(postDao.checkPostById(anyLong())).thenReturn(true);
        when(authorDao.checkAuthorById(anyLong())).thenReturn(true);
        validator.checkPost(correctPost);
        verify(authorDao, times(1)).checkAuthorById(anyLong());
        verify(postDao, times(1)).checkPostById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void checkPostWithIncorrectNegativeId() {
        validator.checkPost(incorrectPost1);
    }

    @Test(expected = ValidationException.class)
    public void checkPostWithIncorrectNegativeAuthorId() {
        when(postDao.checkPostById(anyLong())).thenReturn(true);
        validator.checkPost(incorrectPost2);
    }

    @Test(expected = NotFoundException.class)
    public void checkNotExistPost() {
        when(postDao.checkPostById(anyLong())).thenReturn(false);
        validator.checkPost(correctPost);
        verify(authorDao, never()).checkAuthorById(anyLong());
        verify(postDao, times(1)).checkPostById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void checkPostWhereAuthorIdNotEqualWithPostInDB() {
        when(postDao.checkPostById(anyLong())).thenReturn(true);
        when(authorDao.checkAuthorById(anyLong())).thenReturn(false);
        validator.checkPost(correctPost);
        verify(authorDao, times(1)).checkAuthorById(anyLong());
        verify(postDao, times(1)).checkPostById(anyLong());
    }

    @Test
    public void validateAuthorIdSuccess() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(true);
        validator.validateAuthorId(CORRECT_AUTHOR_ID);
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void validateAuthorIncorrectId() {
        validator.validateAuthorId(INCORRECT_AUTHOR_ID);
    }

    @Test(expected = ValidationException.class)
    public void validateAuthorNullId() {
        validator.validateAuthorId(NULL_LONG);
    }

    @Test(expected = NotFoundException.class)
    public void validateNotExistAuthorById() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(false);
        validator.validateAuthorId(CORRECT_AUTHOR_ID);
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test
    public void validateAuthorLoginSuccess() {
        when(authorDao.checkAuthorByLogin(anyString())).thenReturn(true);
        validator.validateAuthorLogin(CORRECT_AUTHOR_LOGIN);
    }

    @Test(expected = ValidationException.class)
    public void validateAuthorIncorrectLogin() {
        validator.validateAuthorLogin(INCORRECT_AUTHOR_LOGIN);
    }

    @Test(expected = ValidationException.class)
    public void validateAuthorNullLogin() {
        validator.validateAuthorLogin(NULL_STRING);
    }

    @Test(expected = NotFoundException.class)
    public void validateNotExistAuthorByLogin() {
        when(authorDao.checkAuthorByLogin(anyString())).thenReturn(false);
        validator.validateAuthorLogin(CORRECT_AUTHOR_LOGIN);
    }

    @Test
    public void validateTagIdSuccess() {
        when(tagDao.checkTagById(anyLong())).thenReturn(true);
        validator.validateTagId(CORRECT_TAG_ID);
    }

    @Test(expected = ValidationException.class)
    public void validateTagIncorrectId() {
        validator.validateTagId(INCORRECT_TAG_ID);
    }

    @Test(expected = ValidationException.class)
    public void validateTagNullId() {
        validator.validateTagId(NULL_LONG);
    }

    @Test(expected = NotFoundException.class)
    public void validateNotExistTagById() {
        when(tagDao.checkTagById(anyLong())).thenReturn(false);
        validator.validateTagId(CORRECT_TAG_ID);
    }

    @Test
    public void checkTagWithTitleSuccess() {
        when(tagDao.checkTagByTitle(anyString())).thenReturn(false);
        validator.checkTagWithTitle(correctTag);
    }

    @Test(expected = ValidationException.class)
    public void checkExistTagWithTitle() {
        when(tagDao.checkTagByTitle(anyString())).thenReturn(true);
        validator.checkTagWithTitle(correctTag);
    }

    @Test
    public void validateInitialAndQuantitySuccess() {
        validator.validatePageAndSize(CORRECT_INITIAL_NUMBER, CORRECT_QUANTITY_NUMBER);
    }

    @Test(expected = ValidationException.class)
    public void validateIncorrectInitialAndQuantity() {
        validator.validatePageAndSize(INCORRECT_INITIAL_NUMBER, CORRECT_QUANTITY_NUMBER);
    }

    @Test(expected = ValidationException.class)
    public void validateInitialAndIncorrectQuantity() {
        validator.validatePageAndSize(CORRECT_INITIAL_NUMBER, INCORRECT_QUANTITY_NUMBER);
    }

    @Test(expected = ValidationException.class)
    public void validateIncorrectInitialAndIncorrectQuantity() {
        validator.validatePageAndSize(INCORRECT_INITIAL_NUMBER, INCORRECT_QUANTITY_NUMBER);
    }

    @Test(expected = ValidationException.class)
    public void validateNullInitialAndQuantity() {
        validator.validatePageAndSize(NULL_LONG, CORRECT_QUANTITY_NUMBER);
    }

    @Test(expected = ValidationException.class)
    public void validateInitialAndNullQuantity() {
        validator.validatePageAndSize(CORRECT_INITIAL_NUMBER, NULL_LONG);
    }


    @Test(expected = ValidationException.class)
    public void validateNullInitialAndNullQuantity() {
        validator.validatePageAndSize(NULL_LONG, NULL_LONG);
    }

    @Test
    public void checkAuthorExistenceSuccess() {
        when(authorDao.checkAuthorByLogin(anyString())).thenReturn(false);
        validator.checkAuthorExistence(author);
    }

    @Test(expected = ValidationException.class)
    public void checkExistAuthorExistence() {
        when(authorDao.checkAuthorByLogin(anyString())).thenReturn(true);
        validator.checkAuthorExistence(author);
    }

    @Test
    public void validateTagInPostSuccess() {
        when(postDao.checkTagInPostById(anyLong(), anyLong())).thenReturn(false);
        validator.validateTagInPost(CORRECT_POST_ID, CORRECT_TAG_ID);
    }

    @Test(expected = ValidationException.class)
    public void validateExistTagInPost() {
        when(postDao.checkTagInPostById(anyLong(), anyLong())).thenReturn(true);
        validator.validateTagInPost(CORRECT_POST_ID, CORRECT_TAG_ID);
    }
}