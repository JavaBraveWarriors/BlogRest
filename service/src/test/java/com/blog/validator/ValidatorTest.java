package com.blog.validator;

import com.blog.dao.AuthorDao;
import com.blog.dao.PostDao;
import com.blog.dao.TagDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorTest {

    @Mock
    private AuthorDao authorDao;

    @Mock
    private TagDao tagDao;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private Validator validator;

    private static Long CORRECT_AUTHOR_ID = 1L;
    private static Long INCORRECT_AUTHOR_ID = -2L;
    private static String CORRECT_AUTHOR_LOGIN = "testLoginCorrect";
    private static String INCORRECT_AUTHOR_LOGIN = "";


    @Test
    public void validatePostId() {
    }

    @Test
    public void validateTags() {
    }

    @Test
    public void checkPost() {
    }

    @Test
    public void validateAuthor() {
    }

    @Test
    public void validateAuthor1() {
    }

    @Test
    public void validateTagId() {
    }

    @Test
    public void checkTag() {
    }

    @Test
    public void validateInitialAndQuantity() {
    }

    @Test
    public void checkAuthorExistence() {
    }
}