package com.blog.service.impls;

import com.blog.dao.AuthorDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.model.Author;
import com.blog.validator.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceImplTest {

    public static final long CORRECT_AUTHOR_ID = 21L;
    public static final long INCORRECT_AUTHOR_ID = -12L;
    public static final long NOT_EXIST_AUTHOR_ID = 124312L;
    public static final String CORRECT_LOGIN = "testLog";
    public static final String NOT_EXIST_LOGIN = "not_exist_LOG";
    public static final String EMPTY_LOGIN = "";

    @Mock
    private AuthorDao authorDao;

    @Mock
    private Validator validator;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private static Author author = new Author(
            1L,
            "test@mail.ru",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");
    private static List<Author> testAuthors = Collections.singletonList(author);

    @Test
    public void getAllAuthorsSuccess() {
        when(authorDao.getAllAuthors()).thenReturn(testAuthors);
        List<Author> allAuthors = authorService.getAllAuthors();
        verify(authorDao, times(1)).getAllAuthors();
        assertNotNull(allAuthors);
        assertEquals(allAuthors.size(), testAuthors.size());
    }

    @Test
    public void getAuthorByIdSuccess() {
        when(authorDao.getAuthorById(anyLong())).thenReturn(author);
        assertNotNull(authorService.getAuthorById(CORRECT_AUTHOR_ID));
        verify(authorDao, times(1)).getAuthorById(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getAuthorByIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        authorService.getAuthorById(INCORRECT_AUTHOR_ID);
        verify(authorDao, never()).getAuthorById(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());

    }

    @Test(expected = NotFoundException.class)
    public void getAuthorWithNotExistId() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());

        authorService.getAuthorById(NOT_EXIST_AUTHOR_ID);

        verify(authorDao, never()).getAuthorById(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test
    public void getAuthorByLoginSuccess() {
        when(authorDao.getAuthorByLogin(anyString())).thenReturn(author);
        assertNotNull(authorService.getAuthorByLogin(CORRECT_LOGIN));

        verify(authorDao, times(1)).getAuthorByLogin(anyString());
        verify(validator, times(1)).validateAuthorLogin(anyString());
    }

    @Test(expected = NotFoundException.class)
    public void getAuthorWithNotExistLogin() {
        doThrow(NotFoundException.class).when(validator).validateAuthorLogin(anyString());
        authorService.getAuthorByLogin(NOT_EXIST_LOGIN);
        verify(authorDao, never()).getAuthorByLogin(anyString());
        verify(validator, times(1)).validateAuthorLogin(anyString());
    }

    @Test(expected = ValidationException.class)
    public void getAuthorByIncorrectLogin() {
        doThrow(ValidationException.class).when(validator).validateAuthorLogin(anyString());
        authorService.getAuthorByLogin(EMPTY_LOGIN);
        verify(authorDao, never()).getAuthorByLogin(anyString());
        verify(validator, times(1)).validateAuthorLogin(anyString());
    }

    @Test
    public void addAuthorSuccess() {
        when(authorDao.addAuthor(any(Author.class))).thenReturn(1L);
        assertNotNull(authorService.addAuthor(author));
        verify(authorDao, times(1)).addAuthor(any(Author.class));
        verify(validator, times(1)).checkAuthorExistence(any(Author.class));
    }

    @Test(expected = ValidationException.class)
    public void addIncorrectAuthor() {
        doThrow(ValidationException.class).when(validator).checkAuthorExistence(any(Author.class));
        authorService.addAuthor(author);
        verify(authorDao, never()).addAuthor(any(Author.class));
        verify(validator, times(1)).checkAuthorExistence(any(Author.class));
    }

    @Test
    public void updateAuthorSuccess() {
        when(authorDao.updateAuthor(any(Author.class))).thenReturn(true);
        authorService.updateAuthor(author);
        verify(authorDao, times(1)).updateAuthor(any(Author.class));
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void updateIncorrectAuthor() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        authorService.updateAuthor(author);
        verify(authorDao, never()).updateAuthor(any(Author.class));
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void updateNotExistAuthor() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());
        authorService.updateAuthor(author);
        verify(authorDao, never()).updateAuthor(any(Author.class));
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void updateAuthorWithInternalServerException() {
        when(authorDao.updateAuthor(any(Author.class))).thenReturn(false);
        authorService.updateAuthor(author);
        verify(authorDao, never()).updateAuthor(any(Author.class));
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test
    public void deleteAuthorSuccess() {
        when(authorDao.deleteAuthor(anyLong())).thenReturn(true);
        authorService.deleteAuthor(CORRECT_AUTHOR_ID);
        verify(authorDao, times(1)).deleteAuthor(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void deleteAuthorByIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        authorService.deleteAuthor(INCORRECT_AUTHOR_ID);
        verify(authorDao, never()).deleteAuthor(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotExistAuthor() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());
        authorService.deleteAuthor(NOT_EXIST_AUTHOR_ID);
        verify(authorDao, never()).deleteAuthor(anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void deleteAuthorWithInternalServerException() {
        when(authorDao.deleteAuthor(anyLong())).thenReturn(false);
        authorService.deleteAuthor(CORRECT_AUTHOR_ID);
        verify(authorDao, times(1)).deleteAuthor(anyLong());
    }
}